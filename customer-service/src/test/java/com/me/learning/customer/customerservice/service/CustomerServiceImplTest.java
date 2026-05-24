package com.me.learning.customer.customerservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.me.learning.customerservice.dto.AddressRequestDto;
import com.me.learning.customerservice.dto.CustomerRequestDto;
import com.me.learning.customerservice.dto.CustomerResponseDto;
import com.me.learning.customerservice.entity.Address;
import com.me.learning.customerservice.entity.Customer;
import com.me.learning.customerservice.mapper.CustomerMapper;
import com.me.learning.customerservice.repository.AddressRepository;
import com.me.learning.customerservice.repository.CustomerRepository;
import com.me.learning.framework.web.errors.ResourceNotFoundException;

/**
 * Unit tests for {@link CustomerServiceImpl}.
 *
 * <p>All collaborators ({@link CustomerRepository}, {@link AddressRepository},
 * {@link CustomerMapper}) are mocked with Mockito so tests run without a
 * Spring context, database or network.
 *
 * <p>Assertions use AssertJ for a fluent, readable style.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerServiceImpl")
@SuppressWarnings("PMD.MethodNamingConventions")
class CustomerServiceImplTest {

    /* ── Constants ─────────────────────────────────────────────────────── */

    private static final Instant NOW = Instant.parse("2024-06-01T12:00:00Z");
    private static final int    ADDRESS_ID  = 1;
    private static final int    CITY_ID     = 10;
    private static final int    COUNTRY_ID  = 100;

    /* ── Mocks ──────────────────────────────────────────────────────────── */

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    /* ── Test-data builders ─────────────────────────────────────────────── */

    private Address buildAddress() {
        Address address = new Address();
        address.setId(ADDRESS_ID);
        return address;
    }

    private Customer buildCustomer(Integer id) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setFirstName("Jane");
        customer.setLastName("Doe");
        customer.setEmail("jane@example.com");
        customer.setActive(true);
        customer.setCreateDate(NOW);
        customer.setAddress(buildAddress());
        return customer;
    }

    private CustomerRequestDto buildRequest() {
        AddressRequestDto addrDto = new AddressRequestDto(
                ADDRESS_ID, "123 Street", null, "District", null, "555-1234", NOW, null);
        return new CustomerRequestDto(
                null, "Jane", "Doe", "jane@example.com", true, NOW, null, addrDto);
    }

    private CustomerResponseDto buildResponse(Integer id) {
        return new CustomerResponseDto(
                id, "Jane", "Doe", "jane@example.com", true, NOW, null,
                ADDRESS_ID, CITY_ID, COUNTRY_ID);
    }

    /* ══════════════════════════════════════════════════════════════════════
       create()
    ══════════════════════════════════════════════════════════════════════ */

    @Nested
    @DisplayName("create()")
    class CreateTests {

        @Test
        @DisplayName("should persist customer and return populated response DTO")
        void create_withValidRequest_returnsPopulatedResponseDto() {
            CustomerRequestDto request  = buildRequest();
            Customer entity             = buildCustomer(null);
            Customer saved              = buildCustomer(42);
            CustomerResponseDto expected = buildResponse(42);

            when(customerMapper.toEntity(request)).thenReturn(entity);
            when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.of(buildAddress()));
            when(customerRepository.save(any(Customer.class))).thenReturn(saved);
            when(customerRepository.findById(42)).thenReturn(Optional.of(saved));
            when(customerMapper.toResponseDto(saved)).thenReturn(expected);

            CustomerResponseDto result = customerService.create(request);

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(42);
            assertThat(result.firstName()).isEqualTo("Jane");
            assertThat(result.lastName()).isEqualTo("Doe");
            assertThat(result.email()).isEqualTo("jane@example.com");
            assertThat(result.active()).isTrue();
            assertThat(result.addressId()).isEqualTo(ADDRESS_ID);
            verify(customerRepository).save(any(Customer.class));
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when address ID does not exist")
        void create_whenAddressNotFound_throwsResourceNotFoundException() {
            CustomerRequestDto request = buildRequest();
            Customer entity            = buildCustomer(null);

            when(customerMapper.toEntity(request)).thenReturn(entity);
            when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> customerService.create(request))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(customerRepository, never()).save(any());
        }

        @Test
        @DisplayName("should skip address lookup and still save when address field is null")
        void create_withNullAddress_doesNotLookUpAddress() {
            CustomerRequestDto request = new CustomerRequestDto(
                    null, "Jane", "Doe", null, true, NOW, null, null);
            Customer entity = new Customer();
            entity.setFirstName("Jane");
            Customer saved  = buildCustomer(1);
            CustomerResponseDto expected = buildResponse(1);

            when(customerMapper.toEntity(request)).thenReturn(entity);
            when(customerRepository.save(any(Customer.class))).thenReturn(saved);
            when(customerRepository.findById(1)).thenReturn(Optional.of(saved));
            when(customerMapper.toResponseDto(saved)).thenReturn(expected);

            CustomerResponseDto result = customerService.create(request);

            assertThat(result).isNotNull();
            verify(addressRepository, never()).findById(any());
        }
    }

    /* ══════════════════════════════════════════════════════════════════════
       update()
    ══════════════════════════════════════════════════════════════════════ */

    @Nested
    @DisplayName("update()")
    class UpdateTests {

        @Test
        @DisplayName("should overwrite all fields and return updated response DTO")
        void update_withValidId_returnsUpdatedResponseDto() {
            CustomerRequestDto request   = buildRequest();
            Customer existing            = buildCustomer(1);
            CustomerResponseDto expected = buildResponse(1);

            // findById called twice: once to load, once to re-fetch after save
            when(customerRepository.findById(1)).thenReturn(Optional.of(existing));
            when(customerRepository.save(any(Customer.class))).thenReturn(existing);
            when(customerMapper.toResponseDto(existing)).thenReturn(expected);

            CustomerResponseDto result = customerService.update(1, request);

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(1);
            verify(customerRepository).save(existing);
        }

        @Test
        @DisplayName("should load new address when address ID changes during update")
        void update_whenAddressIdChanges_loadsNewAddress() {
            int newAddressId = 99;
            AddressRequestDto newAddrDto = new AddressRequestDto(
                    newAddressId, "New St", null, "NewDist", null, "999-0000", NOW, null);
            CustomerRequestDto request = new CustomerRequestDto(
                    null, "Jane", "Doe", "jane@example.com", true, NOW, null, newAddrDto);

            Customer existing = buildCustomer(1); // existing address.id = 1 (different)
            Address newAddress = new Address();
            newAddress.setId(newAddressId);
            CustomerResponseDto expected = buildResponse(1);

            when(customerRepository.findById(1)).thenReturn(Optional.of(existing));
            when(addressRepository.findById(newAddressId)).thenReturn(Optional.of(newAddress));
            when(customerRepository.save(any(Customer.class))).thenReturn(existing);
            when(customerMapper.toResponseDto(existing)).thenReturn(expected);

            customerService.update(1, request);

            verify(addressRepository).findById(newAddressId);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when customer ID does not exist")
        void update_whenCustomerNotFound_throwsResourceNotFoundException() {
            when(customerRepository.findById(999)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> customerService.update(999, buildRequest()))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    /* ══════════════════════════════════════════════════════════════════════
       partialUpdate()
    ══════════════════════════════════════════════════════════════════════ */

    @Nested
    @DisplayName("partialUpdate()")
    class PartialUpdateTests {

        @Test
        @DisplayName("should delegate to mapper.partialUpdate and persist result")
        void partialUpdate_withValidId_appliesPatchAndSaves() {
            CustomerRequestDto request   = buildRequest();
            Customer existing            = buildCustomer(1);
            CustomerResponseDto expected = buildResponse(1);

            when(customerRepository.findById(1)).thenReturn(Optional.of(existing));
            when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.of(buildAddress()));
            when(customerRepository.save(any(Customer.class))).thenReturn(existing);
            when(customerMapper.toResponseDto(existing)).thenReturn(expected);

            CustomerResponseDto result = customerService.partialUpdate(1, request);

            assertThat(result).isNotNull();
            verify(customerMapper).partialUpdate(request, existing);
            verify(customerRepository).save(existing);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when customer ID does not exist")
        void partialUpdate_whenCustomerNotFound_throwsResourceNotFoundException() {
            when(customerRepository.findById(999)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> customerService.partialUpdate(999, buildRequest()))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    /* ══════════════════════════════════════════════════════════════════════
       findById()
    ══════════════════════════════════════════════════════════════════════ */

    @Nested
    @DisplayName("findById()")
    class FindByIdTests {

        @Test
        @DisplayName("should return response DTO when customer exists")
        void findById_whenCustomerExists_returnsResponseDto() {
            Customer customer            = buildCustomer(1);
            CustomerResponseDto expected = buildResponse(1);

            when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
            when(customerMapper.toResponseDto(customer)).thenReturn(expected);

            CustomerResponseDto result = customerService.findById(1);

            assertThat(result).isNotNull().isEqualTo(expected);
            assertThat(result.id()).isEqualTo(1);
            assertThat(result.email()).isEqualTo("jane@example.com");
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when customer does not exist")
        void findById_whenCustomerNotFound_throwsResourceNotFoundException() {
            when(customerRepository.findById(999)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> customerService.findById(999))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    /* ══════════════════════════════════════════════════════════════════════
       findAll() — unpaged
    ══════════════════════════════════════════════════════════════════════ */

    @Nested
    @DisplayName("findAll() — unpaged")
    class FindAllUnpagedTests {

        @Test
        @DisplayName("should return mapped list of all customers")
        void findAll_withMultipleCustomers_returnsMappedList() {
            Customer c1 = buildCustomer(1);
            Customer c2 = buildCustomer(2);
            CustomerResponseDto dto1 = buildResponse(1);
            CustomerResponseDto dto2 = buildResponse(2);

            when(customerRepository.findAll()).thenReturn(List.of(c1, c2));
            when(customerMapper.toResponseDto(c1)).thenReturn(dto1);
            when(customerMapper.toResponseDto(c2)).thenReturn(dto2);

            List<CustomerResponseDto> result = customerService.findAll();

            assertThat(result).hasSize(2);
            assertThat(result).extracting(CustomerResponseDto::id).containsExactly(1, 2);
        }

        @Test
        @DisplayName("should return empty list when repository has no customers")
        void findAll_whenRepositoryEmpty_returnsEmptyList() {
            when(customerRepository.findAll()).thenReturn(List.of());

            assertThat(customerService.findAll()).isEmpty();
        }
    }

    /* ══════════════════════════════════════════════════════════════════════
       findAll(Pageable)
    ══════════════════════════════════════════════════════════════════════ */

    @Nested
    @DisplayName("findAll(Pageable)")
    class FindAllPagedTests {

        @Test
        @DisplayName("should return page of mapped customers")
        void findAllPaged_returnsPageWithMappedContent() {
            PageRequest pageable = PageRequest.of(0, 10);
            Customer customer            = buildCustomer(1);
            CustomerResponseDto expected = buildResponse(1);
            Page<Customer> page          = new PageImpl<>(List.of(customer), pageable, 1);

            when(customerRepository.findAll(pageable)).thenReturn(page);
            when(customerMapper.toResponseDto(customer)).thenReturn(expected);

            Page<CustomerResponseDto> result = customerService.findAll(pageable);

            assertThat(result.getTotalElements()).isEqualTo(1L);
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).id()).isEqualTo(1);
        }

        @Test
        @DisplayName("should return empty page when repository has no customers")
        void findAllPaged_whenEmpty_returnsEmptyPage() {
            PageRequest pageable    = PageRequest.of(0, 10);
            Page<Customer> emptyPage = new PageImpl<>(List.of(), pageable, 0);

            when(customerRepository.findAll(pageable)).thenReturn(emptyPage);

            Page<CustomerResponseDto> result = customerService.findAll(pageable);

            assertThat(result.getTotalElements()).isZero();
            assertThat(result.getContent()).isEmpty();
        }
    }

    /* ══════════════════════════════════════════════════════════════════════
       delete()
    ══════════════════════════════════════════════════════════════════════ */

    @Nested
    @DisplayName("delete()")
    class DeleteTests {

        @Test
        @DisplayName("should invoke deleteById when customer exists")
        void delete_whenCustomerExists_callsDeleteById() {
            when(customerRepository.existsById(1)).thenReturn(true);

            customerService.delete(1);

            verify(customerRepository).deleteById(1);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException without calling deleteById when not found")
        void delete_whenCustomerNotFound_throwsResourceNotFoundException() {
            when(customerRepository.existsById(999)).thenReturn(false);

            assertThatThrownBy(() -> customerService.delete(999))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(customerRepository, never()).deleteById(any());
        }
    }

    /* ══════════════════════════════════════════════════════════════════════
       existsById()
    ══════════════════════════════════════════════════════════════════════ */

    @Nested
    @DisplayName("existsById()")
    class ExistsByIdTests {

        @Test
        @DisplayName("should return true when customer exists in repository")
        void existsById_whenCustomerExists_returnsTrue() {
            when(customerRepository.existsById(1)).thenReturn(true);

            assertThat(customerService.existsById(1)).isTrue();
        }

        @Test
        @DisplayName("should return false when customer does not exist in repository")
        void existsById_whenCustomerAbsent_returnsFalse() {
            when(customerRepository.existsById(999)).thenReturn(false);

            assertThat(customerService.existsById(999)).isFalse();
        }
    }

    /* ══════════════════════════════════════════════════════════════════════
       count()
    ══════════════════════════════════════════════════════════════════════ */

    @Nested
    @DisplayName("count()")
    class CountTests {

        @Test
        @DisplayName("should return total number of customers from repository")
        void count_delegatesToRepository() {
            when(customerRepository.count()).thenReturn(7L);

            assertThat(customerService.count()).isEqualTo(7L);
        }

        @Test
        @DisplayName("should return zero when repository is empty")
        void count_whenEmpty_returnsZero() {
            when(customerRepository.count()).thenReturn(0L);

            assertThat(customerService.count()).isZero();
        }
    }
}

