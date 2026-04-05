package drinkshop.ui;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.domain.Reteta;
import drinkshop.domain.TipBautura;
import drinkshop.service.DrinkShopService;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TESTE WHITE-BOX PENTRU DrinkShopController.onAddProduct()
 * 
 * Tehnologii: JUnit 5.x + Mockito + JavaFX
 * Metoda testată: public void onAddProduct()
 * 
 * ANALIZA WHITE-BOX:
 * ==================
 * 
 * Node 1: r = retetaTable.getSelectionModel().getSelectedItem()
 *         ↓
 * Node 2: IF (r == null) ─── BRANCH 1
 *         ├─ TRUE:  Alert INFORMATION + return
 *         ├─ FALSE: Continue to Node 3
 *
 * Node 3: IF (service.getAllProducts().stream()...size() > 0) ─── BRANCH 2
 *         ├─ TRUE:  Alert WARNING + return
 *         ├─ FALSE: Continue to Node 4
 *
 * Node 4: Product p = new Product(...)
 * Node 5: service.addProduct(p)
 * Node 6: initData()
 * 
 * TRASEURI ACOPERIȚI:
 * ===================
 * Traseu 1: BRANCH 1.TRUE → Node 2 (r == null)
 * Traseu 2: BRANCH 1.FALSE + BRANCH 2.TRUE → Nodes 3 (product exists)
 * Traseu 3: BRANCH 1.FALSE + BRANCH 2.FALSE → Nodes 4,5,6 (success)
 */

@DisplayName("DrinkShopController - White-Box Tests (onAddProduct)")
class DrinkShopControllerWBTTest {

    @Mock
    private DrinkShopService mockService;
    
    private DrinkShopController controller;
    
    // Mock JavaFX components - vom injecta prin reflection
    @Mock
    private TableView<Reteta> mockRetetaTable;
    @Mock
    private TableView<Product> mockProductTable;
    @Mock
    private TextField mockTxtProdName;
    @Mock
    private TextField mockTxtProdPrice;
    @Mock
    private ComboBox<CategorieBautura> mockComboProdCategorie;
    @Mock
    private ComboBox<TipBautura> mockComboProdTip;
    @Mock
    private TableView<OrderItem> mockCurrentOrderTable;
    @Mock
    private Label mockLblOrderTotal;
    @Mock
    private Label mockLblTotalRevenue;

    @BeforeEach
    @DisplayName("Setup: Inițializare Controller și Mockito")
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Crează instanța controller
        controller = new DrinkShopController();
        
        // Injectează mock-urile în controller
        injectMocks();
        
        // Setează service
        controller.setService(mockService);
    }

    /**
     * Injectează mock-uri în controler folosind reflection
     */
    private void injectMocks() {
        try {
            injectField("retetaTable", mockRetetaTable);
            injectField("productTable", mockProductTable);
            injectField("txtProdName", mockTxtProdName);
            injectField("txtProdPrice", mockTxtProdPrice);
            injectField("comboProdCategorie", mockComboProdCategorie);
            injectField("comboProdTip", mockComboProdTip);
            injectField("currentOrderTable", mockCurrentOrderTable);
            injectField("lblOrderTotal", mockLblOrderTotal);
            injectField("lblTotalRevenue", mockLblTotalRevenue);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mocks: " + e.getMessage(), e);
        }
    }

    private void injectField(String fieldName, Object mockValue) throws Exception {
        var field = DrinkShopController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, mockValue);
    }

    // ============================================================================
    // NESTED CLASS 1: STATEMENT COVERAGE TESTS
    // ============================================================================

    @Nested
    @DisplayName("Statement Coverage - Acoperire maxim instrucțiuni")
    class StatementCoverageTests {

        /**
         * TC_SC_001: Traseu care execută maxim instrucțiuni
         * 
         * Scenario:
         * - Reteta selectată (r != null)
         * - Produsul NU există (Branch 2.FALSE)
         * - Se crează noul Product
         * - Se apelează service.addProduct()
         * - Se apelează initData()
         * 
         * Acopere TODOS nodurile: 1, 2, 3, 4, 5, 6
         */
        @Test
        @DisplayName("TC_SC_001: Caz valid - Crează produs (Statement Coverage)")
        void testStatementCoverage_SuccessfulProductCreation() {
            // ====== ARRANGE ======
            Reteta retetaMock = new Reteta(1, "Reteta Cola");
            var selectionModelMock = mock(TableView.TableViewSelectionModel.class);
            
            when(mockRetetaTable.getSelectionModel()).thenReturn(selectionModelMock);
            when(selectionModelMock.getSelectedItem()).thenReturn(retetaMock);
            
            when(mockTxtProdName.getText()).thenReturn("Cola Premium");
            when(mockTxtProdPrice.getText()).thenReturn("15.99");
            when(mockComboProdCategorie.getValue()).thenReturn(CategorieBautura.RACORITOARE);
            when(mockComboProdTip.getValue()).thenReturn(TipBautura.NATURALA);
            
            when(mockService.getAllProducts()).thenReturn(new ArrayList<>());

            // ====== ACT ======
            controller.onAddProduct();

            // ====== ASSERT ======
            verify(mockService, times(1)).addProduct(any(Product.class));
            System.out.println("✅ TC_SC_001 PASSED: Produs creat cu succes");
        }
    }

    // ============================================================================
    // NESTED CLASS 2: BRANCH COVERAGE TESTS
    // ============================================================================

    @Nested
    @DisplayName("Branch Coverage - Acoperire ramuri decizionale")
    class BranchCoverageTests {

        /**
         * TC_BC_001: Branch 1 = TRUE (r == null)
         * 
         * Scenariu: Nicio reteta selectată
         * 
         * Expected:
         * - Alert INFORMATION afișat
         * - NU se creează Product
         * - service.addProduct() NU apelat
         */
        @Test
        @DisplayName("TC_BC_001: Nicio reteta selectată (Branch 1.TRUE: r == null)")
        void testBranchCoverage_NoRetetaSelected() {
            // ====== ARRANGE ======
            var selectionModelMock = mock(TableView.TableViewSelectionModel.class);
            when(mockRetetaTable.getSelectionModel()).thenReturn(selectionModelMock);
            when(selectionModelMock.getSelectedItem()).thenReturn(null);

            // ====== ACT ======
            controller.onAddProduct();

            // ====== ASSERT ======
            verify(mockService, never()).addProduct(any(Product.class));
            System.out.println("✅ TC_BC_001 PASSED: Branch 1.TRUE - Product NU creat");
        }

        /**
         * TC_BC_002: Branch 1 = FALSE, Branch 2 = TRUE
         * 
         * Scenariu: Produs cu aceeași ID DEJA EXISTĂ
         * 
         * Expected:
         * - Alert WARNING afișat
         * - NU se creează noul Product
         * - service.addProduct() NU apelat
         */
        @Test
        @DisplayName("TC_BC_002: Produs deja există (Branch 1.FALSE + Branch 2.TRUE)")
        void testBranchCoverage_ProductAlreadyExists() {
            // ====== ARRANGE ======
            Reteta retetaMock = new Reteta(1, "Reteta Cola");
            var selectionModelMock = mock(TableView.TableViewSelectionModel.class);
            
            when(mockRetetaTable.getSelectionModel()).thenReturn(selectionModelMock);
            when(selectionModelMock.getSelectedItem()).thenReturn(retetaMock);
            
            Product existingProduct = new Product(1, "Cola Existentă", 10.0,
                    CategorieBautura.RACORITOARE, TipBautura.NATURALA);
            List<Product> productsWithCollision = new ArrayList<>();
            productsWithCollision.add(existingProduct);
            when(mockService.getAllProducts()).thenReturn(productsWithCollision);

            // ====== ACT ======
            controller.onAddProduct();

            // ====== ASSERT ======
            verify(mockService, never()).addProduct(any(Product.class));
            System.out.println("✅ TC_BC_002 PASSED: Branch 2.TRUE - Product NU creat");
        }

        /**
         * TC_BC_003: Branch 1 = FALSE, Branch 2 = FALSE (SUCCESS)
         * 
         * Scenario: Reteta validă + Produs NU există
         * 
         * Expected: Se creează noul Product
         */
        @Test
        @DisplayName("TC_BC_003: Reteta validă + Produs nu există (Branch 1.FALSE + Branch 2.FALSE)")
        void testBranchCoverage_SuccessfulCreation() {
            // ====== ARRANGE ======
            Reteta retetaMock = new Reteta(5, "Reteta Lemonadă");
            var selectionModelMock = mock(TableView.TableViewSelectionModel.class);
            
            when(mockRetetaTable.getSelectionModel()).thenReturn(selectionModelMock);
            when(selectionModelMock.getSelectedItem()).thenReturn(retetaMock);
            
            when(mockTxtProdName.getText()).thenReturn("Lemonadă Fresh");
            when(mockTxtProdPrice.getText()).thenReturn("12.50");
            when(mockComboProdCategorie.getValue()).thenReturn(CategorieBautura.RACORITOARE);
            when(mockComboProdTip.getValue()).thenReturn(TipBautura.NATURALA);
            
            when(mockService.getAllProducts()).thenReturn(new ArrayList<>());

            // ====== ACT ======
            controller.onAddProduct();

            // ====== ASSERT ======
            verify(mockService, times(1)).addProduct(any(Product.class));
            System.out.println("✅ TC_BC_003 PASSED: Branch SUCCESS - Product creat");
        }
    }

    // ============================================================================
    // NESTED CLASS 3: TESTE SUPLIMENTARE (DATA VALIDATION & EDGE CASES)
    // ============================================================================

    @Nested
    @DisplayName("Edge Cases & Validări suplimentare")
    class EdgeCasesTests {

        /**
         * TC_EDGE_001: Verifică datele exacte ale Product-ului
         */
        @Test
        @DisplayName("TC_EDGE_001: Verifică datele exacte ale Product-ului transmis")
        void testEdgeCase_VerifyProductDataAccuracy() {
            // ====== ARRANGE ======
            Reteta retetaMock = new Reteta(3, "Reteta Test");
            var selectionModelMock = mock(TableView.TableViewSelectionModel.class);
            
            when(mockRetetaTable.getSelectionModel()).thenReturn(selectionModelMock);
            when(selectionModelMock.getSelectedItem()).thenReturn(retetaMock);
            
            when(mockTxtProdName.getText()).thenReturn("Test Drink");
            when(mockTxtProdPrice.getText()).thenReturn("9.99");
            when(mockComboProdCategorie.getValue()).thenReturn(CategorieBautura.ENERGIZANTE);
            when(mockComboProdTip.getValue()).thenReturn(TipBautura.PREPARATA);
            when(mockService.getAllProducts()).thenReturn(new ArrayList<>());

            // ====== ACT ======
            controller.onAddProduct();

            // ====== ASSERT ======
            ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
            verify(mockService).addProduct(captor.capture());

            Product capturedProduct = captor.getValue();
            
            assertEquals(3, capturedProduct.getId());
            assertEquals("Test Drink", capturedProduct.getNume());
            assertEquals(9.99, capturedProduct.getPret(), 0.01);
            assertEquals(CategorieBautura.ENERGIZANTE, capturedProduct.getCategorie());
            assertEquals(TipBautura.PREPARATA, capturedProduct.getTip());
            
            System.out.println("✅ TC_EDGE_001 PASSED: Product-ul are datele corecte");
        }

        /**
         * TC_EDGE_002: Preț invalid (NumberFormatException)
         */
        @Test
        @DisplayName("TC_EDGE_002: Preț invalid (non-numeric) - Exception handling")
        void testEdgeCase_InvalidPrice_ThrowsException() {
            // ====== ARRANGE ======
            Reteta retetaMock = new Reteta(1, "Reteta Test");
            var selectionModelMock = mock(TableView.TableViewSelectionModel.class);
            
            when(mockRetetaTable.getSelectionModel()).thenReturn(selectionModelMock);
            when(selectionModelMock.getSelectedItem()).thenReturn(retetaMock);
            
            when(mockTxtProdName.getText()).thenReturn("Product");
            when(mockTxtProdPrice.getText()).thenReturn("INVALID_PRICE");
            when(mockService.getAllProducts()).thenReturn(new ArrayList<>());

            // ====== ACT & ASSERT ======
            assertThrows(NumberFormatException.class,
                    () -> controller.onAddProduct(),
                    "Trebuia să arunce NumberFormatException");
            
            System.out.println("✅ TC_EDGE_002 PASSED: NumberFormatException aruncată");
        }
    }
}

        /**
         * TC_BC_003: Branch 1 = FALSE, Branch 2 = FALSE (caz fericit)
         * 
         * Scenariu:
         * - Reteta e selectată (r != null) ✓
         * - Produs cu aceeași ID NU EXISTĂ (sau service returnează listă goală)
         * - Crează noul produs
         * - Apelează service.addProduct()
         * - Apelează initData()
         * 
         * Acopere ramura: Decision 1.FALSE + Decision 2.FALSE
         */
        @Test
        @DisplayName("TC_BC_003: Reteta validă + Produs nu există (Branch 1.FALSE + Branch 2.FALSE)")
        void testAddProduct_SuccessfulCreation_BothBranches False() {
            // ============ ARRANGE ============
            Reteta retetaMock = new Reteta(5, "Reteta Lemonadă");
            
            var mockSelectionModel = mock(javafx.scene.control.TableView.TableViewSelectionModel.class);
            when(mockRetetaTable.getSelectionModel()).thenReturn(mockSelectionModel);
            when(mockSelectionModel.getSelectedItem()).thenReturn(retetaMock);

            when(txtProdName.getText()).thenReturn("Lemonadă Fresh");
            when(txtProdPrice.getText()).thenReturn("12.50");
            when(comboProdCategorie.getValue()).thenReturn(CategorieBautura.RACORITOARE);
            when(comboProdTip.getValue()).thenReturn(TipBautura.NATURALA);

            // service.getAllProducts() returnează listă GOALĂ (fără coliziuni)
            when(mockService.getAllProducts()).thenReturn(new ArrayList<>());

            // ============ ACT ============
            controller.onAddProduct();

            // ============ ASSERT ============
            // Verifica că service.addProduct() a fost apelat o dată
            verify(mockService, times(1)).addProduct(any(Product.class));
            
            System.out.println("✅ TC_BC_003: Branch 1.FALSE + Branch 2.FALSE - Produs creat cu succes");
        }
    }

    // ============================================================================
    // NESTED CLASS 3: TESTE SUPLIMENTARE (EDGE CASES & INTEGRATION)
    // ============================================================================

    @Nested
    @DisplayName("Edge Cases & Validări suplimentare")
    class EdgeCaseTests {

        /**
         * TC_EDGE_001: Verifică că serviciul primește Product-ul cu datele corecte
         */
        @Test
        @DisplayName("TC_EDGE_001: Verifică datele exacte ale Product-ului transmis la service")
        void testAddProduct_VerifyProductData() {
            // ============ ARRANGE ============
            Reteta retetaMock = new Reteta(3, "Reteta Test");
            
            var mockSelectionModel = mock(javafx.scene.control.TableView.TableViewSelectionModel.class);
            when(mockRetetaTable.getSelectionModel()).thenReturn(mockSelectionModel);
            when(mockSelectionModel.getSelectedItem()).thenReturn(retetaMock);

            when(txtProdName.getText()).thenReturn("Test Drink");
            when(txtProdPrice.getText()).thenReturn("9.99");
            when(comboProdCategorie.getValue()).thenReturn(CategorieBautura.ENERGIZANTE);
            when(comboProdTip.getValue()).thenReturn(TipBautura.PREPARATA);
            when(mockService.getAllProducts()).thenReturn(new ArrayList<>());

            // ============ ACT ============
            controller.onAddProduct();

            // ============ ASSERT ============
            // Captează argumentul pass to service.addProduct()
            var captor = org.mockito.ArgumentCaptor.forClass(Product.class);
            verify(mockService).addProduct(captor.capture());

            Product capturedProduct = captor.getValue();
            
            // Verifică fiecare câmp
            assert capturedProduct.getId() == 3 : "ID trebuie să fie 3 (de la Reteta)";
            assert capturedProduct.getNume().equals("Test Drink") : "Nume incorect";
            assert capturedProduct.getPret() == 9.99 : "Preț incorect";
            assert capturedProduct.getCategorie() == CategorieBautura.ENERGIZANTE : "Categorie incorectă";
            assert capturedProduct.getTip() == TipBautura.PREPARATA : "Tip incorect";
            
            System.out.println("✅ TC_EDGE_001: Product-ul transmis la service are datele corecte");
        }

        /**
         * TC_EDGE_002: Verifica comportamentul cu preț invalid (NumberFormatException)
         * 
         * Notă: Currentul cod NU face validare, deci va arunca excepție.
         * Testul verifică că excepția se propagă.
         */
        @Test
        @DisplayName("TC_EDGE_002: Preț invalid (Non-numeric) - Exception handling")
        void testAddProduct_InvalidPrice_ThrowsException() {
            // ============ ARRANGE ============
            Reteta retetaMock = new Reteta(1, "Reteta Test");
            
            var mockSelectionModel = mock(javafx.scene.control.TableView.TableViewSelectionModel.class);
            when(mockRetetaTable.getSelectionModel()).thenReturn(mockSelectionModel);
            when(mockSelectionModel.getSelectedItem()).thenReturn(retetaMock);

            when(txtProdName.getText()).thenReturn("Product");
            when(txtProdPrice.getText()).thenReturn("INVALID_PRICE"); // Nu e numeric!
            when(mockService.getAllProducts()).thenReturn(new ArrayList<>());

            // ============ ACT & ASSERT ============
            // Verifică că se aruncă NumberFormatException la Double.parseDouble()
            try {
                controller.onAddProduct();
                assert false : "Trebuia să arunce NumberFormatException";
            } catch (NumberFormatException e) {
                System.out.println("✅ TC_EDGE_002: NumberFormatException aruncată corect pentru preț invalid");
            }
        }
    }
}
