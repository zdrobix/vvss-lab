package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.repository.RepositoryInMemory;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Lab04 - Pasul 3: Integration Testing (Top-Down, Step 3)
 * 
 * Testează integrarea completă Service + Validator + Repository reali.
 * NO MOCKING: Toate componentele sunt instanțe reale, în memorie.
 * 
 * Scopul: Validează fluxul complet end-to-end: validare -> salvare -> regăsire
 * din Repository.
 * 
 * Arhitectura E-V-R-S:
 * - E (Entity): Product - real
 * - V (Validator): ProductService.validateProductAddition() - real
 * - R (Repository): RepositoryInMemory - real (în memorie, fără I/O)
 * - S (Service): ProductService - real
 */
@DisplayName("Lab04 - Integration Step 3: Service + Validator + Repository Reali (End-to-End)")
class IntegrationStep3_ServiceValidatorRepositoryTest {

    private Repository<Integer, Product> inMemoryRepository;
    private ProductService productService;

    /**
     * Setup: Creează repository în memorie și service cu logica reală
     */
    @BeforeEach
    void setUp() {
        inMemoryRepository = new RepositoryInMemory();
        productService = new ProductService(inMemoryRepository);
    }

    /**
     * CATEGORIA 1: Teste de Succes - Flux End-to-End
     * Validează fluxul complet: validare -> salvare -> regăsire
     */
    @Nested
    @DisplayName("TC_Integration_Step3_Success: Flux Complet End-to-End")
    class SuccessTests {

        /**
         * TC_Integration_Step3_Success_001: Adăugare produs -> Salvare în Repository ->
         * Regăsire
         * 
         * Scenariu:
         * - Crează produs valid
         * - Apelează Service.addProduct() care validează și salvează
         * - Prelucrează produsul din Repository
         * 
         * Aserțiuni:
         * - Produsul este salvat în Repository
         * - Produsul poate fi regăsit prin findOne()
         * - Datele sunt corecte (nu sunt modificate în tranzit)
         */
        @Test
        @DisplayName("TC_Int3_Success_001: Adăugare + Salvare + Regăsire produs valid")
        void testAddProductFullFlow_SaveAndRetrieve() {
            // Arrange
            Product originalProduct = new Product(1, "Espresso", 5.99,
                    CategorieBautura.CLASSIC_COFFEE, TipBautura.POWDER);

            // Act
            productService.addProduct(originalProduct);

            // Assert: Produsul este salvat și poate fi regăsit
            Product retrievedProduct = inMemoryRepository.findOne(1);
            assertNotNull(retrievedProduct, "Produsul ar trebui să fie salvat în Repository");
            assertEquals("Espresso", retrievedProduct.getNume(), "Numele ar trebui să fie intact");
            assertEquals(5.99, retrievedProduct.getPret(), "Prețul ar trebui să fie intact");
            assertEquals(CategorieBautura.CLASSIC_COFFEE, retrievedProduct.getCategorie());
            assertEquals(TipBautura.POWDER, retrievedProduct.getTip());
        }

        /**
         * TC_Integration_Step3_Success_002: Adăugare mai multor produse -> Salvare
         * multipă -> findAll()
         * 
         * Scenariu:
         * - Adaugă 3 produse valide
         * - Reține toate produsele în Repository
         * - Prelucrează lista completă
         * 
         * Aserțiuni:
         * - Repository conține exact 3 produse
         * - Datele tuturor produselor sunt corecte
         */
        @Test
        @DisplayName("TC_Int3_Success_002: Adăugare multipă + findAll() returnează toate")
        void testAddMultipleProducts_FindAllReturnsAll() {
            // Arrange
            Product product1 = new Product(1, "Cappuccino", 7.50,
                    CategorieBautura.MILK_COFFEE, TipBautura.POWDER);
            Product product2 = new Product(2, "Americano", 5.00,
                    CategorieBautura.CLASSIC_COFFEE, TipBautura.POWDER);
            Product product3 = new Product(3, "Green Tea", 4.00,
                    CategorieBautura.TEA, TipBautura.WATER_BASED);

            // Act
            productService.addProduct(product1);
            productService.addProduct(product2);
            productService.addProduct(product3);

            // Assert
            var allProducts = inMemoryRepository.findAll();
            assertEquals(3, allProducts.size(), "Repository ar trebui să conțină 3 produse");

            assertTrue(allProducts.contains(product1), "Product 1 ar trebui în Repository");
            assertTrue(allProducts.contains(product2), "Product 2 ar trebui în Repository");
            assertTrue(allProducts.contains(product3), "Product 3 ar trebui în Repository");
        }

        /**
         * TC_Integration_Step3_Success_003: Actualizare produs existent
         * 
         * Scenariu:
         * - Crează produs inițial
         * - Actualizează prețul și alte date
         * - Prelucrează datele actualizate din Repository
         * 
         * Aserțiuni:
         * - Produsul din Repository are noile date
         * - ID-ul rămâne același
         */
        @Test
        @DisplayName("TC_Int3_Success_003: Update produs existent cu date valide")
        void testUpdateProduct_SuccessfulUpdate() {
            // Arrange
            Product original = new Product(1, "Latte", 8.00,
                    CategorieBautura.MILK_COFFEE, TipBautura.POWDER);
            productService.addProduct(original);

            // Act: Actualizează prețul și categoria
            productService.updateProduct(1, "Latte Upgraded", 9.50,
                    CategorieBautura.SPECIAL_COFFEE, TipBautura.POWDER);

            // Assert
            Product updated = inMemoryRepository.findOne(1);
            assertNotNull(updated, "Produsul actualizat ar trebui să existe");
            assertEquals("Latte Upgraded", updated.getNume(), "Numele ar trebui actualizat");
            assertEquals(9.50, updated.getPret(), "Prețul ar trebui actualizat");
            assertEquals(CategorieBautura.SPECIAL_COFFEE, updated.getCategorie(),
                    "Categoria ar trebui actualizată");
        }

        /**
         * TC_Integration_Step3_Success_004: Ștergere produs
         * 
         * Scenariu:
         * - Crează produs
         * - Șterge produsul
         * - Încearcă să regăsească produsul sters
         * 
         * Aserțiuni:
         * - findOne() returnează null pentru ID-ul sters
         * - findAll() nu conține produsul sters
         */
        @Test
        @DisplayName("TC_Int3_Success_004: Ștergere produs -> Nu mai este în Repository")
        void testDeleteProduct_ProductRemoved() {
            // Arrange
            Product product = new Product(1, "Mocha", 10.00,
                    CategorieBautura.SPECIAL_COFFEE, TipBautura.POWDER);
            productService.addProduct(product);

            // Verify added
            assertNotNull(inMemoryRepository.findOne(1), "Produsul ar trebui adăugat inițial");

            // Act
            productService.deleteProduct(1);

            // Assert
            assertNull(inMemoryRepository.findOne(1), "Produsul sters ar trebui să returneze null");
            assertEquals(0, inMemoryRepository.findAll().size(), "Repository ar trebui gol");
        }

        /**
         * TC_Integration_Step3_Success_005: Filtrare după categorie
         * 
         * Scenariu:
         * - Adaugă produse din diferite categorii
         * - Filtrează după categorie
         * 
         * Aserțiuni:
         * - filterByCategorie() returnează doar produsele din categoria selectată
         */
        @Test
        @DisplayName("TC_Int3_Success_005: Filtrare după categorie")
        void testFilterByCategory_ReturnsOnlyMatchingProducts() {
            // Arrange
            productService.addProduct(new Product(1, "Espresso", 5.00,
                    CategorieBautura.CLASSIC_COFFEE, TipBautura.POWDER));
            productService.addProduct(new Product(2, "Cappuccino", 7.00,
                    CategorieBautura.MILK_COFFEE, TipBautura.POWDER));
            productService.addProduct(new Product(3, "Orange Juice", 6.00,
                    CategorieBautura.JUICE, TipBautura.PLANT_BASED));

            // Act
            var coffeeProducts = productService.filterByCategorie(CategorieBautura.CLASSIC_COFFEE);
            var juiceProducts = productService.filterByCategorie(CategorieBautura.JUICE);

            // Assert
            assertEquals(1, coffeeProducts.size(), "Ar trebui 1 produs CLASSIC_COFFEE");
            assertEquals("Espresso", coffeeProducts.get(0).getNume());

            assertEquals(1, juiceProducts.size(), "Ar trebui 1 produs JUICE");
            assertEquals("Orange Juice", juiceProducts.get(0).getNume());
        }
    }

    /**
     * CATEGORIA 2: Teste de Eșec - Validare Completă
     * Validează că Service nu salvează date invalide în Repository
     */
    @Nested
    @DisplayName("TC_Integration_Step3_Failure: Eșec - Validare Previne Salvare")
    class FailureTests {

        /**
         * TC_Integration_Step3_Failure_001: Produs NULL -> ValidationException -> Nu se
         * salvează
         * 
         * Scenariu:
         * - Încearcă să adauge null
         * - Validator din Service respinge
         * - Repository rămâne gol
         * 
         * Aserțiuni:
         * - ValidationException aruncată
         * - Repository findAll() returnează list gol
         */
        @Test
        @DisplayName("TC_Int3_Failure_001: Produs NULL -> ValidationException -> Repository gol")
        void testAddNullProduct_ValidationFails_RepositoryEmpty() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(null);
            });

            // Verify Repository is empty
            assertEquals(0, inMemoryRepository.findAll().size(),
                    "Repository ar trebui gol după eșec validare");
        }

        /**
         * TC_Integration_Step3_Failure_002: Preț invalid -> ValidationException
         * 
         * Scenariu:
         * - Adaug produs cu preț negativ
         * - Validator respinge
         * - Repository rămâne gol
         * 
         * Aserțiuni:
         * - ValidationException aruncată
         * - Repository gol
         */
        @Test
        @DisplayName("TC_Int3_Failure_002: Preț negativ -> ValidationException -> Repository gol")
        void testAddProductNegativePrice_ValidationFails_RepositoryEmpty() {
            // Arrange
            Product invalidProduct = new Product(1, "Invalid", -5.00,
                    CategorieBautura.CLASSIC_COFFEE, TipBautura.POWDER);

            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(invalidProduct);
            });

            // Verify Repository is empty
            assertEquals(0, inMemoryRepository.findAll().size(),
                    "Repository ar trebui gol după eșec validare");
        }

        /**
         * TC_Integration_Step3_Failure_003: Preț prea mare -> ValidationException
         * 
         * Scenariu:
         * - Preț depășind MAX_PRICE (10000)
         * - Validator respinge
         * 
         * Aserțiuni:
         * - ValidationException aruncată
         * - Repository gol
         */
        @Test
        @DisplayName("TC_Int3_Failure_003: Preț prea mare (15000) -> ValidationException")
        void testAddProductExcessivePrice_ValidationFails_RepositoryEmpty() {
            // Arrange
            Product expensiveProduct = new Product(1, "Diamond Coffee", 50000.00,
                    CategorieBautura.SPECIAL_COFFEE, TipBautura.POWDER);

            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(expensiveProduct);
            });

            // Verify Repository is empty
            assertEquals(0, inMemoryRepository.findAll().size());
        }

        /**
         * TC_Integration_Step3_Failure_004: Nume prea scurt -> ValidationException
         * 
         * Scenariu:
         * - Nume mai scurt de 3 caractere
         * - Validator respinge
         * 
         * Aserțiuni:
         * - ValidationException aruncată
         * - Repository gol
         */
        @Test
        @DisplayName("TC_Int3_Failure_004: Nume scurt (2 char) -> ValidationException")
        void testAddProductShortName_ValidationFails_RepositoryEmpty() {
            // Arrange
            Product shortNameProduct = new Product(1, "XY", 8.00,
                    CategorieBautura.TEA, TipBautura.WATER_BASED);

            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(shortNameProduct);
            });

            // Verify Repository is empty
            assertEquals(0, inMemoryRepository.findAll().size());
        }

        /**
         * TC_Integration_Step3_Failure_005: Update cu date invalide -> Nu modifica
         * 
         * Scenariu:
         * - Crează produs valid inițial
         * - Încearcă update cu preț invalid
         * - Datele originale ar trebui să rămână
         * 
         * Aserțiuni:
         * - ValidationException aruncată
         * - Datele din Repository rămân neschimbate
         */
        @Test
        @DisplayName("TC_Int3_Failure_005: Update cu date invalide -> Datele rămân neschimbate")
        void testUpdateProductInvalidData_OriginalDataPreserved() {
            // Arrange
            Product original = new Product(1, "Latte", 8.00,
                    CategorieBautura.MILK_COFFEE, TipBautura.POWDER);
            productService.addProduct(original);

            // Act & Assert: Încercare update cu preț invalid
            assertThrows(ValidationException.class, () -> {
                productService.updateProduct(1, "Latte", -5.00,
                        CategorieBautura.MILK_COFFEE, TipBautura.POWDER);
            });

            // Verify original data is preserved
            Product retrieved = inMemoryRepository.findOne(1);
            assertEquals(8.00, retrieved.getPret(), "Prețul original ar trebui păstrat");
            assertEquals("Latte", retrieved.getNume(), "Numele original ar trebui păstrat");
        }
    }

    /**
     * CATEGORIA 3: Teste de Consistență - State Management
     * Validează starea Repository în scenarii complexe
     */
    @Nested
    @DisplayName("TC_Integration_Step3_StateManagement: Consistență și State Management")
    class StateManagementTests {

        /**
         * TC_Integration_Step3_StateManagement_001: Operații multiple - State
         * Consistency
         * 
         * Scenariu:
         * - Adaugă 3 produse
         * - Șterge 1
         * - Actualizează 1
         * - Verifică state final
         * 
         * Aserțiuni:
         * - Produse: 2 (1 adăugat + 1 actualizat)
         * - Datele sunt corecte
         */
        @Test
        @DisplayName("TC_Int3_StateManagement_001: Operații multiple -> State consistent")
        void testMultipleOperations_ConsistentState() {
            // Arrange & Act: Add 3 products
            productService.addProduct(new Product(1, "Espresso", 5.00,
                    CategorieBautura.CLASSIC_COFFEE, TipBautura.POWDER));
            productService.addProduct(new Product(2, "Cappuccino", 7.00,
                    CategorieBautura.MILK_COFFEE, TipBautura.POWDER));
            productService.addProduct(new Product(3, "Tea", 4.00,
                    CategorieBautura.TEA, TipBautura.WATER_BASED));

            // Delete product 2
            productService.deleteProduct(2);

            // Update product 3
            productService.updateProduct(3, "Green Tea", 5.00,
                    CategorieBautura.TEA, TipBautura.WATER_BASED);

            // Assert: Final state check
            var allProducts = inMemoryRepository.findAll();
            assertEquals(2, allProducts.size(), "Ar trebui 2 produse (3-1+updated)");

            // Product 1 should exist unchanged
            assertNotNull(inMemoryRepository.findOne(1), "Product 1 ar trebui să existe");
            assertEquals(5.00, inMemoryRepository.findOne(1).getPret());

            // Product 2 should not exist
            assertNull(inMemoryRepository.findOne(2), "Product 2 ar trebui sters");

            // Product 3 should be updated
            Product product3 = inMemoryRepository.findOne(3);
            assertEquals("Green Tea", product3.getNume(), "Product 3 ar trebui actualizat");
            assertEquals(5.00, product3.getPret(), "Prețul ar trebui actualizat");
        }

        /**
         * TC_Integration_Step3_StateManagement_002: Validare nu corompe Repository în
         * caz eșec
         * 
         * Scenariu:
         * - Adaugă produs valid (state = 1 produs)
         * - Încearcă să adauge produs invalid
         * - Adaugă alt produs valid
         * - Verifică consistență
         * 
         * Aserțiuni:
         * - Repository conține 2 produse (nu 3)
         * - Datele sunt corecte
         * - Nu au fost parțial salvate date invalide
         */
        @Test
        @DisplayName("TC_Int3_StateManagement_002: Validare eșuată nu corompe state")
        void testValidationFailureDoesNotCorruptState() {
            // Arrange & Act
            productService.addProduct(new Product(1, "Valid Coffee", 5.00,
                    CategorieBautura.CLASSIC_COFFEE, TipBautura.POWDER));

            // Try to add invalid product
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(new Product(2, "X", -10.00,
                        CategorieBautura.TEA, TipBautura.WATER_BASED));
            });

            // Add another valid product
            productService.addProduct(new Product(3, "Valid Tea", 4.00,
                    CategorieBautura.TEA, TipBautura.WATER_BASED));

            // Assert: Repository has exactly 2 products (not 3)
            var allProducts = inMemoryRepository.findAll();
            assertEquals(2, allProducts.size(), "Repository ar trebui să conțină 2 produse");

            // Verify both valid products are there with correct data
            assertNotNull(inMemoryRepository.findOne(1), "Product 1 ar trebui să existe");
            assertNull(inMemoryRepository.findOne(2), "Product 2 (invalid) NU ar trebui să existe");
            assertNotNull(inMemoryRepository.findOne(3), "Product 3 ar trebui să existe");
        }

        /**
         * TC_Integration_Step3_StateManagement_003: Filter pe Repository actualizat
         * 
         * Scenariu:
         * - Adaugă produse din diverse categorii
         * - Filtrează și verifică
         * - Șterge unele produse
         * - Filtrează din nou
         * 
         * Aserțiuni:
         * - Filter returnează rezultate corecte după modificări
         */
        @Test
        @DisplayName("TC_Int3_StateManagement_003: Filter funcționează corect după ștergeri")
        void testFilterAfterDeletions_CorrectResults() {
            // Arrange & Act: Add products from different categories
            productService.addProduct(new Product(1, "Espresso", 5.00,
                    CategorieBautura.CLASSIC_COFFEE, TipBautura.POWDER));
            productService.addProduct(new Product(2, "Cappuccino", 7.00,
                    CategorieBautura.MILK_COFFEE, TipBautura.POWDER));
            productService.addProduct(new Product(3, "Orange Juice", 6.00,
                    CategorieBautura.JUICE, TipBautura.PLANT_BASED));

            // First filter: should have 1 JUICE
            var juiceFirst = productService.filterByCategorie(CategorieBautura.JUICE);
            assertEquals(1, juiceFirst.size(), "Ar trebui 1 JUICE inițial");

            // Delete juice product
            productService.deleteProduct(3);

            // Second filter: should have 0 JUICE
            var juiceAfter = productService.filterByCategorie(CategorieBautura.JUICE);
            assertEquals(0, juiceAfter.size(), "Ar trebui 0 JUICE după ștergere");

            // Coffee filter should still work
            var coffeeAfter = productService.filterByCategorie(CategorieBautura.CLASSIC_COFFEE);
            assertEquals(1, coffeeAfter.size(), "Coffee ar trebui intact");
        }
    }
}
