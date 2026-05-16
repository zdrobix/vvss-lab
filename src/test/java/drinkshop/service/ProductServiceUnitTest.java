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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Lab04 - Pasul 2: Unit Testing cu Mockito pentru ProductService
 * 
 * Testează clasa ProductService în izolare absolută.
 * Repository-ul este mock-uit pentru a izola logica Service-ului.
 * 
 * Arhitectura E-V-R-S:
 * - E (Entity): Product
 * - V (Validator): Validarea din ProductService.validateProductAddition()
 * - R (Repository): Mock de Repository<Integer, Product>
 * - S (Service): ProductService
 */
@DisplayName("Lab04 - ProductService Unit Tests (cu Mockito)")
class ProductServiceUnitTest {

    @Mock
    private Repository<Integer, Product> mockProductRepository;

    private ProductService productService;

    /**
     * Setup: inițializează mock-urile și Service-ul înainte de fiecare test.
     * MockitoAnnotations.openMocks(this) activează adnotările @Mock
     * și @InjectMocks.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(mockProductRepository);
    }

    /**
     * CATEGORIA 1: Teste de Succes (Success Path)
     * Testează comportamentul când datele sunt valide și operația reușește.
     */
    @Nested
    @DisplayName("TC_Unit_Success: Teste de Succes")
    class SuccessTests {

        /**
         * TC_Unit_Success_001: Adăugare produs valid -> Repository.save() apelat o
         * singură dată
         * 
         * Scenariu:
         * - Produs cu date valide (nume 5 caractere, preț 9.99)
         * - Mock-ul Repository nu aruncă excepție
         * 
         * Aserțiuni:
         * - verify() confirmă că save() a fost apelat exact o dată cu produsul corect
         */
        @Test
        @DisplayName("TC_Unit_Success_001: Adăugare produs valid -> save() apelat")
        void testAddProductValid_SaveCalled() {
            // Arrange
            Product validProduct = new Product(1, "Espresso", 9.99,
                    CategorieBautura.CLASSIC_COFFEE, TipBautura.POWDER);

            // Act
            productService.addProduct(validProduct);

            // Assert
            verify(mockProductRepository, times(1)).save(validProduct);
        }

        /**
         * TC_Unit_Success_002: Adăugare produs cu preț la limita superioară
         * 
         * Scenariu:
         * - Produs cu preț maxim acceptat (10000.00)
         * - Testează cazul limită de preț
         * 
         * Aserțiuni:
         * - Repository.save() apelat cu produsul valid
         */
        @Test
        @DisplayName("TC_Unit_Success_002: Preț la limita superioară (10000.00) -> save() apelat")
        void testAddProductMaxPrice_SaveCalled() {
            // Arrange
            Product maxPriceProduct = new Product(2, "Premium Coffee Blend", 10000.00,
                    CategorieBautura.SPECIAL_COFFEE, TipBautura.POWDER);

            // Act
            productService.addProduct(maxPriceProduct);

            // Assert
            verify(mockProductRepository, times(1)).save(maxPriceProduct);
        }

        /**
         * TC_Unit_Success_003: Adăugare produs cu preț la limita inferioară
         * 
         * Scenariu:
         * - Produs cu preț minim acceptat (0.01)
         * - Testează cazul limită de preț jos
         * 
         * Aserțiuni:
         * - Repository.save() apelat
         */
        @Test
        @DisplayName("TC_Unit_Success_003: Preț la limita inferioară (0.01) -> save() apelat")
        void testAddProductMinPrice_SaveCalled() {
            // Arrange
            Product minPriceProduct = new Product(3, "Water", 0.01,
                    CategorieBautura.SMOOTHIE, TipBautura.PLANT_BASED);

            // Act
            productService.addProduct(minPriceProduct);

            // Assert
            verify(mockProductRepository, times(1)).save(minPriceProduct);
        }

        /**
         * TC_Unit_Success_004: Adăugare produs cu nume la lungimea minimă
         * 
         * Scenariu:
         * - Produs cu nume de 3 caractere (minim acceptat)
         * - Testează cazul limită pentru lungimea numelui
         * 
         * Aserțiuni:
         * - Repository.save() apelat
         */
        @Test
        @DisplayName("TC_Unit_Success_004: Nume cu lungimea minimă (3 char) -> save() apelat")
        void testAddProductMinNameLength_SaveCalled() {
            // Arrange
            Product minNameProduct = new Product(4, "Tea", 5.50,
                    CategorieBautura.TEA, TipBautura.WATER_BASED);

            // Act
            productService.addProduct(minNameProduct);

            // Assert
            verify(mockProductRepository, times(1)).save(minNameProduct);
        }
    }

    /**
     * CATEGORIA 2: Teste de Eșec - Validare (Failure Cases - Validation)
     * Testează comportamentul când datele sunt invalide.
     * 
     * Fiecare caz invalid trebuie:
     * 1. Să arunce ValidationException
     * 2. SĂ NU apeleze Repository.save()
     */
    @Nested
    @DisplayName("TC_Unit_Failure: Teste de Eșec (Validare)")
    class FailureTests {

        /**
         * TC_Unit_Failure_001: Produs NULL -> ValidationException aruncată
         * 
         * Scenariu:
         * - Pasare null la addProduct()
         * - Trebuie să eșueze înainte de a apela Repository
         * 
         * Aserțiuni:
         * - assertThrows() confirmă că ValidationException este aruncată
         * - verify() confirmă că save() NU a fost apelat (times(0))
         */
        @Test
        @DisplayName("TC_Unit_Failure_001: Produs NULL -> ValidationException")
        void testAddProductNull_ThrowsValidationException() {
            // Arrange
            Product nullProduct = null;

            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(nullProduct);
            });

            // Verify that save() was never called
            verify(mockProductRepository, times(0)).save(any());
        }

        /**
         * TC_Unit_Failure_002: Preț invalid (≤ 0) -> ValidationException aruncată
         * 
         * Scenariu:
         * - Preț negativ (-5.00)
         * - Validarea ar trebui să respingă
         * 
         * Aserțiuni:
         * - ValidationException aruncată cu mesaj despre preț
         * - save() NU apelat
         */
        @Test
        @DisplayName("TC_Unit_Failure_002: Preț negativ (-5.00) -> ValidationException")
        void testAddProductNegativePrice_ThrowsValidationException() {
            // Arrange
            Product negPriceProduct = new Product(5, "Invalid Coffee", -5.00,
                    CategorieBautura.CLASSIC_COFFEE, TipBautura.POWDER);

            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(negPriceProduct);
            });

            // Verify that save() was never called
            verify(mockProductRepository, times(0)).save(any());
        }

        /**
         * TC_Unit_Failure_003: Preț zero (0) -> ValidationException aruncată
         * 
         * Scenariu:
         * - Preț = 0 (invalid, trebuie > 0)
         * 
         * Aserțiuni:
         * - ValidationException aruncată
         * - save() NU apelat
         */
        @Test
        @DisplayName("TC_Unit_Failure_003: Preț zero (0) -> ValidationException")
        void testAddProductZeroPrice_ThrowsValidationException() {
            // Arrange
            Product zeroPriceProduct = new Product(6, "Free Water", 0.0,
                    CategorieBautura.SMOOTHIE, TipBautura.PLANT_BASED);

            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(zeroPriceProduct);
            });

            // Verify that save() was never called
            verify(mockProductRepository, times(0)).save(any());
        }

        /**
         * TC_Unit_Failure_004: Preț prea mare (> 10000) -> ValidationException aruncată
         * 
         * Scenariu:
         * - Preț = 15000.00 (depășește MAX_PRICE = 10000)
         * 
         * Aserțiuni:
         * - ValidationException aruncată
         * - save() NU apelat
         */
        @Test
        @DisplayName("TC_Unit_Failure_004: Preț prea mare (15000.00) -> ValidationException")
        void testAddProductExcessivePrice_ThrowsValidationException() {
            // Arrange
            Product highPriceProduct = new Product(7, "Diamond Coffee", 15000.00,
                    CategorieBautura.SPECIAL_COFFEE, TipBautura.POWDER);

            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(highPriceProduct);
            });

            // Verify that save() was never called
            verify(mockProductRepository, times(0)).save(any());
        }

        /**
         * TC_Unit_Failure_005: Nume NULL -> ValidationException aruncată
         * 
         * Scenariu:
         * - Produs cu nume = null
         * 
         * Aserțiuni:
         * - ValidationException aruncată
         * - save() NU apelat
         */
        @Test
        @DisplayName("TC_Unit_Failure_005: Nume NULL -> ValidationException")
        void testAddProductNullName_ThrowsValidationException() {
            // Arrange
            Product nullNameProduct = new Product(8, null, 5.99,
                    CategorieBautura.TEA, TipBautura.WATER_BASED);

            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(nullNameProduct);
            });

            // Verify that save() was never called
            verify(mockProductRepository, times(0)).save(any());
        }

        /**
         * TC_Unit_Failure_006: Nume gol (blank) -> ValidationException aruncată
         * 
         * Scenariu:
         * - Produs cu nume = " " (doar spații)
         * 
         * Aserțiuni:
         * - ValidationException aruncată
         * - save() NU apelat
         */
        @Test
        @DisplayName("TC_Unit_Failure_006: Nume gol (blank) -> ValidationException")
        void testAddProductBlankName_ThrowsValidationException() {
            // Arrange
            Product blankNameProduct = new Product(9, "   ", 7.50,
                    CategorieBautura.CLASSIC_COFFEE, TipBautura.POWDER);

            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(blankNameProduct);
            });

            // Verify that save() was never called
            verify(mockProductRepository, times(0)).save(any());
        }

        /**
         * TC_Unit_Failure_007: Nume prea scurt (< 3 caractere) -> ValidationException
         * 
         * Scenariu:
         * - Produs cu nume = "AB" (doar 2 caractere)
         * 
         * Aserțiuni:
         * - ValidationException aruncată cu mesaj despre lungime
         * - save() NU apelat
         */
        @Test
        @DisplayName("TC_Unit_Failure_007: Nume prea scurt (2 char) -> ValidationException")
        void testAddProductShortName_ThrowsValidationException() {
            // Arrange
            Product shortNameProduct = new Product(10, "AB", 8.99,
                    CategorieBautura.SMOOTHIE, TipBautura.PLANT_BASED);

            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(shortNameProduct);
            });

            // Verify that save() was never called
            verify(mockProductRepository, times(0)).save(any());
        }

        /**
         * TC_Unit_Failure_008: Nume prea lung (> 50 caractere) -> ValidationException
         * 
         * Scenariu:
         * - Produs cu nume cu 51 de caractere (depășește MAX = 50)
         * 
         * Aserțiuni:
         * - ValidationException aruncată
         * - save() NU apelat
         */
        @Test
        @DisplayName("TC_Unit_Failure_008: Nume prea lung (51 char) -> ValidationException")
        void testAddProductLongName_ThrowsValidationException() {
            // Arrange
            String longName = "a".repeat(51); // 51 caractere
            Product longNameProduct = new Product(11, longName, 6.50,
                    CategorieBautura.TEA, TipBautura.WATER_BASED);

            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(longNameProduct);
            });

            // Verify that save() was never called
            verify(mockProductRepository, times(0)).save(any());
        }

        /**
         * TC_Unit_Failure_009: Erori multiple (preț negativ + nume scurt)
         * 
         * Scenariu:
         * - Produs cu preț invalid ȘI nume invalid
         * - ValidationException ar trebui să conțină ambele mesaje de eroare
         * 
         * Aserțiuni:
         * - ValidationException aruncată
         * - save() NU apelat
         */
        @Test
        @DisplayName("TC_Unit_Failure_009: Erori multiple (preț negativ + nume scurt) -> ValidationException")
        void testAddProductMultipleErrors_ThrowsValidationException() {
            // Arrange
            Product multiErrorProduct = new Product(12, "X", -10.00,
                    CategorieBautura.CLASSIC_COFFEE, TipBautura.POWDER);

            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(multiErrorProduct);
            });

            // Verify that save() was never called
            verify(mockProductRepository, times(0)).save(any());
        }
    }

    /**
     * CATEGORIA 3: Teste de Interacțiune cu Mock-ul (Mock Interaction Tests)
     * Testează cum Service-ul interacționează cu Repository-ul mock-uit.
     */
    @Nested
    @DisplayName("TC_Unit_MockInteraction: Teste de Interacțiune cu Mock")
    class MockInteractionTests {

        /**
         * TC_Unit_MockInteraction_001: Repository.save() apelat exact o dată cu
         * parametrul corect
         * 
         * Scenariu:
         * - Adăugare produs valid
         * - Verifică că save() a fost apelat cu produsul exact
         * 
         * Aserțiuni:
         * - verify(mockProductRepository, times(1)).save(validProduct)
         */
        @Test
        @DisplayName("TC_Unit_MockInteraction_001: save() apelat cu parametrul corect")
        void testAddProductVerifyRepositorySaveParameter() {
            // Arrange
            Product expectedProduct = new Product(20, "Latte", 12.50,
                    CategorieBautura.MILK_COFFEE, TipBautura.POWDER);

            // Act
            productService.addProduct(expectedProduct);

            // Assert: verify that save() was called exactly once with the expected product
            verify(mockProductRepository, times(1)).save(expectedProduct);

            // Verify no other repository methods were called
            verify(mockProductRepository, never()).delete(any());
            verify(mockProductRepository, never()).update(any());
        }

        /**
         * TC_Unit_MockInteraction_002: După ValidationException, save() NU este apelat
         * 
         * Scenariu:
         * - Încercare adăugare produs invalid
         * - Validarea eșuează înainte de a ajunge la Repository
         * 
         * Aserțiuni:
         * - verify(..., never()).save(...) confirmă că save() nu a fost apelat
         */
        @Test
        @DisplayName("TC_Unit_MockInteraction_002: ValidationException previne apelul save()")
        void testAddProductInvalidPreventsSave() {
            // Arrange
            Product invalidProduct = new Product(21, "X", -1.0,
                    CategorieBautura.CLASSIC_COFFEE, TipBautura.POWDER);

            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                productService.addProduct(invalidProduct);
            });

            // Verify that save() was never called
            verify(mockProductRepository, never()).save(any());
        }

        /**
         * TC_Unit_MockInteraction_003: Mock Exception Handling
         * 
         * Scenariu:
         * - Repository mock-uit pentru a arunca excepție la save()
         * - Service-ul ar trebui să propagă excepția
         * 
         * Aserțiuni:
         * - doThrow().when() configurează mock-ul
         * - assertThrows() confirmă că excepția este propagată
         */
        @Test
        @DisplayName("TC_Unit_MockInteraction_003: Mock Repository aruncă excepție la save()")
        void testAddProductRepositoryThrowsException() {
            // Arrange
            Product validProduct = new Product(22, "Cappuccino", 11.00,
                    CategorieBautura.MILK_COFFEE, TipBautura.POWDER);

            // Configure mock to throw an exception
            doThrow(new RuntimeException("Database connection failed"))
                    .when(mockProductRepository).save(any());

            // Act & Assert
            assertThrows(RuntimeException.class, () -> {
                productService.addProduct(validProduct);
            });
        }
    }
}
