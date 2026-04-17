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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("ProductService.addProduct - White-Box Tests")
class ProductServiceAddProductWBTTest {

    @Mock
    private Repository<Integer, Product> mockProductRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(mockProductRepository);
    }

    @Nested
    @DisplayName("Statement Coverage")
    class StatementCoverage {

        @Test
        @DisplayName("TC_SC_001: valid product executes success path and saves once")
        void tcSc001_validProduct_successPath() {
            // Arrange
            Product p = new Product(
                    1,
                    "Fresh Juice",
                    12.5,
                    CategorieBautura.JUICE,
                    TipBautura.WATER_BASED);

            // Act
            productService.addProduct(p);

            // Assert
            verify(mockProductRepository, times(1)).save(p);
        }
    }

    @Nested
    @DisplayName("Branch Coverage")
    class BranchCoverage {

        @Test
        @DisplayName("TC_BC_001: product == null -> ValidationException")
        void tcBc001_nullProduct() {
            // Arrange
            Product p = null;

            // Act
            ValidationException ex = assertThrows(
                    ValidationException.class,
                    () -> productService.addProduct(p));

            // Assert
            assertTrue(ex.getMessage().contains("Produsul nu poate fi null"));
            verify(mockProductRepository, never()).save(org.mockito.ArgumentMatchers.any(Product.class));
        }

        @Test
        @DisplayName("TC_BC_002: invalid price (<=0), valid name -> ValidationException")
        void tcBc002_invalidPrice() {
            // Arrange
            Product p = new Product(
                    2,
                    "Orange Juice",
                    0.0,
                    CategorieBautura.JUICE,
                    TipBautura.WATER_BASED);

            // Act
            ValidationException ex = assertThrows(
                    ValidationException.class,
                    () -> productService.addProduct(p));

            // Assert
            assertTrue(ex.getMessage().contains("Prețul trebuie să fie > 0"));
            verify(mockProductRepository, never()).save(org.mockito.ArgumentMatchers.any(Product.class));
        }

        @Test
        @DisplayName("TC_BC_003: blank name, valid price -> ValidationException")
        void tcBc003_blankName() {
            // Arrange
            Product p = new Product(
                    3,
                    "   ",
                    10.0,
                    CategorieBautura.TEA,
                    TipBautura.BASIC);

            // Act
            ValidationException ex = assertThrows(
                    ValidationException.class,
                    () -> productService.addProduct(p));

            // Assert
            assertTrue(ex.getMessage().contains("Numele nu poate fi gol"));
            verify(mockProductRepository, never()).save(org.mockito.ArgumentMatchers.any(Product.class));
        }

        @Test
        @DisplayName("TC_BC_004: short name (<3), valid price -> ValidationException")
        void tcBc004_shortName() {
            // Arrange
            Product p = new Product(
                    4,
                    "AB",
                    10.0,
                    CategorieBautura.CLASSIC_COFFEE,
                    TipBautura.BASIC);

            // Act
            ValidationException ex = assertThrows(
                    ValidationException.class,
                    () -> productService.addProduct(p));

            // Assert
            assertTrue(ex.getMessage().contains("minim 3"));
            verify(mockProductRepository, never()).save(org.mockito.ArgumentMatchers.any(Product.class));
        }

        @Test
        @DisplayName("TC_BC_005: long name (>50), valid price -> ValidationException")
        void tcBc005_longName() {
            // Arrange
            Product p = new Product(
                    5,
                    "X".repeat(51),
                    10.0,
                    CategorieBautura.SMOOTHIE,
                    TipBautura.PLANT_BASED);

            // Act
            ValidationException ex = assertThrows(
                    ValidationException.class,
                    () -> productService.addProduct(p));

            // Assert
            assertTrue(ex.getMessage().contains("maxim 50"));
            verify(mockProductRepository, never()).save(org.mockito.ArgumentMatchers.any(Product.class));
        }

        @Test
        @DisplayName("TC_BC_006: valid product -> no exception and save called")
        void tcBc006_validProduct() {
            // Arrange
            Product p = new Product(
                    6,
                    "Green Tea",
                    8.99,
                    CategorieBautura.TEA,
                    TipBautura.WATER_BASED);

            // Act
            productService.addProduct(p);

            // Assert
            verify(mockProductRepository, times(1)).save(p);
        }
    }
}
