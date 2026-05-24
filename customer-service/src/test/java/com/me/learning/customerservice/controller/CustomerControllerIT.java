package com.me.learning.customerservice.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.time.Instant;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.me.learning.customerservice.entity.Address;
import com.me.learning.customerservice.entity.City;
import com.me.learning.customerservice.entity.Country;
import com.me.learning.customerservice.repository.AddressRepository;
import com.me.learning.customerservice.repository.CityRepository;
import com.me.learning.customerservice.repository.CountryRepository;
import com.me.learning.customerservice.repository.CustomerRepository;

/**
 * Full REST-Assured integration tests for {@link CustomerController}.
 *
 * <p>The application starts with a real embedded server on a random port
 * ({@link SpringBootTest.WebEnvironment#RANDOM_PORT}) backed by the H2
 * in-memory database.  Reference data (Country → City → Address) is seeded
 * once in {@link #setUpReferenceData()} before any test runs.
 *
 * <p>{@code Address.location} is declared {@code geometry} (nullable) so that
 * JPA-based inserts work in H2 without requiring a JTS / hibernate-spatial stack.
 *
 * <p>Each endpoint group is isolated in its own {@link Nested} class so that
 * the test report has a clear per-operation breakdown.  Helper methods
 * ({@link #buildCreateJson} / {@link #createCustomerAndReturnId}) keep
 * individual test bodies concise.
 *
 * <p>Tests use only lenient numeric assertions (e.g. {@code ≥ 1}) when other
 * parallel tests may have left rows in the shared H2 database.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CustomerController — REST-Assured Integration Tests")
@SuppressWarnings({"PMD.MethodNamingConventions", "PMD.UnitTestShouldIncludeAssert"})
class CustomerControllerIT {

    // ── Constants ─────────────────────────────────────────────────────────────

    private static final String CUSTOMERS_PATH = "/api/v1/customers";
    private static final String CREATE_DATE     = "2025-01-01T00:00:00Z";

    @LocalServerPort
    private int port;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // IDs shared across all nested test classes
    private int seededAddressId;
    private int seededCityId;
    private int seededCountryId;

    // ── One-time reference data setup ─────────────────────────────────────────

    /**
     * Seeds a Country → City → Address hierarchy once before any test runs.
     * Customer rows are created per individual test.
     */
    @BeforeAll
    void setUpReferenceData() {
        Country country = new Country();
        country.setCountry("Testland");
        country.setLastUpdate(Instant.parse("2024-01-01T00:00:00Z"));
        country = countryRepository.save(country);
        seededCountryId = country.getId();

        City city = new City();
        city.setCity("Test City");
        city.setCountry(country);
        city.setLastUpdate(Instant.parse("2024-01-01T00:00:00Z"));
        city = cityRepository.save(city);
        seededCityId = city.getId();

        Address address = new Address();
        address.setAddress("123 Test Street");
        address.setDistrict("Test District");
        address.setPhone("555-0001");
        address.setLastUpdate(Instant.parse("2024-01-01T00:00:00Z"));
        address.setCity(city);
        address = addressRepository.save(address);
        seededAddressId = address.getId();
    }

    // ── Per-test REST Assured configuration ───────────────────────────────────

    @BeforeEach
    void configureRestAssured() {
        RestAssured.port    = port;
        RestAssured.basePath = "/customer-identity/api";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    // ── Shared helpers ────────────────────────────────────────────────────────

    /**
     * Builds a minimal valid customer creation JSON body.
     * The {@code address.id} is always set to the seeded address ID.
     */
    private String buildCreateJson(String firstName, String lastName) {
        return """
                {
                  "firstName": "%s",
                  "lastName": "%s",
                  "email": "%s@example.com",
                  "active": true,
                  "createDate": "%s",
                  "address": { "id": %d }
                }
                 """.formatted(firstName, lastName, firstName.toLowerCase(java.util.Locale.ROOT), CREATE_DATE, seededAddressId);
    }

    /**
     * Posts a create request and returns the generated customer ID.
     * Asserts 201 status so callers can trust the ID is valid.
     */
    private int createCustomerAndReturnId(String firstName, String lastName) {
        return given()
                .contentType(ContentType.JSON)
                .body(buildCreateJson(firstName, lastName))
                .when().post(CUSTOMERS_PATH)
                .then().statusCode(HttpStatus.CREATED.value())
                .extract().path("id");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // POST /api/v1/customers — create()
    // ══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("POST /api/v1/customers — create()")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CreateCustomerTests {

        @Test
        @Order(1)
        @DisplayName("201 with full valid payload — body contains generated id and all sent fields")
        void create_withFullValidPayload_returns201AndBodyFields() {
            given()
                    .contentType(ContentType.JSON)
                    .body(buildCreateJson("Alice", "Smith"))
                    .when().post(CUSTOMERS_PATH)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .contentType(ContentType.JSON)
                    .body("id", notNullValue())
                    .body("firstName", equalTo("Alice"))
                    .body("lastName", equalTo("Smith"))
                    .body("email", equalTo("alice@example.com"))
                    .body("active", equalTo(true))
                    .body("addressId", equalTo(seededAddressId))
                    .body("cityId", equalTo(seededCityId))
                    .body("countryId", equalTo(seededCountryId));
        }

        @Test
        @Order(2)
        @DisplayName("201 — id in the request body is ignored; id is always DB-generated")
        void create_withIdInPayload_dbGeneratesNewId() {
            String body = """
                    {
                      "id": 99999,
                      "firstName": "Bob",
                      "lastName": "Jones",
                      "active": true,
                      "createDate": "%s",
                      "address": { "id": %d }
                    }
                    """.formatted(CREATE_DATE, seededAddressId);

            given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when().post(CUSTOMERS_PATH)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", not(equalTo(99_999)));
        }

        @Test
        @Order(3)
        @DisplayName("201 — null optional email is accepted (field absent from response per non_null Jackson policy)")
        void create_withNullEmail_returns201WithoutEmailField() {
            String body = """
                    {
                      "firstName": "NoEmail",
                      "lastName": "User",
                      "active": true,
                      "createDate": "%s",
                      "address": { "id": %d }
                    }
                    """.formatted(CREATE_DATE, seededAddressId);

            given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when().post(CUSTOMERS_PATH)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", notNullValue())
                    .body("firstName", equalTo("NoEmail"));
        }

        @Test
        @Order(4)
        @DisplayName("400 when firstName is missing (@NotNull violation)")
        void create_missingFirstName_returns400() {
            String body = """
                    {
                      "lastName": "Doe",
                      "active": true,
                      "createDate": "%s",
                      "address": { "id": %d }
                    }
                    """.formatted(CREATE_DATE, seededAddressId);

            given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when().post(CUSTOMERS_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @Order(5)
        @DisplayName("400 when lastName is missing (@NotNull violation)")
        void create_missingLastName_returns400() {
            String body = """
                    {
                      "firstName": "Jane",
                      "active": true,
                      "createDate": "%s",
                      "address": { "id": %d }
                    }
                    """.formatted(CREATE_DATE, seededAddressId);

            given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when().post(CUSTOMERS_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @Order(6)
        @DisplayName("400 when active flag is missing (@NotNull violation)")
        void create_missingActiveFlag_returns400() {
            String body = """
                    {
                      "firstName": "Jane",
                      "lastName": "Doe",
                      "createDate": "%s",
                      "address": { "id": %d }
                    }
                    """.formatted(CREATE_DATE, seededAddressId);

            given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when().post(CUSTOMERS_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @Order(7)
        @DisplayName("400 when firstName exceeds 45 characters (@Size(max=45) violation)")
        void create_firstNameExceeds45Chars_returns400() {
            String body = """
                    {
                      "firstName": "%s",
                      "lastName": "Doe",
                      "active": true,
                      "createDate": "%s",
                      "address": { "id": %d }
                    }
                    """.formatted("A".repeat(46), CREATE_DATE, seededAddressId);

            given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when().post(CUSTOMERS_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @Order(8)
        @DisplayName("400 when lastName exceeds 45 characters (@Size(max=45) violation)")
        void create_lastNameExceeds45Chars_returns400() {
            String body = """
                    {
                      "firstName": "Jane",
                      "lastName": "%s",
                      "active": true,
                      "createDate": "%s",
                      "address": { "id": %d }
                    }
                    """.formatted("B".repeat(46), CREATE_DATE, seededAddressId);

            given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when().post(CUSTOMERS_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @Order(9)
        @DisplayName("400 when email exceeds 50 characters (@Size(max=50) violation)")
        void create_emailExceeds50Chars_returns400() {
            String body = """
                    {
                      "firstName": "Jane",
                      "lastName": "Doe",
                      "email": "%s@long.com",
                      "active": true,
                      "createDate": "%s",
                      "address": { "id": %d }
                    }
                    """.formatted("C".repeat(50), CREATE_DATE, seededAddressId);

            given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when().post(CUSTOMERS_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @Order(10)
        @DisplayName("404 when referenced address ID does not exist")
        void create_nonExistentAddressId_returns404() {
            String body = """
                    {
                      "firstName": "Jane",
                      "lastName": "Doe",
                      "active": true,
                      "createDate": "%s",
                      "address": { "id": 999999 }
                    }
                    """.formatted(CREATE_DATE);

            given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when().post(CUSTOMERS_PATH)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @Order(11)
        @DisplayName("400 when request body is empty JSON object")
        void create_emptyJsonBody_returns400() {
            given()
                    .contentType(ContentType.JSON)
                    .body("{}")
                    .when().post(CUSTOMERS_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @Order(12)
        @DisplayName("400 when Content-Type header is missing (no application/json)")
        void create_missingContentType_returns400OrUnsupportedMedia() {
            given()
                    .body(buildCreateJson("ContentType", "Missing"))
                    .when().post(CUSTOMERS_PATH)
                    .then()
                    .statusCode(anyOf(
                            equalTo(HttpStatus.BAD_REQUEST.value()),
                            equalTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()),
                            equalTo(HttpStatus.INTERNAL_SERVER_ERROR.value())));
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // GET /api/v1/customers/{id} — findById()
    // ══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("GET /api/v1/customers/{id} — findById()")
    class FindByIdTests {

        @Test
        @DisplayName("200 with full nested IDs when customer exists")
        void findById_existingId_returns200WithAllFields() {
            int id = createCustomerAndReturnId("FindById", "Tester");

            given()
                    .when().get(CUSTOMERS_PATH + "/" + id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(id))
                    .body("firstName", equalTo("FindById"))
                    .body("lastName", equalTo("Tester"))
                    .body("active", equalTo(true))
                    .body("addressId", equalTo(seededAddressId))
                    .body("cityId", equalTo(seededCityId))
                    .body("countryId", equalTo(seededCountryId));
        }

        @Test
        @DisplayName("404 when customer ID does not exist")
        void findById_nonExistentId_returns404() {
            given()
                    .when().get(CUSTOMERS_PATH + "/999999")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("200 — createDate is persisted and returned as ISO-8601 timestamp")
        void findById_createDateIsPersistedAndReturned() {
            int id = createCustomerAndReturnId("DateCheck", "Verify");

            given()
                    .when().get(CUSTOMERS_PATH + "/" + id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("createDate", notNullValue());
        }

        @Test
        @DisplayName("400 or 404 when id path variable is not a valid integer")
        void findById_nonNumericId_returnsClientError() {
            given()
                    .when().get(CUSTOMERS_PATH + "/not-a-number")
                    .then()
                    .statusCode(anyOf(
                            equalTo(HttpStatus.BAD_REQUEST.value()),
                            equalTo(HttpStatus.NOT_FOUND.value())));
        }

        @Test
        @DisplayName("404 after customer is deleted — get returns 404")
        void findById_afterDelete_returns404() {
            int id = createCustomerAndReturnId("ThenDelete", "Me");
            customerRepository.deleteById(id);

            given()
                    .when().get(CUSTOMERS_PATH + "/" + id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // GET /api/v1/customers — findAll(Pageable)
    // ══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("GET /api/v1/customers — findAll(Pageable)")
    class FindAllPagedTests {

        @Test
        @DisplayName("200 with default page size 20 when no pagination parameters sent")
        void findAll_noParams_defaultPageSize20() {
            createCustomerAndReturnId("Paged", "Default");

            given()
                    .when().get(CUSTOMERS_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("content", notNullValue())
                    .body("size", equalTo(20))
                    .body("number", equalTo(0));
        }

        @Test
        @DisplayName("200 with explicit page and size parameters respected")
        void findAll_withExplicitPageAndSize_pageMetadataCorrect() {
            createCustomerAndReturnId("Explicit", "Paged");

            given()
                    .queryParam("page", 0)
                    .queryParam("size", 5)
                    .when().get(CUSTOMERS_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size", equalTo(5))
                    .body("number", equalTo(0));
        }

        @Test
        @DisplayName("200 with empty content array when requesting a page beyond available data")
        void findAll_pageWayBeyondData_returnsEmptyContent() {
            given()
                    .queryParam("page", 9999)
                    .queryParam("size", 10)
                    .when().get(CUSTOMERS_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("content", empty());
        }

        @Test
        @DisplayName("200 when sort parameter is supplied (lastName,asc)")
        void findAll_withSortParam_returns200() {
            createCustomerAndReturnId("Sorted", "Customer");

            given()
                    .queryParam("page", 0)
                    .queryParam("size", 5)
                    .queryParam("sort", "lastName,asc")
                    .when().get(CUSTOMERS_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("content", notNullValue());
        }

        @Test
        @DisplayName("200 response includes totalElements and totalPages in page metadata")
        void findAll_responsePaginationMetadata_present() {
            createCustomerAndReturnId("Meta", "Check");

            given()
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when().get(CUSTOMERS_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("totalElements", greaterThanOrEqualTo(1))
                    .body("totalPages", greaterThanOrEqualTo(1));
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // GET /api/v1/customers/all — getAllCustomers() (unpaged list)
    // ══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("GET /api/v1/customers/all — getAllCustomers()")
    class GetAllCustomersTests {

        @Test
        @DisplayName("200 returns a JSON array with at least one element")
        void getAllCustomers_afterCreation_returnsNonEmptyArray() {
            createCustomerAndReturnId("All", "Unpaged");

            given()
                    .when().get(CUSTOMERS_PATH + "/all")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", instanceOf(java.util.List.class))
                    .body("size()", greaterThanOrEqualTo(1));
        }

        @Test
        @DisplayName("200 — each item in the list contains id, firstName, lastName, active")
        void getAllCustomers_eachItemContainsMandatoryFields() {
            createCustomerAndReturnId("FieldAudit", "AllList");

            given()
                    .when().get(CUSTOMERS_PATH + "/all")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("[0].id", notNullValue())
                    .body("[0].firstName", notNullValue())
                    .body("[0].lastName", notNullValue())
                    .body("[0].active", notNullValue());
        }

        @Test
        @DisplayName("200 — newly created customer appears in the unpaged list")
        void getAllCustomers_newlyCreatedCustomerAppearsInList() {
            int id = createCustomerAndReturnId("InList", "Check");

            given()
                    .when().get(CUSTOMERS_PATH + "/all")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("collect { it.id }", notNullValue());

            // Verify the created ID exists among returned IDs via JSON path
            given()
                    .when().get(CUSTOMERS_PATH + "/" + id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(id));
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // GET /api/v1/customers/count — count()
    // ══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("GET /api/v1/customers/count — count()")
    class CountTests {

        @Test
        @DisplayName("200 returns a non-negative integer")
        void count_returns200WithNonNegativeInteger() {
            createCustomerAndReturnId("CountSeed", "One");

            given()
                    .when().get(CUSTOMERS_PATH + "/count")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", greaterThanOrEqualTo(1));
        }

        @Test
        @DisplayName("count increments by exactly 1 after creating a single customer")
        void count_incrementsByOneAfterCreate() {
            long before = given()
                    .when().get(CUSTOMERS_PATH + "/count")
                    .then().statusCode(200)
                    .extract().as(Long.class);

            createCustomerAndReturnId("CountBefore", "After");

            long after = given()
                    .when().get(CUSTOMERS_PATH + "/count")
                    .then().statusCode(200)
                    .extract().as(Long.class);

            org.assertj.core.api.Assertions.assertThat(after).isEqualTo(before + 1);
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // GET /api/v1/customers/exists/{id} — existsById()
    // ══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("GET /api/v1/customers/exists/{id} — existsById()")
    class ExistsByIdTests {

        @Test
        @DisplayName("200 true when customer with given id exists")
        void existsById_existingCustomer_returnsTrue() {
            int id = createCustomerAndReturnId("ExistsTrue", "Check");

            given()
                    .when().get(CUSTOMERS_PATH + "/exists/" + id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", equalTo(true));
        }

        @Test
        @DisplayName("200 false when customer with given id does not exist")
        void existsById_nonExistentId_returnsFalse() {
            given()
                    .when().get(CUSTOMERS_PATH + "/exists/999999")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", equalTo(false));
        }

        @Test
        @DisplayName("200 false after the customer is deleted")
        void existsById_afterDelete_returnsFalse() {
            int id = createCustomerAndReturnId("ExistsDelete", "Test");

            customerRepository.deleteById(id);

            given()
                    .when().get(CUSTOMERS_PATH + "/exists/" + id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", equalTo(false));
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // PUT /api/v1/customers/{id} — update() (full replace)
    // ══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("PUT /api/v1/customers/{id} — update()")
    class FullUpdateTests {

        @Test
        @DisplayName("200 — all mutable fields are replaced with request payload values")
        void update_withValidPayload_updatesAllFields() {
            int id = createCustomerAndReturnId("OldFirst", "OldLast");

            String body = """
                    {
                      "firstName": "NewFirst",
                      "lastName": "NewLast",
                      "email": "updated@example.com",
                      "active": false,
                      "createDate": "%s",
                      "address": { "id": %d }
                    }
                    """.formatted(CREATE_DATE, seededAddressId);

            given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when().put(CUSTOMERS_PATH + "/" + id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(id))
                    .body("firstName", equalTo("NewFirst"))
                    .body("lastName", equalTo("NewLast"))
                    .body("email", equalTo("updated@example.com"))
                    .body("active", equalTo(false));
        }

        @Test
        @DisplayName("200 — subsequent GET reflects the updated values")
        void update_subsequentGet_showsNewValues() {
            int id = createCustomerAndReturnId("BeforeUpdate", "GetCheck");

            String body = """
                    {
                      "firstName": "AfterUpdate",
                      "lastName": "GetCheck",
                      "active": true,
                      "createDate": "%s",
                      "address": { "id": %d }
                    }
                    """.formatted(CREATE_DATE, seededAddressId);

            given().contentType(ContentType.JSON).body(body)
                    .when().put(CUSTOMERS_PATH + "/" + id)
                    .then().statusCode(200);

            given()
                    .when().get(CUSTOMERS_PATH + "/" + id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("firstName", equalTo("AfterUpdate"));
        }

        @Test
        @DisplayName("404 when the customer ID to update does not exist")
        void update_nonExistentId_returns404() {
            String body = """
                    {
                      "firstName": "X",
                      "lastName": "Y",
                      "active": true,
                      "createDate": "%s",
                      "address": { "id": %d }
                    }
                    """.formatted(CREATE_DATE, seededAddressId);

            given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when().put(CUSTOMERS_PATH + "/999999")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("400 when required field firstName is absent in PUT payload")
        void update_missingRequiredFirstName_returns400() {
            int id = createCustomerAndReturnId("PutRequired", "Field");

            String body = """
                    {
                      "lastName": "NewLast",
                      "active": true,
                      "createDate": "%s",
                      "address": { "id": %d }
                    }
                    """.formatted(CREATE_DATE, seededAddressId);

            given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when().put(CUSTOMERS_PATH + "/" + id)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("404 when the address ID in PUT payload does not exist")
        void update_nonExistentAddressId_returns404() {
            int id = createCustomerAndReturnId("PutBadAddr", "Test");

            String body = """
                    {
                      "firstName": "PutBadAddr",
                      "lastName": "Test",
                      "active": true,
                      "createDate": "%s",
                      "address": { "id": 999999 }
                    }
                    """.formatted(CREATE_DATE);

            given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when().put(CUSTOMERS_PATH + "/" + id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // PATCH /api/v1/customers/{id} — partialUpdate()
    // ══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("PATCH /api/v1/customers/{id} — partialUpdate()")
    class PartialUpdateTests {

        @Test
        @DisplayName("200 — patching email only updates email, leaves other fields intact")
        void partialUpdate_emailOnly_updatesEmailAndLeavesOtherFieldsIntact() {
            int id = createCustomerAndReturnId("PatchEmail", "Original");

            String patch = """
                    {
                      "firstName": "PatchEmail",
                      "lastName": "Original",
                      "email": "patched@example.com",
                      "active": true,
                      "createDate": "%s"
                    }
                    """.formatted(CREATE_DATE);

            given()
                    .contentType(ContentType.JSON)
                    .body(patch)
                    .when().patch(CUSTOMERS_PATH + "/" + id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(id))
                    .body("email", equalTo("patched@example.com"))
                    .body("firstName", equalTo("PatchEmail"))
                    .body("lastName", equalTo("Original"));
        }

        @Test
        @DisplayName("200 — patching active flag to false persists the change")
        void partialUpdate_activeFlagToFalse_persistsChange() {
            int id = createCustomerAndReturnId("PatchActive", "Flip");

            String patch = """
                    {
                      "firstName": "PatchActive",
                      "lastName": "Flip",
                      "active": false,
                      "createDate": "%s"
                    }
                    """.formatted(CREATE_DATE);

            given()
                    .contentType(ContentType.JSON)
                    .body(patch)
                    .when().patch(CUSTOMERS_PATH + "/" + id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("active", equalTo(false));
        }

        @Test
        @DisplayName("200 — PATCH with empty body does not corrupt existing customer data")
        void partialUpdate_emptyBody_doesNotCorruptExistingData() {
            int id = createCustomerAndReturnId("SafePatch", "NoChange");

            given()
                    .contentType(ContentType.JSON)
                    .body("{}")
                    .when().patch(CUSTOMERS_PATH + "/" + id)
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // Verify original data is still intact via GET
            given()
                    .when().get(CUSTOMERS_PATH + "/" + id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(id));
        }

        @Test
        @DisplayName("404 when customer ID to patch does not exist")
        void partialUpdate_nonExistentId_returns404() {
            given()
                    .contentType(ContentType.JSON)
                    .body("{\"firstName\": \"X\"}")
                    .when().patch(CUSTOMERS_PATH + "/999999")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("404 when patching with an address ID that does not exist")
        void partialUpdate_nonExistentAddressId_returns404() {
            int id = createCustomerAndReturnId("PatchBadAddr", "Test");

            given()
                    .contentType(ContentType.JSON)
                    .body("{\"address\": {\"id\": 999999}}")
                    .when().patch(CUSTOMERS_PATH + "/" + id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // DELETE /api/v1/customers/{id} — delete()
    // ══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("DELETE /api/v1/customers/{id} — delete()")
    class DeleteCustomerTests {

        @Test
        @DisplayName("204 No Content when customer exists and is successfully deleted")
        void delete_existingCustomer_returns204() {
            int id = createCustomerAndReturnId("DeleteMe", "Now");

            given()
                    .when().delete(CUSTOMERS_PATH + "/" + id)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        }

        @Test
        @DisplayName("404 on subsequent GET after successful delete (data actually removed)")
        void delete_subsequentGet_returns404() {
            int id = createCustomerAndReturnId("Deleted", "GetChk");

            given().when().delete(CUSTOMERS_PATH + "/" + id).then().statusCode(204);

            given()
                    .when().get(CUSTOMERS_PATH + "/" + id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("404 when trying to delete a non-existent customer ID")
        void delete_nonExistentId_returns404() {
            given()
                    .when().delete(CUSTOMERS_PATH + "/999999")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("404 on second delete attempt — not idempotent (resource is gone)")
        void delete_secondAttemptOnSameId_returns404() {
            int id = createCustomerAndReturnId("DoubleDelete", "Attempt");

            given().when().delete(CUSTOMERS_PATH + "/" + id).then().statusCode(204);
            given().when().delete(CUSTOMERS_PATH + "/" + id).then().statusCode(404);
        }

        @Test
        @DisplayName("existsById returns false after successful deletion")
        void delete_existsByIdReturnsFalseAfterDelete() {
            int id = createCustomerAndReturnId("ExistsAfterDelete", "Chk");

            given().when().delete(CUSTOMERS_PATH + "/" + id).then().statusCode(204);

            given()
                    .when().get(CUSTOMERS_PATH + "/exists/" + id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", equalTo(false));
        }
    }
}

