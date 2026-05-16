# Lab04 Pasul 4: TestLink - Quick Reference Tables

## 🚀 TL;DR - CE TREBUIE SĂ FACI

1. **Intră în TestLink** → Project ProiectAAA
2. **Crează Test Plan**: `xyir1234_IntT_TP`
3. **Crează Test Suite**: `xyir1234_IntT`
4. **Adaugă 41 Test Cases** - folosind tabelele de mai jos
5. **Completează Custom Fields** cu JavaClassName și JavaTestMethodName
6. **Generează Document** .docx

---

## 📊 TABEL 1: UNIT TESTS (ProductServiceUnitTest)

### Copy-paste ready table

```
Test Case ID | Display Name | Java Method | Category
─────────────────────────────────────────────────────────────────────────────
TC_Unit_Success_001 | Adăugare produs valid | testAddProductValid_SaveCalled | Success
TC_Unit_Success_002 | Preț maxim (10000) | testAddProductMaxPrice_SaveCalled | Success
TC_Unit_Success_003 | Preț minim (0.01) | testAddProductMinPrice_SaveCalled | Success
TC_Unit_Success_004 | Nume minim (3 char) | testAddProductMinNameLength_SaveCalled | Success
TC_Unit_Failure_001 | Produs NULL | testAddProductNull_ThrowsValidationException | Failure
TC_Unit_Failure_002 | Preț negativ (-5) | testAddProductNegativePrice_ThrowsValidationException | Failure
TC_Unit_Failure_003 | Preț zero (0) | testAddProductZeroPrice_ThrowsValidationException | Failure
TC_Unit_Failure_004 | Preț prea mare (15000) | testAddProductExcessivePrice_ThrowsValidationException | Failure
TC_Unit_Failure_005 | Nume NULL | testAddProductNullName_ThrowsValidationException | Failure
TC_Unit_Failure_006 | Nume gol (blank) | testAddProductBlankName_ThrowsValidationException | Failure
TC_Unit_Failure_007 | Nume scurt (2 char) | testAddProductShortName_ThrowsValidationException | Failure
TC_Unit_Failure_008 | Nume lung (51 char) | testAddProductLongName_ThrowsValidationException | Failure
TC_Unit_Failure_009 | Erori multiple | testAddProductMultipleErrors_ThrowsValidationException | Failure
TC_Unit_MockInteraction_001 | save() cu parametru corect | testAddProductVerifyRepositorySaveParameter | Interaction
TC_Unit_MockInteraction_002 | ValidationException previne save() | testAddProductInvalidPreventsSave | Interaction
TC_Unit_MockInteraction_003 | Mock Repository exception | testAddProductRepositoryThrowsException | Interaction
```

**JavaClassName pentru toate**: `drinkshop.service.ProductServiceUnitTest`

---

## 📊 TABEL 2: INTEGRATION STEP 2 (IntegrationStep2_ServiceValidatorTest)

### Success Tests (3)

```
TC_Int2_Success_001 | Date valide → Validator acceptă | testAddValidProduct_ValidatorAccepts_SaveCalled
TC_Int2_Success_002 | Preț maxim (10000) | testAddProductMaxPrice_ValidatorAccepts_SaveCalled
TC_Int2_Success_003 | Nume lung (50 char) | testAddProductMaxNameLength_ValidatorAccepts_SaveCalled
```

### Failure Tests (6)

```
TC_Int2_Failure_001 | Produs NULL | testAddNullProduct_ValidatorRejects_SaveNotCalled
TC_Int2_Failure_002 | Preț negativ | testAddProductNegativePrice_ValidatorRejects_SaveNotCalled
TC_Int2_Failure_003 | Nume scurt | testAddProductShortName_ValidatorRejects_SaveNotCalled
TC_Int2_Failure_004 | Preț prea mare | testAddProductExcessivePrice_ValidatorRejects_SaveNotCalled
TC_Int2_Failure_005 | Nume prea lung | testAddProductLongName_ValidatorRejects_SaveNotCalled
TC_Int2_Failure_006 | Erori multiple | testAddProductMultipleErrors_ValidatorRejects_SaveNotCalled
```

### Interaction Tests (3)

```
TC_Int2_Interaction_001 | Flux complet (Validate→Save) | testFullSuccessFlow_ServiceValidatesAndSaves
TC_Int2_Interaction_002 | Validation error previne Repository | testValidationFailurePreventsRepositoryAccess
TC_Int2_Interaction_003 | Repository exception propagated | testRepositoryException_PropagatedByService
```

**JavaClassName pentru toate**: `drinkshop.service.IntegrationStep2_ServiceValidatorTest`

---

## 📊 TABEL 3: INTEGRATION STEP 3 (IntegrationStep3_ServiceValidatorRepositoryTest)

### Success Tests (5)

```
TC_Int3_Success_001 | Add + Save + Retrieve | testAddProductFullFlow_SaveAndRetrieve
TC_Int3_Success_002 | Adăugare multipă → findAll | testAddMultipleProducts_FindAllReturnsAll
TC_Int3_Success_003 | Update produs | testUpdateProduct_SuccessfulUpdate
TC_Int3_Success_004 | Ștergere produs | testDeleteProduct_ProductRemoved
TC_Int3_Success_005 | Filtrare după categorie | testFilterByCategory_ReturnsOnlyMatchingProducts
```

### Failure Tests (5)

```
TC_Int3_Failure_001 | Produs NULL → Repository gol | testAddNullProduct_ValidationFails_RepositoryEmpty
TC_Int3_Failure_002 | Preț negativ → Repository gol | testAddProductNegativePrice_ValidationFails_RepositoryEmpty
TC_Int3_Failure_003 | Preț prea mare → Repository gol | testAddProductExcessivePrice_ValidationFails_RepositoryEmpty
TC_Int3_Failure_004 | Nume scurt → Repository gol | testAddProductShortName_ValidationFails_RepositoryEmpty
TC_Int3_Failure_005 | Update invalid → Date preserve | testUpdateProductInvalidData_OriginalDataPreserved
```

### State Management Tests (3)

```
TC_Int3_StateManagement_001 | Operații multiple → State consistent | testMultipleOperations_ConsistentState
TC_Int3_StateManagement_002 | Validare eșuată nu corompe state | testValidationFailureDoesNotCorruptState
TC_Int3_StateManagement_003 | Filter după ștergeri | testFilterAfterDeletions_CorrectResults
```

**JavaClassName pentru toate**: `drinkshop.service.IntegrationStep3_ServiceValidatorRepositoryTest`

---

## 🔧 CUSTOM FIELDS - CE COMPLETEZI

```
JavaClassName:      drinkshop.service.ProductServiceUnitTest
                    (sau IntegrationStep2_... sau IntegrationStep3_...)

JavaTestMethodName: testAddProductValid_SaveCalled
                    (exact metoda din Java)

TestCategory:       Success | Failure | Interaction | StateManagement

ExecutionLevel:     Unit | Integration_Step2 | Integration_Step3

PreConditions:      (Copy din LAB04_TESTLINK_REFERENCE_GUIDE.md)
```

---

## 📋 STEP-BY-STEP CREARE ÎN TESTLINK

### 1️⃣ Crează Test Plan

```
Menu: Test Plan Management → Create Test Plan
Name: xyir1234_IntT_TP
Description: Lab04 Integration Testing Plan
Project: ProiectAAA
```

### 2️⃣ Crează Test Suite

```
Menu: Test Specification → Create Test Suite
Name: xyir1234_IntT
Parent: xyir1234_IntT_TP
Type: Test Suite
```

### 3️⃣ Crează Test Cases (Repeat 41x)

```
Menu: Test Specification → Create Test Case (sub xyir1234_IntT)

EXEMPLU PENTRU TC_Unit_Success_001:
─────────────────────────────────────────────────────────
Field: Test Case Name
Value: TC_Unit_Success_001

Field: Display Name
Value: Adăugare produs valid → save() apelat

Field: Description
Value: Testează că un produs valid este acceptat și salvat în Repository

Field: Preconditions
Value: (Copy din tabel mai sus)

Field: Test Step
Value: Create Product(1, "Espresso", 9.99, CLASSIC_COFFEE, POWDER)
Value: Call productService.addProduct(product)
Value: Assert: verify(mockRepository, times(1)).save(product)

Field: Expected Result
Value: Repository.save() called exactly once with the valid product

CUSTOM FIELDS:
Field: JavaClassName
Value: drinkshop.service.ProductServiceUnitTest

Field: JavaTestMethodName
Value: testAddProductValid_SaveCalled

Field: TestCategory
Value: Success

Field: ExecutionLevel
Value: Unit
```

### 4️⃣ Link Test Cases la Test Plan

```
După ce ai creat toate test cases:
Click pe fiecare Test Case
Button: "Add to Test Plans"
Select: xyir1234_IntT_TP
Confirm
```

### 5️⃣ Generează Document

```
Menu: Test Specification → Test Cases
Filter: Toți din xyir1234_IntT
Action: "Generate Test Specification Document"
Format: .docx
Output: xyir1234_IntT_Spec.docx
```

---

## ✨ EXEMPLU COMPLET: TC_Unit_Success_001

```
TEST CASE DETAILS
═══════════════════════════════════════════════════════════

TEST CASE NAME:
TC_Unit_Success_001

DISPLAY NAME:
Adăugare produs valid → save() apelat

DESCRIPTION:
Testează Unit Testing pentru ProductService în izolare absolută.
Validează că un produs valid este acceptat și că Repository.save()
este apelat corect o singură dată (cu Mockito verify).

LEVEL: Unit Testing

CATEGORY: Success Path

PRECONDITIONS:
• Mockito initialized with MockitoAnnotations.openMocks(this)
• @Mock Repository<Integer, Product> mockProductRepository created
• ProductService instance created: new ProductService(mockProductRepository)
• No side effects from previous tests

TEST STEPS:
1. Create Product object:
   Product product = new Product(1, "Espresso", 9.99,
       CategorieBautura.CLASSIC_COFFEE, TipBautura.POWDER)

2. Call ProductService.addProduct(product)

3. Use Mockito verify to confirm Repository.save() was called:
   verify(mockProductRepository, times(1)).save(product)

EXPECTED RESULT:
✓ Repository.save() called exactly 1 time with the product object
✓ No validation exceptions thrown
✓ Mock verification passes

ACTUAL RESULT:
(Filled after execution)
✓ PASS

CUSTOM FIELDS
═══════════════════════════════════════════════════════════
JavaClassName:        drinkshop.service.ProductServiceUnitTest
JavaTestMethodName:   testAddProductValid_SaveCalled
TestCategory:         Success
ExecutionLevel:       Unit
```

---

## 📋 LISTA DE VERIFICARE - FINAL

- [ ] Test Plan `xyir1234_IntT_TP` created
- [ ] Test Suite `xyir1234_IntT` created
- [ ] Unit Test Cases (16) added with correct names
- [ ] Integration Step 2 Test Cases (12) added
- [ ] Integration Step 3 Test Cases (13) added
- [ ] All 41 test cases linked to test plan
- [ ] JavaClassName populated in custom fields
- [ ] JavaTestMethodName populated in custom fields
- [ ] Test Specification Document generated (.docx)
- [ ] Document saved and ready for Pasul 5 (Jenkins)

---

## 🎯 PASUL 5 PREVIEW (Jenkins Integration)

Când Jenkins va rula testele:

```
Jenkins Job Trigger:
├── Parse TestLink for JavaClassName + JavaTestMethodName
├── Build Maven command:
│   mvn -Dtest=ProductServiceUnitTest#testAddProductValid_SaveCalled test
├── Execute test
├── Capture result (PASS/FAIL)
├── Report back to TestLink
└── Update Test Case status
```

**Aceasta depinde de TestLink ↔ Jenkins plugin configuration**

---

Generated: 2026-05-16
Total: 41 Test Cases Ready for TestLink
