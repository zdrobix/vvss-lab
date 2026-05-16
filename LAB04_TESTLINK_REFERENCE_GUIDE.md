# Lab04 - Pasul 4: TestLink Integration - Complete Reference Guide

## 📋 Overview: Ce informații ai nevoie pentru TestLink

Pentru a mapa corect testele Java cu TestLink, trebuie să cunoști:

1. **JavaClassName** - Clasa Java care conține testele
2. **JavaTestMethodName** - Metoda exactă a testului din clasa Java
3. **DisplayName** - Descrierea umană a testului (pentru raport)
4. **Category** - Categoria din care face parte testul (Success/Failure/Interaction)
5. **Expected Result** - Ce se așteptă să se întâmple
6. **Preconditions** - Ce trebuie inițializat înainte de test

---

## 🎯 PASUL 1: Informații Generale pentru TestLink

### Test Plan Configuration

```
Test Plan Name: xyir1234_IntT_TP
Description: Lab04 - Integration Testing Plan for ProductService
Project: ProiectAAA
Version: 1.0
Status: Active
```

### Test Suite Configuration

```
Test Suite Name: xyir1234_IntT
Description: Lab04 - Integration Testing Suite for Service-Validator-Repository Architecture
Test Suite Structure: 3 main categories (Unit, Integration Step 2, Integration Step 3)
```

---

## 📊 PASUL 2: Mapping Testelor Java → TestLink

### Tabel 1: ProductServiceUnitTest (16 teste)

#### Category: TC_Unit_Success (4 teste)

| Test Case ID        | DisplayName                           | JavaClassName                            | JavaTestMethodName                     | Expected Result                                            |
| ------------------- | ------------------------------------- | ---------------------------------------- | -------------------------------------- | ---------------------------------------------------------- |
| TC_Unit_Success_001 | Adăugare produs valid → save() apelat | drinkshop.service.ProductServiceUnitTest | testAddProductValid_SaveCalled         | Repository.save() called exactly 1 time with valid product |
| TC_Unit_Success_002 | Preț maxim (10000.00) → save() apelat | drinkshop.service.ProductServiceUnitTest | testAddProductMaxPrice_SaveCalled      | Repository.save() called successfully for max price        |
| TC_Unit_Success_003 | Preț minim (0.01) → save() apelat     | drinkshop.service.ProductServiceUnitTest | testAddProductMinPrice_SaveCalled      | Repository.save() called successfully for min price        |
| TC_Unit_Success_004 | Nume minim (3 char) → save() apelat   | drinkshop.service.ProductServiceUnitTest | testAddProductMinNameLength_SaveCalled | Repository.save() called successfully for min length name  |

**Preconditions:**

- MockitoAnnotations initialized
- @Mock Repository created
- ProductService instance created with mock repository

#### Category: TC_Unit_Failure (9 teste)

| Test Case ID        | DisplayName                                    | JavaClassName                            | JavaTestMethodName                                     | Expected Result                                               |
| ------------------- | ---------------------------------------------- | ---------------------------------------- | ------------------------------------------------------ | ------------------------------------------------------------- |
| TC_Unit_Failure_001 | Produs NULL → ValidationException              | drinkshop.service.ProductServiceUnitTest | testAddProductNull_ThrowsValidationException           | ValidationException thrown, save() NOT called                 |
| TC_Unit_Failure_002 | Preț negativ (-5.00) → ValidationException     | drinkshop.service.ProductServiceUnitTest | testAddProductNegativePrice_ThrowsValidationException  | ValidationException thrown, save() NOT called                 |
| TC_Unit_Failure_003 | Preț zero (0) → ValidationException            | drinkshop.service.ProductServiceUnitTest | testAddProductZeroPrice_ThrowsValidationException      | ValidationException thrown, save() NOT called                 |
| TC_Unit_Failure_004 | Preț prea mare (15000) → ValidationException   | drinkshop.service.ProductServiceUnitTest | testAddProductExcessivePrice_ThrowsValidationException | ValidationException thrown, save() NOT called                 |
| TC_Unit_Failure_005 | Nume NULL → ValidationException                | drinkshop.service.ProductServiceUnitTest | testAddProductNullName_ThrowsValidationException       | ValidationException thrown, save() NOT called                 |
| TC_Unit_Failure_006 | Nume gol (blank) → ValidationException         | drinkshop.service.ProductServiceUnitTest | testAddProductBlankName_ThrowsValidationException      | ValidationException thrown, save() NOT called                 |
| TC_Unit_Failure_007 | Nume prea scurt (2 char) → ValidationException | drinkshop.service.ProductServiceUnitTest | testAddProductShortName_ThrowsValidationException      | ValidationException thrown, save() NOT called                 |
| TC_Unit_Failure_008 | Nume prea lung (51 char) → ValidationException | drinkshop.service.ProductServiceUnitTest | testAddProductLongName_ThrowsValidationException       | ValidationException thrown, save() NOT called                 |
| TC_Unit_Failure_009 | Erori multiple → ValidationException           | drinkshop.service.ProductServiceUnitTest | testAddProductMultipleErrors_ThrowsValidationException | ValidationException thrown with all errors, save() NOT called |

**Preconditions:** (Same as Success tests)

#### Category: TC_Unit_MockInteraction (3 teste)

| Test Case ID                | DisplayName                        | JavaClassName                            | JavaTestMethodName                          | Expected Result                                                 |
| --------------------------- | ---------------------------------- | ---------------------------------------- | ------------------------------------------- | --------------------------------------------------------------- |
| TC_Unit_MockInteraction_001 | save() apelat cu parametrul corect | drinkshop.service.ProductServiceUnitTest | testAddProductVerifyRepositorySaveParameter | verify() confirms save() called 1x, no other methods called     |
| TC_Unit_MockInteraction_002 | ValidationException previne save() | drinkshop.service.ProductServiceUnitTest | testAddProductInvalidPreventsSave           | verify() confirms save() NEVER called after ValidationException |
| TC_Unit_MockInteraction_003 | Mock Repository aruncă excepție    | drinkshop.service.ProductServiceUnitTest | testAddProductRepositoryThrowsException     | RuntimeException propagated from mocked save()                  |

---

### Tabel 2: IntegrationStep2_ServiceValidatorTest (12 teste)

#### Category: TC_Integration_Step2_Success (3 teste)

| Test Case ID        | DisplayName                                     | JavaClassName                                           | JavaTestMethodName                                      | Expected Result                                            |
| ------------------- | ----------------------------------------------- | ------------------------------------------------------- | ------------------------------------------------------- | ---------------------------------------------------------- |
| TC_Int2_Success_001 | Date valide → Validator acceptă → save() apelat | drinkshop.service.IntegrationStep2_ServiceValidatorTest | testAddValidProduct_ValidatorAccepts_SaveCalled         | Real Validator accepts, mock Repository.save() called      |
| TC_Int2_Success_002 | Preț maxim (10000) → Validator acceptă          | drinkshop.service.IntegrationStep2_ServiceValidatorTest | testAddProductMaxPrice_ValidatorAccepts_SaveCalled      | Real Validator accepts max price, Repository.save() called |
| TC_Int2_Success_003 | Nume lung (50 char) → Validator acceptă         | drinkshop.service.IntegrationStep2_ServiceValidatorTest | testAddProductMaxNameLength_ValidatorAccepts_SaveCalled | Real Validator accepts max length name, save() called      |

**Preconditions:**

- Real ProductService instance
- Real validation logic (embedded in Service)
- Mocked Repository (MOCK only)
- MockitoAnnotations initialized

#### Category: TC_Integration_Step2_Failure (6 teste)

| Test Case ID        | DisplayName                                   | JavaClassName                                           | JavaTestMethodName                                          | Expected Result                                           |
| ------------------- | --------------------------------------------- | ------------------------------------------------------- | ----------------------------------------------------------- | --------------------------------------------------------- |
| TC_Int2_Failure_001 | Produs NULL → Validator respinge              | drinkshop.service.IntegrationStep2_ServiceValidatorTest | testAddNullProduct_ValidatorRejects_SaveNotCalled           | Real Validator rejects, save() NOT called                 |
| TC_Int2_Failure_002 | Preț negativ (-10) → Validator respinge       | drinkshop.service.IntegrationStep2_ServiceValidatorTest | testAddProductNegativePrice_ValidatorRejects_SaveNotCalled  | Real Validator rejects negative price, save() NOT called  |
| TC_Int2_Failure_003 | Nume scurt (2 char) → Validator respinge      | drinkshop.service.IntegrationStep2_ServiceValidatorTest | testAddProductShortName_ValidatorRejects_SaveNotCalled      | Real Validator rejects short name, save() NOT called      |
| TC_Int2_Failure_004 | Preț prea mare (15000) → Validator respinge   | drinkshop.service.IntegrationStep2_ServiceValidatorTest | testAddProductExcessivePrice_ValidatorRejects_SaveNotCalled | Real Validator rejects excessive price, save() NOT called |
| TC_Int2_Failure_005 | Nume prea lung (51 char) → Validator respinge | drinkshop.service.IntegrationStep2_ServiceValidatorTest | testAddProductLongName_ValidatorRejects_SaveNotCalled       | Real Validator rejects long name, save() NOT called       |
| TC_Int2_Failure_006 | Erori multiple → Validator respinge complet   | drinkshop.service.IntegrationStep2_ServiceValidatorTest | testAddProductMultipleErrors_ValidatorRejects_SaveNotCalled | Real Validator rejects with complete error messages       |

#### Category: TC_Integration_Step2_Interaction (3 teste)

| Test Case ID            | DisplayName                                | JavaClassName                                           | JavaTestMethodName                            | Expected Result                                                                   |
| ----------------------- | ------------------------------------------ | ------------------------------------------------------- | --------------------------------------------- | --------------------------------------------------------------------------------- |
| TC_Int2_Interaction_001 | Flux complet succes (Validate→Repo→Save)   | drinkshop.service.IntegrationStep2_ServiceValidatorTest | testFullSuccessFlow_ServiceValidatesAndSaves  | Real validation passes, mocked save() called exactly 1x                           |
| TC_Int2_Interaction_002 | Validation error previne Repository access | drinkshop.service.IntegrationStep2_ServiceValidatorTest | testValidationFailurePreventsRepositoryAccess | ValidationException thrown, verifyZeroInteractions() confirms no Repository calls |
| TC_Int2_Interaction_003 | Repository exception propagated by Service | drinkshop.service.IntegrationStep2_ServiceValidatorTest | testRepositoryException_PropagatedByService   | Mock configured to throw, Service propagates exception                            |

---

### Tabel 3: IntegrationStep3_ServiceValidatorRepositoryTest (13 teste)

#### Category: TC_Integration_Step3_Success (5 teste)

| Test Case ID        | DisplayName                                   | JavaClassName                                                     | JavaTestMethodName                               | Expected Result                                                     |
| ------------------- | --------------------------------------------- | ----------------------------------------------------------------- | ------------------------------------------------ | ------------------------------------------------------------------- |
| TC_Int3_Success_001 | Add + Save + Retrieve produs valid            | drinkshop.service.IntegrationStep3_ServiceValidatorRepositoryTest | testAddProductFullFlow_SaveAndRetrieve           | Product saved to Repository, retrievable via findOne(), data intact |
| TC_Int3_Success_002 | Adăugare multipă → findAll() returnează toate | drinkshop.service.IntegrationStep3_ServiceValidatorRepositoryTest | testAddMultipleProducts_FindAllReturnsAll        | 3 products added, findAll() returns all 3 with correct data         |
| TC_Int3_Success_003 | Update produs existent cu date valide         | drinkshop.service.IntegrationStep3_ServiceValidatorRepositoryTest | testUpdateProduct_SuccessfulUpdate               | Product updated in Repository, new data persisted                   |
| TC_Int3_Success_004 | Ștergere produs → Nu mai este în Repository   | drinkshop.service.IntegrationStep3_ServiceValidatorRepositoryTest | testDeleteProduct_ProductRemoved                 | Product deleted, findOne() returns null, findAll() empty            |
| TC_Int3_Success_005 | Filtrare după categorie                       | drinkshop.service.IntegrationStep3_ServiceValidatorRepositoryTest | testFilterByCategory_ReturnsOnlyMatchingProducts | filterByCategorie() returns only products of selected category      |

**Preconditions:**

- RepositoryInMemory instance created
- Real ProductService with real Repository (NO MOCKING)
- Real Validator logic active

#### Category: TC_Integration_Step3_Failure (5 teste)

| Test Case ID        | DisplayName                                         | JavaClassName                                                     | JavaTestMethodName                                           | Expected Result                                                   |
| ------------------- | --------------------------------------------------- | ----------------------------------------------------------------- | ------------------------------------------------------------ | ----------------------------------------------------------------- |
| TC_Int3_Failure_001 | Produs NULL → ValidationException → Repository gol  | drinkshop.service.IntegrationStep3_ServiceValidatorRepositoryTest | testAddNullProduct_ValidationFails_RepositoryEmpty           | ValidationException thrown, Repository remains empty              |
| TC_Int3_Failure_002 | Preț negativ → ValidationException → Repository gol | drinkshop.service.IntegrationStep3_ServiceValidatorRepositoryTest | testAddProductNegativePrice_ValidationFails_RepositoryEmpty  | ValidationException thrown, Repository remains empty              |
| TC_Int3_Failure_003 | Preț prea mare (15000) → ValidationException        | drinkshop.service.IntegrationStep3_ServiceValidatorRepositoryTest | testAddProductExcessivePrice_ValidationFails_RepositoryEmpty | ValidationException thrown, Repository remains empty              |
| TC_Int3_Failure_004 | Nume scurt (2 char) → ValidationException           | drinkshop.service.IntegrationStep3_ServiceValidatorRepositoryTest | testAddProductShortName_ValidationFails_RepositoryEmpty      | ValidationException thrown, Repository remains empty              |
| TC_Int3_Failure_005 | Update cu date invalide → Datele rămân neschimbate  | drinkshop.service.IntegrationStep3_ServiceValidatorRepositoryTest | testUpdateProductInvalidData_OriginalDataPreserved           | ValidationException thrown, original data preserved in Repository |

#### Category: TC_Integration_Step3_StateManagement (3 teste)

| Test Case ID                | DisplayName                              | JavaClassName                                                     | JavaTestMethodName                       | Expected Result                                                        |
| --------------------------- | ---------------------------------------- | ----------------------------------------------------------------- | ---------------------------------------- | ---------------------------------------------------------------------- |
| TC_Int3_StateManagement_001 | Operații multiple → State consistent     | drinkshop.service.IntegrationStep3_ServiceValidatorRepositoryTest | testMultipleOperations_ConsistentState   | Add 3, Delete 1, Update 1 = 2 products with correct data               |
| TC_Int3_StateManagement_002 | Validare eșuată nu corompe state         | drinkshop.service.IntegrationStep3_ServiceValidatorRepositoryTest | testValidationFailureDoesNotCorruptState | Failed add doesn't leave partial data, Repository integrity maintained |
| TC_Int3_StateManagement_003 | Filter funcționează corect după ștergeri | drinkshop.service.IntegrationStep3_ServiceValidatorRepositoryTest | testFilterAfterDeletions_CorrectResults  | Filter results correct before and after deletions, state consistent    |

---

## 🔧 PASUL 3: Custom Fields Configuration în TestLink

Când creezi un Test Case în TestLink, în secțiunea **Custom Fields**, completează:

### Field 1: JavaClassName

```
Value: drinkshop.service.<NomClasa>
Example: drinkshop.service.ProductServiceUnitTest
Example: drinkshop.service.IntegrationStep2_ServiceValidatorTest
Example: drinkshop.service.IntegrationStep3_ServiceValidatorRepositoryTest
```

### Field 2: JavaTestMethodName

```
Value: <ExactaNumeMetoda>
Example: testAddProductValid_SaveCalled
Example: testAddValidProduct_ValidatorAccepts_SaveCalled
Example: testAddProductFullFlow_SaveAndRetrieve
```

### Field 3: TestCategory

```
Value: [Success|Failure|Interaction|StateManagement]
Example: Success
Example: Failure
Example: Interaction
```

### Field 4: ExecutionLevel

```
Value: [Unit|Integration_Step2|Integration_Step3]
Example: Unit
Example: Integration_Step2
Example: Integration_Step3
```

### Field 5: PreConditions

```
Unit Tests:
- Mockito initialized with @Mock and @InjectMocks
- Mock Repository configured
- ProductService instance created with mock dependencies

Integration Step 2:
- Real ProductService instance
- Real Validator (embedded in Service)
- Mock Repository configured
- MockitoAnnotations initialized

Integration Step 3:
- Real ProductService instance
- Real Validator logic
- Real RepositoryInMemory (in-memory storage)
- No mocking used
```

---

## 📋 PASUL 4: Structura Test Suite în TestLink

```
Test Suite: xyir1234_IntT
├── Unit Testing (ProductServiceUnitTest)
│   ├── Success Tests (4)
│   │   ├── TC_Unit_Success_001
│   │   ├── TC_Unit_Success_002
│   │   ├── TC_Unit_Success_003
│   │   └── TC_Unit_Success_004
│   ├── Failure Tests (9)
│   │   ├── TC_Unit_Failure_001
│   │   ├── TC_Unit_Failure_002
│   │   ... (9 total)
│   └── Mock Interaction Tests (3)
│       ├── TC_Unit_MockInteraction_001
│       ├── TC_Unit_MockInteraction_002
│       └── TC_Unit_MockInteraction_003
│
├── Integration Step 2 (Service + Validator Real, Repo Mock)
│   ├── Success Tests (3)
│   │   ├── TC_Int2_Success_001
│   │   ├── TC_Int2_Success_002
│   │   └── TC_Int2_Success_003
│   ├── Failure Tests (6)
│   │   ├── TC_Int2_Failure_001
│   │   ... (6 total)
│   └── Interaction Tests (3)
│       ├── TC_Int2_Interaction_001
│       ├── TC_Int2_Interaction_002
│       └── TC_Int2_Interaction_003
│
└── Integration Step 3 (All Real - End-to-End)
    ├── Success Tests (5)
    │   ├── TC_Int3_Success_001
    │   ├── TC_Int3_Success_002
    │   ├── TC_Int3_Success_003
    │   ├── TC_Int3_Success_004
    │   └── TC_Int3_Success_005
    ├── Failure Tests (5)
    │   ├── TC_Int3_Failure_001
    │   ... (5 total)
    └── State Management Tests (3)
        ├── TC_Int3_StateManagement_001
        ├── TC_Int3_StateManagement_002
        └── TC_Int3_StateManagement_003
```

**Total: 41 Test Cases**

---

## 🎯 PASUL 5: Crearea în TestLink - Step by Step

### Step 1: Create Test Plan

```
Name: xyir1234_IntT_TP
Description: Lab04 - Integration Testing Plan
Project: ProiectAAA
```

### Step 2: Create Test Suite

```
Name: xyir1234_IntT
Type: Test Suite
Parent: xyir1234_IntT_TP
```

### Step 3: Create Test Cases

Repeti pentru fiecare test din tabelele de mai sus:

```
Test Case Name: TC_Unit_Success_001
Description: Adăugare produs valid → save() apelat
Steps:
  1. Create valid Product object with name="Espresso", price=9.99
  2. Call productService.addProduct(validProduct)
  3. Verify Repository.save() was called exactly once

Expected Result: verify(mockRepository, times(1)).save() succeeds

Custom Fields:
  JavaClassName: drinkshop.service.ProductServiceUnitTest
  JavaTestMethodName: testAddProductValid_SaveCalled
  TestCategory: Success
  ExecutionLevel: Unit
```

### Step 4: Link Test Cases to Test Plan

```
Click: "Add to Test Plans"
Select: xyir1234_IntT_TP
Link all 41 test cases
```

### Step 5: Generate Documentation

```
Click: "Generate Test Specification Document"
Format: .docx
Include: All test cases, descriptions, expected results, preconditions
```

---

## 📄 Document Structure for Generated Spec

```
Test Specification Document: xyir1234_IntT_Spec.docx

Contents:
1. Executive Summary
   - Total Test Cases: 41
   - Categories: Unit (16) + Integration Step 2 (12) + Integration Step 3 (13)
   - Status: Design Phase

2. Test Plan Overview
   - Plan ID: xyir1234_IntT_TP
   - Scope: ProductService Testing (E-V-R-S Architecture)
   - Environment: Java 22, JUnit 5, Mockito 5.2

3. Test Suite Details
   - Suite ID: xyir1234_IntT
   - Containers: 3 categories
   - Test Count per Category

4. Test Case Specifications
   For each category:
   - Test Case ID
   - Title (DisplayName)
   - Preconditions
   - Test Steps
   - Expected Results
   - JavaClassName (for Jenkins linking)
   - JavaTestMethodName (for execution)

5. Architecture Mapping
   - E-V-R-S Components
   - Mocking Levels (Unit → Integration Step 2 → Step 3)
   - Dependencies Chart

6. Execution Matrix
   Test Coverage by Component (Product, Validator, Repository, Service)

7. Success Criteria
   - All 41 tests must pass
   - No compilation errors
   - No skipped tests
```

---

## 🔗 Jenkins Integration (Preview for Pasul 5)

Când linkezi cu Jenkins, Jenkins va:

```
1. Parse JavaClassName → Identifică clasa
2. Parse JavaTestMethodName → Identifică metoda
3. Construiește comanda Maven:
   mvn -Dtest=ProductServiceUnitTest#testAddProductValid_SaveCalled test
4. Capturează rezultatul (PASS/FAIL)
5. Raportează înapoi în TestLink
```

---

## ✅ Checklist pentru Pasul 4

- [ ] Test Plan create: xyir1234_IntT_TP
- [ ] Test Suite create: xyir1234_IntT
- [ ] 41 Test Cases created with correct DisplayNames
- [ ] JavaClassName populated in Custom Fields (16+12+13 cases)
- [ ] JavaTestMethodName populated correctly
- [ ] All test cases linked to Test Plan
- [ ] Test Specification Document generated as .docx
- [ ] Document saved and shared

---

## 📚 Reference: Nume Exacte din Cod

### ProductServiceUnitTest Methods

```java
testAddProductValid_SaveCalled()
testAddProductMaxPrice_SaveCalled()
testAddProductMinPrice_SaveCalled()
testAddProductMinNameLength_SaveCalled()
testAddProductNull_ThrowsValidationException()
testAddProductNegativePrice_ThrowsValidationException()
testAddProductZeroPrice_ThrowsValidationException()
testAddProductExcessivePrice_ThrowsValidationException()
testAddProductNullName_ThrowsValidationException()
testAddProductBlankName_ThrowsValidationException()
testAddProductShortName_ThrowsValidationException()
testAddProductLongName_ThrowsValidationException()
testAddProductMultipleErrors_ThrowsValidationException()
testAddProductVerifyRepositorySaveParameter()
testAddProductInvalidPreventsSave()
testAddProductRepositoryThrowsException()
```

### IntegrationStep2_ServiceValidatorTest Methods

```java
testAddValidProduct_ValidatorAccepts_SaveCalled()
testAddProductMaxPrice_ValidatorAccepts_SaveCalled()
testAddProductMaxNameLength_ValidatorAccepts_SaveCalled()
testAddNullProduct_ValidatorRejects_SaveNotCalled()
testAddProductNegativePrice_ValidatorRejects_SaveNotCalled()
testAddProductShortName_ValidatorRejects_SaveNotCalled()
testAddProductExcessivePrice_ValidatorRejects_SaveNotCalled()
testAddProductLongName_ValidatorRejects_SaveNotCalled()
testAddProductMultipleErrors_ValidatorRejects_SaveNotCalled()
testFullSuccessFlow_ServiceValidatesAndSaves()
testValidationFailurePreventsRepositoryAccess()
testRepositoryException_PropagatedByService()
```

### IntegrationStep3_ServiceValidatorRepositoryTest Methods

```java
testAddProductFullFlow_SaveAndRetrieve()
testAddMultipleProducts_FindAllReturnsAll()
testUpdateProduct_SuccessfulUpdate()
testDeleteProduct_ProductRemoved()
testFilterByCategory_ReturnsOnlyMatchingProducts()
testAddNullProduct_ValidationFails_RepositoryEmpty()
testAddProductNegativePrice_ValidationFails_RepositoryEmpty()
testAddProductExcessivePrice_ValidationFails_RepositoryEmpty()
testAddProductShortName_ValidationFails_RepositoryEmpty()
testUpdateProductInvalidData_OriginalDataPreserved()
testMultipleOperations_ConsistentState()
testValidationFailureDoesNotCorruptState()
testFilterAfterDeletions_CorrectResults()
```

---

**Document Generated:** 2026-05-16
**Total Test Cases:** 41
**Framework:** JUnit 5 + Mockito 5.2.0
**Java Version:** JDK 22.0.2
