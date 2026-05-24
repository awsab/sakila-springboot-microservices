package com.me.learning.customerservice.controller;

import java.util.List;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.me.learning.customerservice.dto.CustomerRequestDto;
import com.me.learning.customerservice.dto.CustomerResponseDto;
import com.me.learning.customerservice.service.CustomerService;

/**
 * REST controller for {@link com.me.learning.customerservice.entity.Customer} management.
 *
 * <p>Base path: {@code /api/v1/customers}
 *
 * <p>All endpoints delegate to {@link CustomerService} and follow the standard
 * response conventions:
 * <ul>
 *   <li>{@code POST}   → 201 Created with the created resource in the body</li>
 *   <li>{@code PUT}    → 200 OK with the fully updated resource</li>
 *   <li>{@code PATCH}  → 200 OK with the partially updated resource</li>
 *   <li>{@code GET}    → 200 OK with requested resource(s)</li>
 *   <li>{@code DELETE} → 204 No Content</li>
 * </ul>
 *
 * <p>Error responses are handled globally by
 * {@code com.me.learning.framework.web.errors.GlobalExceptionHandler}.
 */
@Slf4j
@RestController
@RequestMapping ("/api/v1/customers")
@RequiredArgsConstructor
@Tag (name = "Customer Identity", description = "Customer identity management APIs")
public class CustomerController {

    private final CustomerService customerService;

    // ── Create ────────────────────────────────────────────────────────────────

    @PostMapping
    @Operation (
            summary = "Create a new customer",
            description = "Creates a customer record. The `id` field in the request body is ignored; "
                    + "the ID is always assigned by the database. The `address.id` must "
                    + "reference an existing address."
    )
    @ApiResponse (
            responseCode = "201", description = "Customer created successfully",
            content = @Content (schema = @Schema (implementation = CustomerResponseDto.class)))
    @ApiResponse (
            responseCode = "400", description = "Validation error — check field constraints")
    @ApiResponse (
            responseCode = "404", description = "Referenced address does not exist")
    public ResponseEntity<CustomerResponseDto> create (
            @Valid @RequestBody CustomerRequestDto dto) {
        log.info ("REST POST /api/v1/customers — creating customer: {} {}", dto.firstName (), dto.lastName ());
        return ResponseEntity.status (HttpStatus.CREATED).body (customerService.create (dto));
    }

    // ── Full update ───────────────────────────────────────────────────────────

    @PutMapping ("/{id}")
    @Operation (
            summary = "Fully update a customer",
            description = "Replaces ALL editable fields of the customer identified by `id`. "
                    + "Any field not sent in the request body will be set to its supplied value "
                    + "(null-safe update is not applied — use PATCH for partial updates)."
    )
    @ApiResponse (
            responseCode = "200", description = "Customer updated successfully",
            content = @Content (schema = @Schema (implementation = CustomerResponseDto.class)))
    @ApiResponse (
            responseCode = "404", description = "Customer or referenced address not found")
    public ResponseEntity<CustomerResponseDto> update (
            @Parameter (description = "Customer primary key") @PathVariable Integer id,
            @Valid @RequestBody CustomerRequestDto dto) {
        log.info ("REST PUT /api/v1/customers/{} — full update", id);
        return ResponseEntity.ok (customerService.update (id, dto));
    }

    // ── Partial update ────────────────────────────────────────────────────────

    @PatchMapping ("/{id}")
    @Operation (
            summary = "Partially update a customer",
            description = "Updates only the non-null fields supplied in the request body. "
                    + "Fields omitted or explicitly set to `null` are left unchanged."
    )
    @ApiResponse (
            responseCode = "200", description = "Customer patched successfully",
            content = @Content (schema = @Schema (implementation = CustomerResponseDto.class)))
    @ApiResponse (
            responseCode = "404", description = "Customer or referenced address not found")
    public ResponseEntity<CustomerResponseDto> partialUpdate (
            @Parameter (description = "Customer primary key") @PathVariable Integer id,
            @RequestBody CustomerRequestDto dto) {
        log.info ("REST PATCH /api/v1/customers/{} — partial update", id);
        return ResponseEntity.ok (customerService.partialUpdate (id, dto));
    }

    // ── Read by ID ────────────────────────────────────────────────────────────

    @GetMapping ("/{id}")
    @Operation (
            summary = "Get a customer by ID",
            description = "Returns the customer along with its full nested address hierarchy "
                    + "(address → city → country). The association chain is eagerly loaded "
                    + "in a single JOIN query — no N+1."
    )
    @ApiResponse (
            responseCode = "200", description = "Customer found",
            content = @Content (schema = @Schema (implementation = CustomerResponseDto.class)))
    @ApiResponse (
            responseCode = "404", description = "Customer not found")
    public ResponseEntity<CustomerResponseDto> findById (
            @Parameter (description = "Customer primary key") @PathVariable Integer id) {
        log.info ("REST GET /api/v1/customers/{}", id);
        return ResponseEntity.ok (customerService.findById (id));
    }

    // ── List / Paginated ──────────────────────────────────────────────────────

    @GetMapping
    @Operation (
            summary = "List all customers (paginated)",
            description = "Returns a page of customers. Default page size is 20. "
                    + "Supports Spring Data pagination parameters: `page`, `size`, `sort`."
    )
    @ApiResponse (
            responseCode = "200", description = "Customers retrieved")
    public ResponseEntity<Page<CustomerResponseDto>> findAll (
            @PageableDefault (size = 20) Pageable pageable) {
        log.info ("REST GET /api/v1/customers — page {}, size {}",
                pageable.getPageNumber (), pageable.getPageSize ());
        return ResponseEntity.ok (customerService.findAll (pageable));
    }

    @GetMapping ("/all")
    @Operation (
            summary = "List all customers (unpaged)",
            description = "Returns the complete list of customers without pagination. "
                    + "Use with caution on large data sets; prefer the paginated endpoint."
    )
    @ApiResponse (
            responseCode = "200", description = "All customers retrieved")
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers () {
        log.info ("REST GET /api/v1/customers/all");
        return ResponseEntity.ok (customerService.findAll ());
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @DeleteMapping ("/{id}")
    @Operation (summary = "Delete a customer by ID")
    @ApiResponse (
            responseCode = "204", description = "Customer deleted successfully")
    @ApiResponse (
            responseCode = "404", description = "Customer not found")
    public ResponseEntity<Void> delete (
            @Parameter (description = "Customer primary key") @PathVariable Integer id) {
        log.info ("REST DELETE /api/v1/customers/{}", id);
        customerService.delete (id);
        return ResponseEntity.noContent ().build ();
    }

    // ── Utility ───────────────────────────────────────────────────────────────

    @GetMapping ("/count")
    @Operation (summary = "Count total number of customers")
    @ApiResponse (
            responseCode = "200", description = "Count returned")
    public ResponseEntity<Long> count () {
        return ResponseEntity.ok (customerService.count ());
    }

    @GetMapping ("/exists/{id}")
    @Operation (summary = "Check whether a customer exists by ID")
    @ApiResponse (
            responseCode = "200", description = "Existence flag returned")
    public ResponseEntity<Boolean> existsById (
            @Parameter (description = "Customer primary key") @PathVariable Integer id) {
        return ResponseEntity.ok (customerService.existsById (id));
    }
}

