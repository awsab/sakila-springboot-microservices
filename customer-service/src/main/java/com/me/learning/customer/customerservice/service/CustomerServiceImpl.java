package com.me.learning.customer.customerservice.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.me.learning.customerservice.dto.CustomerRequestDto;
import com.me.learning.customerservice.dto.CustomerResponseDto;
import com.me.learning.customerservice.entity.Address;
import com.me.learning.customerservice.entity.Customer;
import com.me.learning.customerservice.mapper.CustomerMapper;
import com.me.learning.customerservice.repository.AddressRepository;
import com.me.learning.customerservice.repository.CustomerRepository;
import com.me.learning.framework.web.errors.ResourceNotFoundException;

/**
 * Default implementation of {@link CustomerService}.
 *
 * <h3>Transaction strategy</h3>
 * <ul>
 *   <li>Class-level {@code @Transactional(readOnly = true)} covers all read paths.</li>
 *   <li>Write methods override with {@code @Transactional} (read-write).</li>
 * </ul>
 *
 * <h3>N+1 prevention</h3>
 * All repository calls that back a {@code toDto} conversion use the
 * {@code @EntityGraph}-annotated overrides in {@link CustomerRepository}, which
 * JOIN-fetch the full {@code address → city → country} chain in one query.
 * After a save, the entity is re-fetched via {@code findById} so that the
 * same EntityGraph is applied and the returned DTO is fully populated.
 *
 * <h3>Address association</h3>
 * {@link CustomerRequestDto#address()} carries the ID of an existing
 * {@link Address}.  On create/update the address is loaded from
 * {@link AddressRepository} (which itself has an EntityGraph loading
 * {@code city → country}), ensuring we set a JPA-managed entity reference
 * rather than a detached one created by MapStruct.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional (readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private static final String RESOURCE_CUSTOMER = "Customer";
    private static final String RESOURCE_ADDRESS = "Address";
    private static final String FIELD_ID = "id";

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final CustomerMapper customerMapper;

    // ── Write operations ─────────────────────────────────────────────────────

    @Override
    @Transactional
    public CustomerResponseDto create (CustomerRequestDto dto) {
        log.debug ("Creating new customer: {} {}", dto.firstName (), dto.lastName ());

        Customer customer = customerMapper.toEntity (dto);
        customer.setId (null); // ID is always DB-generated; ignore any value in the DTO

        if (dto.address () != null && dto.address ().id () != null) {
            Address address = addressRepository.findById (dto.address ().id ())
                    .orElseThrow (() -> new ResourceNotFoundException (
                            RESOURCE_ADDRESS, FIELD_ID, dto.address ().id ()));
            customer.setAddress (address);
        }

        Customer saved = customerRepository.save (customer);
        log.info ("Created customer with ID: {}", saved.getId ());

        // Re-fetch via EntityGraph so the returned DTO has the full address hierarchy.
        return customerMapper.toResponseDto (customerRepository.findById (saved.getId ()).orElseThrow ());
    }

    @Override
    @Transactional
    public CustomerResponseDto update (Integer id, CustomerRequestDto dto) {
        log.debug ("Fully updating customer with ID: {}", id);

        Customer existing = customerRepository.findById (id)
                .orElseThrow (() -> new ResourceNotFoundException (RESOURCE_CUSTOMER, FIELD_ID, id));

        existing.setFirstName (dto.firstName ());
        existing.setLastName (dto.lastName ());
        existing.setEmail (dto.email ());
        existing.setActive (dto.active ());
        existing.setCreateDate (dto.createDate ());
        existing.setLastUpdate (dto.lastUpdate ());

        if (dto.address () != null && dto.address ().id () != null) {
            Integer newAddressId = dto.address ().id ();
            if (!newAddressId.equals (existing.getAddress ().getId ())) {
                Address address = addressRepository.findById (newAddressId)
                        .orElseThrow (() -> new ResourceNotFoundException (
                                RESOURCE_ADDRESS, FIELD_ID, newAddressId));
                existing.setAddress (address);
            }
        }

        customerRepository.save (existing);
        log.info ("Updated customer with ID: {}", id);

        return customerMapper.toResponseDto (customerRepository.findById (id).orElseThrow ());
    }

    @Override
    @Transactional
    public CustomerResponseDto partialUpdate (Integer id, CustomerRequestDto dto) {
        log.debug ("Partially updating customer with ID: {}", id);

        Customer existing = customerRepository.findById (id)
                .orElseThrow (() -> new ResourceNotFoundException (RESOURCE_CUSTOMER, FIELD_ID, id));

        // Apply only non-null scalar fields via the MapStruct partial-update method.
        customerMapper.partialUpdate (dto, existing);

        // MapStruct may have set a detached Address on `existing` if address was
        // non-null.  Replace it with a managed entity loaded from the repository.
        if (dto.address () != null && dto.address ().id () != null) {
            Address address = addressRepository.findById (dto.address ().id ())
                    .orElseThrow (() -> new ResourceNotFoundException (
                            RESOURCE_ADDRESS, FIELD_ID, dto.address ().id ()));
            existing.setAddress (address);
        }

        customerRepository.save (existing);
        log.info ("Patched customer with ID: {}", id);

        return customerMapper.toResponseDto (customerRepository.findById (id).orElseThrow ());
    }

    @Override
    @Transactional
    public void delete (Integer id) {
        log.debug ("Deleting customer with ID: {}", id);

        if (!customerRepository.existsById (id)) {
            throw new ResourceNotFoundException (RESOURCE_CUSTOMER, FIELD_ID, id);
        }

        customerRepository.deleteById (id);
        log.info ("Deleted customer with ID: {}", id);
    }

    // ── Read operations ───────────────────────────────────────────────────────

    @Override
    public CustomerResponseDto findById (Integer id) {
        log.debug ("Fetching customer with ID: {}", id);

        return customerRepository.findById (id)
                .map (customerMapper::toResponseDto)
                .orElseThrow (() -> new ResourceNotFoundException (RESOURCE_CUSTOMER, FIELD_ID, id));
    }

    @Override
    public List<CustomerResponseDto> findAll () {
        log.debug ("Fetching all customers");
        return customerRepository.findAll ()
                .stream ()
                .map (customerMapper::toResponseDto)
                .toList ();
    }

    @Override
    public Page<CustomerResponseDto> findAll (Pageable pageable) {
        log.debug ("Fetching customers — page {}, size {}",
                pageable.getPageNumber (), pageable.getPageSize ());
        return customerRepository.findAll (pageable).map (customerMapper::toResponseDto);
    }

    @Override
    public boolean existsById (Integer id) {
        return customerRepository.existsById (id);
    }

    @Override
    public long count () {
        return customerRepository.count ();
    }
}

