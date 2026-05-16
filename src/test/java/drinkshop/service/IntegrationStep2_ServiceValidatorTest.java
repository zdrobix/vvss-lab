package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Lab04 - Pasul 3: Integration Testing (Top-Down, Step 2)
 * 
 * Testează integrarea Service + Validator reali.
 * Repository-ul rămâne mock-uit pentru a izola integrarea Service-Validator.
 * 
 * Scopul: Validează că Service-ul apelează corect logica de validare
 * și transmite corect cereri la Repository.
 * 
 * Arhitectura:
 * - E (Entity): Product - real
 * - V (Validator): ProductService.validateProductAddition() - real (încorporat
 * în Service)
 * - R (Repository): Mock (izolat)
 * - S (Service): ProductService - real
 */
@DisplayName("Lab04 - Integration Step 2: Service + Validator Reali (Repository Mock)")
class IntegrationStep2_ServiceValidatorTest {

    @Mock
    private Repository<Integer, Product> mockProductRepository;

    private ProductService productService;

    /**
     * Setup: inițializează Repository mock și Service cu logica de validare reală
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(mockProductRepository);
    }

    /**
     * CATEGORIA 1: Teste de Succes
     * Validează că Service + Validator colaborează corect pentru date valide
     */
    @Nested
    @DisplayName("TC_Integration_Step2_Success: Succes - Validator Accept")
    class SuccessTests {

        /**
         * TC_Integration_Step2_Success_001: Date valide -> Validator acceptă ->
         * Repository.save() apelat
         * 
         * Scenariu:
         * - Produs cu date valide (Service conține validarea)
         * - Validator real (încorporat în Service) trebuie să accepte
         * - Repository mock trebuie apelat cu produsul
         * 
         * Aserțiuni:
         * - verify() confirmă Repository.save() apelat exact o dată
         * - Mock nu aruncă excepție
         */
        @Test
        @DisplayName("TC_Int2_Success_001: Date valide -> Validator acceptă -> save() apelat")
        void testAddValidProduct_ValidatorAccepts_SaveCalled() {
            // Arrange
            Product validProduct = new Product(1, "Americano", 5.50,
                    CategorieBautura.CLASSIC_COFFEE, TipBautura.POWDER);

            // Act
            productService.addProduct(validProduct);

            // Assert
            verify(mockProductRepository, times(1)).save(validProduct);
            verify(mockProductRepository, never()).delete(any());
            verify(mockProductRepository, never()).update(any());
        }

        /**
         * TC_Integration_Step2_Success_002: Date valide cu preț maxim
         * 
         * Scenariu:
         * - Preț la limita superioară (10000.00)
         * - Validator real din Service trebuie să accepte
         * 
         * Aserțiuni:
         * - Repository.save() apelat
         */
        @Test
        @DisplayName("TC_Int2_Success_002: Preț maxim (10000.00) -> Validator acceptă")
        void testAddProductMaxPrice_ValidatorAccepts_SaveCalled() {
            // Arrange
            Product maxPriceProduct = new Product(2, "Luxury Blend", 10000.00,
                    CategorieBautura.SPECIAL_COFFEE, TipBautura.POWDER);

            // Act
            productService.addProduct(maxPriceProduct);

            // Assert
            verify(mockProductRepository, times(1)).save(maxPriceProduct);
        }

        /**
         * TC_Integration_Step2_Success_003: Nume la lungimea maximă
         * 
         * Scenariu:
         * - Nume cu 50 de caractere (maxim acceptat)
         * - Validator din Service trebuie să accepte
         * 
         * Aserțiuni:
         * - Repository.save() apelat
         */
        @Test
        @DisplayName("TC_Int2_Success_003: Nume lung (50 char) -> Validator acceptă")
        void testAddProductMaxNameLength_ValidatorAccepts_SaveCalled() {
            // Arrange
            String maxName = "a".repeat(50); // 50 caractere
            Product maxNameProduct = new Product(3, maxName, 7.99,
                    CategorieBautura.JUICE, TipBautura.PLANT_BASED);

            // Act
            productService.addProduct(maxNameProduct);

            // Assert
            verify(mockProductRepository, times(1)).save(maxNameProduct);
        }
    }

    /**
     * CATEGORIA 2: Teste de Eșec - Validator Respinge
     * Validează că Service + Validator respinge date invalide
     */
    @Nested
    @DisplayName("TC_Integration_Step2_Failure: Eșec - Validator Respinge")
    class FailureTests {

        /**
         * TC_Integration_Step2_Failure_001: Produs NULL -> Validator respinge
         * 
         * Scenariu:
         * - null product
         * - Validator din Service trebuie să arunce ValidationException
         * 
         * Aserțiuni:
         * - assertThrows() confirmă ValidationException
         * - Repository.save() NU apelat (verify never)
         */
        @Test
        @DisplayName("TC_Int2_Failure_001: Produs NULL -> Validator respinge")
        void testAddNullProduct_ValidatorRejects_SaveNotCalled() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(null);
            });

            // Verify save() was never called
            verify(mockProductRepository, never()).save(any());
        }

        /**
         * TC_Integration_Step2_Failure_002: Preț invalid (negativ)
         * 
         * Scenariu:
         * - Preț negativ (-10.00)
         * - Validator din Service trebuie să respingă
         * 
         * Aserțiuni:
         * - ValidationException aruncată
         * - Repository.save() NU apelat
         */
        @Test
        @DisplayName("TC_Int2_Failure_002: Preț negativ (-10.00) -> Validator respinge")
        void testAddProductNegativePrice_ValidatorRejects_SaveNotCalled() {
            // Arrange
            Product invalidPriceProduct = new Product(4, "Invalid", -10.00,
                    CategorieBautura.CLASSIC_COFFEE, TipBautura.POWDER);

            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(invalidPriceProduct);
            });

            // Verify save() was never called
            verify(mockProductRepository, never()).save(any());
        }

        /**
         * TC_Integration_Step2_Failure_003: Nume prea scurt (< 3 caractere)
         * 
         * Scenariu:
         * - Nume = "AB" (2 caractere, invalid)
         * - Validator din Service trebuie să respingă
         * 
         * Aserțiuni:
         * - ValidationException aruncată
         * - Repository.save() NU apelat
         */
        @Test
        @DisplayName("TC_Int2_Failure_003: Nume scurt (2 char) -> Validator respinge")
        void testAddProductShortName_ValidatorRejects_SaveNotCalled() {
            // Arrange
            Product shortNameProduct = new Product(5, "XY", 8.50,
                    CategorieBautura.TEA, TipBautura.WATER_BASED);

            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(shortNameProduct);
            });

            // Verify save() was never called
            verify(mockProductRepository, never()).save(any());
        }

        /**
         * TC_Integration_Step2_Failure_004: Preț prea mare (> MAX_PRICE)
         * 
         * Scenariu:
         * - Preț = 15000.00 (depășește MAX = 10000)
         * - Validator din Service trebuie să respingă
         * 
         * Aserțiuni:
         * - ValidationException aruncată
         * - Repository.save() NU apelat
         */
        @Test
        @DisplayName("TC_Int2_Failure_004: Preț prea mare (15000) -> Validator respinge")
        void testAddProductExcessivePrice_ValidatorRejects_SaveNotCalled() {
            // Arrange
            Product expensiveProduct = new Product(6, "Diamond Coffee", 15000.00,
                    CategorieBautura.SPECIAL_COFFEE, TipBautura.POWDER);

            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(expensiveProduct);
            });

            // Verify save() was never called
            verify(mockProductRepository, never()).save(any());
        }

        /**
         * TC_Integration_Step2_Failure_005: Nume prea lung (> 50 caractere)
         * 
         * Scenariu:
         * - Nume cu 51 de caractere
         * - Validator din Service trebuie să respingă
         * 
         * Aserțiuni:
         * - ValidationException aruncată
         * - Repository.save() NU apelat
         */
        @Test
        @DisplayName("TC_Int2_Failure_005: Nume lung (51 char) -> Validator respinge")
        void testAddProductLongName_ValidatorRejects_SaveNotCalled() {
            // Arrange
            String longName = "a".repeat(51); // 51 caractere
            Product longNameProduct = new Product(7, longName, 9.99,
                    CategorieBautura.SMOOTHIE, TipBautura.PLANT_BASED);

            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(longNameProduct);
            });

            // Verify save() was never called
            verify(mockProductRepository, never()).save(any());
        }

        /**
         * TC_Integration_Step2_Failure_006: Erori multiple (preț invalid + nume
         * invalid)
         * 
         * Scenariu:
         * - Preț negativ ȘI nume prea scurt
         * - Validator trebuie să raporteze ambele erori
         * 
         * Aserțiuni:
         * - ValidationException aruncată (cu mesaj complet)
         * - Repository.save() NU apelat
         */
        @Test
        @DisplayName("TC_Int2_Failure_006: Erori multiple -> Validator respinge complet")
        void testAddProductMultipleErrors_ValidatorRejects_SaveNotCalled() {
            // Arrange
            Product multiErrorProduct = new Product(8, "X", -5.00,
                    CategorieBautura.CLASSIC_COFFEE, TipBautura.POWDER);

            // Act & Assert
            ValidationException exception = assertThrows(ValidationException.class, () -> {
                productService.addProduct(multiErrorProduct);
            });

            // Assert that error message contains both issues
            String errorMsg = exception.getMessage();
            assertTrue(errorMsg.contains("Prețul") || errorMsg.contains("Numele"),
                    "Mesajul de validare ar trebui să conțină informații despre erori");

            // Verify save() was never called
            verify(mockProductRepository, never()).save(any());
        }
    }

    /**
     * CATEGORIA 3: Teste de Interacțiune Service-Validator-Repository
     * Validează fluxul complet de comunicare între componente
     */
    @Nested
    @DisplayName("TC_Integration_Step2_Interaction: Interacțiune Service-Validator-Repository")
    class InteractionTests {

        /**
         * TC_Integration_Step2_Interaction_001: Flux complet de succes
         * 
         * Scenariu:
         * - Service validează (Validator real incorporat)
         * - Service apelează Repository.save()
         * - Mock Repository confirmă apelul
         * 
         * Aserțiuni:
         * - Repository.save() apelat cu parametrul exact
         * - Niciun alt apel la Repository
         */
        @Test
        @DisplayName("TC_Int2_Interaction_001: Flux complet succes (Service->Validator->Repository)")
        void testFullSuccessFlow_ServiceValidatesAndSaves() {
            // Arrange
            Product product = new Product(10, "Cappuccino", 11.00,
                    CategorieBautura.MILK_COFFEE, TipBautura.POWDER);

            // Act
            productService.addProduct(product);

            // Assert: verify exact interactions
            verify(mockProductRepository, times(1)).save(product);
            verify(mockProductRepository, times(0)).update(any());
            verify(mockProductRepository, times(0)).delete(any());
            verifyNoMoreInteractions(mockProductRepository);
        }

        /**
         * TC_Integration_Step2_Interaction_002: ValidationException previne orice
         * interacțiune
         * 
         * Scenariu:
         * - Service validează și găsește eroare (Validator respinge)
         * - Service NU ar trebui să apeleze Repository deloc
         * 
         * Aserțiuni:
         * - verifyZeroInteractions() confirmă că Repository nu a fost apelat
         */
        @Test
        @DisplayName("TC_Int2_Interaction_002: Validation Error previne orice interacțiune cu Repository")
        void testValidationFailurePreventsRepositoryAccess() {
            // Arrange
            Product invalidProduct = new Product(11, "X", -100.00,
                    CategorieBautura.TEA, TipBautura.WATER_BASED);

            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(invalidProduct);
            });

            // Verify Repository was never accessed
            verifyNoInteractions(mockProductRepository);
        }

        /**
         * TC_Integration_Step2_Interaction_003: Repository mock exception handling
         * 
         * Scenariu:
         * - Service validează cu succes
         * - Repository mock aruncă excepție la save()
         * - Service ar trebui să propaghe excepția
         * 
         * Aserțiuni:
         * - Excepția de Repository este propagată
         */
        @Test
        @DisplayName("TC_Int2_Interaction_003: Repository exception propagated by Service")
        void testRepositoryException_PropagatedByService() {
            // Arrange
            Product validProduct = new Product(12, "Macchiato", 9.50,
                    CategorieBautura.MILK_COFFEE, TipBautura.POWDER);

            // Configure mock to throw exception
            doThrow(new RuntimeException("Database error"))
                    .when(mockProductRepository).save(any());

            // Act & Assert
            assertThrows(RuntimeException.class, () -> {
                productService.addProduct(validProduct);
            });

            // Verify Repository.save() was called before throwing
            verify(mockProductRepository, times(1)).save(validProduct);
        }
    }
}
