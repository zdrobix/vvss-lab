package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TESTE UNITARE PENTRU ProductService.addProduct()
 * 
 * Tehnologii folosite: JUnit 5.x + Mockito
 * Metoda testată: public void addProduct(Product p)
 * 
 * Parametrii vizați (cu constrângeri):
 * 1. Price (pret): > 0 și <= 10000
 * 2. Name (nume): 3-50 caractere, not null/blank
 * 
 * Alți parametri (dummy - valide mereu):
 * - ID: 1 (valid)
 * - Categorie: CategorieBautura.RACORITOARE
 * - Tip: TipBautura.NATURALA
 */

@DisplayName("ProductService - Teste Adăugare Produs")
class ProductServiceTest {

    @Mock
    private Repository<Integer, Product> mockProductRepository;

    private ProductService productService;

    @BeforeEach
    @DisplayName("Setup: inițializare Mockito și ProductService")
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(mockProductRepository);
    }

    // ============================================================================
    // NESTED CLASS 1: EQUAL PARTITION CLASSES (ECP) TESTS
    // ============================================================================

    @Nested
    @DisplayName("ECP - Teste Partiții Echivalente")
    class ECPTests {

        /**
         * Teste parametrizate ECP cu @CsvSource
         * Format: price, name, shouldThrowException
         */
        @ParameterizedTest(name = "[ECP-{index}] price={0}, name={1}, shouldThrow={2}")
        @CsvSource({
                // TC_ECP_01: Valid - preț și nume normale
                "25.50, Cola Clasic, false",

                // TC_ECP_02: Invalid - preț negativ
                "-10.00, Cola Clasic, true",

                // TC_ECP_03: Invalid - nume prea scurt (2 caractere)
                "25.50, Co, true",

                // TC_ECP_04: Invalid - nume gol/blank
                "25.50, '', true"
        })
        @DisplayName("Teste ECP - Validări preț și nume")
        void testAddProductECP(String priceStr, String name, boolean shouldThrow) {
            // Arrange
            double price = Double.parseDouble(priceStr);
            Product product = new Product(
                    1,  // ID - valid (dummy)
                    name,
                    price,
                    CategorieBautura.RACORITOARE,  // Categorie - valid (dummy)
                    TipBautura.NATURALA  // Tip - valid (dummy)
            );

            // Act & Assert
            if (shouldThrow) {
                // Testified behavior: Dacă e invalid, trebuie să arunce ValidationException
                // (presupunem că ProductService ar trebui să valideze)
                
                // Notă: Codul actual NU validează în addProduct(), dar testul
                // reflectă cerința din laborator: validare la service level
                
                assertThrows(ValidationException.class,
                        () -> productService.addProduct(product),
                        "Trebuia să arunce ValidationException pentru date invalide");
                
                // Verify: repository.save() NU trebuie apelat dacă e invalid
                verify(mockProductRepository, never()).save(any(Product.class));
            } else {
                // Comportament valid: nu trebuie excepție
                assertDoesNotThrow(
                        () -> productService.addProduct(product),
                        "Nu trebuia să arunce excepție pentru date valide"
                );
                
                // Verify: repository.save() TREBUIE apelat o dată
                verify(mockProductRepository, times(1)).save(product);
            }
        }
    }

    // ============================================================================
    // NESTED CLASS 2: BOUNDARY VALUE ANALYSIS (BVA) TESTS
    // ============================================================================

    @Nested
    @DisplayName("BVA - Teste Analiză Valori la Limită")
    class BVATests {

        /**
         * Teste parametrizate BVA cu @CsvSource
         * Format: price, nameLength, name, shouldThrowException
         * 
         * Constrângeri:
         * - Price: > 0 și <= 10000
         * - Name: 3-50 caractere
         */
        @ParameterizedTest(name = "[BVA-{index}] price={0}, nameLen={1}, shouldThrow={3}")
        @CsvSource({
                // TC_BVA_01: Valid - Limite inferioare valide
                "0.01, 3, ABC, false",

                // TC_BVA_02: Valid - Limite superioare valide
                "10000.00, 50, AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA, false",

                // TC_BVA_03: Invalid - Preț la limita inferioară invalidă (= 0, nu > 0)
                "0.00, 4, Cola, true",

                // TC_BVA_04: Invalid - Nume la limita superioară invalidă (51 caractere)
                "25.50, 51, AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA, true"
        })
        @DisplayName("Teste BVA - Valori la limite")
        void testAddProductBVA(String priceStr, int nameLength, String name, boolean shouldThrow) {
            // Arrange
            double price = Double.parseDouble(priceStr);
            
            // Asigură că nume-ul are lungimea dorită (CsvSource limităru string-urile)
            String nameWithCorrectLength = name.substring(0, Math.min(name.length(), nameLength));
            if (nameWithCorrectLength.length() < nameLength) {
                nameWithCorrectLength = nameWithCorrectLength + "A".repeat(nameLength - nameWithCorrectLength.length());
            }
            
            Product product = new Product(
                    1,  // ID - valid (dummy)
                    nameWithCorrectLength,
                    price,
                    CategorieBautura.RACORITOARE,  // Categorie - valid (dummy)
                    TipBautura.NATURALA  // Tip - valid (dummy)
            );

            // Act & Assert
            if (shouldThrow) {
                assertThrows(ValidationException.class,
                        () -> productService.addProduct(product),
                        "Trebuia să arunce ValidationException pentru valori la limita invalidă");
                
                verify(mockProductRepository, never()).save(any(Product.class));
            } else {
                assertDoesNotThrow(
                        () -> productService.addProduct(product),
                        "Nu trebuia să arunce excepție pentru valori la limita validă"
                );
                
                verify(mockProductRepository, times(1)).save(product);
            }
        }
    }

    // ============================================================================
    // NESTED CLASS 3: TESTE SUPLIMENTARE CU @MethodSource
    // ============================================================================

    @Nested
    @DisplayName("Teste Suplimentare - Provider de Date")
    class AdvancedTests {

        /**
         * Teste cu @MethodSource pentru cazuri mai complexe
         * Folosim stream de objekte pentru mai multă flexibilitate
         */
        @ParameterizedTest(name = "[Advanced-{index}] {0}")
        @MethodSource("provideProductTestCases")
        @DisplayName("Teste avansate cu MethodSource")
        void testAddProductAdvanced(String description, Product product, boolean shouldThrow) {
            // Arrange
            // (produsul e deja construit în provider)

            // Act & Assert
            if (shouldThrow) {
                assertThrows(ValidationException.class,
                        () -> productService.addProduct(product),
                        "Test: " + description);
                
                verify(mockProductRepository, never()).save(any(Product.class));
            } else {
                assertDoesNotThrow(
                        () -> productService.addProduct(product),
                        "Test: " + description
                );
                
                verify(mockProductRepository, times(1)).save(product);
            }
            
            reset(mockProductRepository);  // Reset mock pentru următoarea iterație
        }

        /**
         * MethodSource: furnizează Stream de cazuri de testare complexe
         */
        static Stream<org.junit.jupiter.params.provider.Arguments> provideProductTestCases() {
            return Stream.of(
                    // Caz 1: Preț maxim valid, nume maxim valid
                    org.junit.jupiter.params.provider.Arguments.of(
                            "Preț maxim (10000) + Nume maxim (50 chars)",
                            new Product(1, "X".repeat(50), 10000.00,
                                    CategorieBautura.ALCOOLICE, TipBautura.PREPARATA),
                            false  // Should NOT throw
                    ),

                    // Caz 2: Preț extrem de mic dar valid (0.01)
                    org.junit.jupiter.params.provider.Arguments.of(
                            "Preț minim valid (0.01) + Nume minim (3 chars)",
                            new Product(1, "XYZ", 0.01,
                                    CategorieBautura.ENERGIZANTE, TipBautura.NATURALA),
                            false  // Should NOT throw
                    ),

                    // Caz 3: Preț null-like (zero - invalid)
                    org.junit.jupiter.params.provider.Arguments.of(
                            "Preț zero (invalid)",
                            new Product(1, "Valid Name", 0.0,
                                    CategorieBautura.RACORITOARE, TipBautura.NATURALA),
                            true  // Should throw
                    ),

                    // Caz 4: Nume cu spații (trebuie să numere la lungime)
                    org.junit.jupiter.params.provider.Arguments.of(
                            "Nume cu spații și caractere speciale (valid lungime)",
                            new Product(1, "Cola - Promo 2024!", 15.99,
                                    CategorieBautura.RACORITOARE, TipBautura.NATURALA),
                            false  // Should NOT throw (lungimea e 18, care e entre 3-50)
                    )
            );
        }
    }
}
