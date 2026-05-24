package com.me.learning.rental.mapper;

import com.me.learning.rental.dto.PaymentRequestDto;
import com.me.learning.rental.dto.PaymentResponseDto;
import com.me.learning.rental.dto.RentalRequestDto;
import com.me.learning.rental.entity.Payment;
import com.me.learning.rental.entity.Rental;
import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-24T07:45:55+0400",
    comments = "version: 1.6.2, compiler: javac, environment: Java 25.0.2 (Eclipse Adoptium)"
)
@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Autowired
    private RentalMapper rentalMapper;

    @Override
    public Payment toEntity(PaymentRequestDto paymentRequestDto) {
        if ( paymentRequestDto == null ) {
            return null;
        }

        Payment payment = new Payment();

        payment.setRental( rentalMapper.toEntity( paymentRequestDto.rental() ) );
        payment.setId( paymentRequestDto.id() );
        payment.setCustomerId( paymentRequestDto.customerId() );
        payment.setStaffId( paymentRequestDto.staffId() );
        payment.setAmount( paymentRequestDto.amount() );
        payment.setPaymentDate( paymentRequestDto.paymentDate() );
        payment.setLastUpdate( paymentRequestDto.lastUpdate() );

        return payment;
    }

    @Override
    public PaymentRequestDto toDto(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        RentalRequestDto rental = null;
        Integer id = null;
        Integer customerId = null;
        Short staffId = null;
        BigDecimal amount = null;
        Instant paymentDate = null;
        Instant lastUpdate = null;

        rental = rentalMapper.toDto( payment.getRental() );
        id = payment.getId();
        customerId = payment.getCustomerId();
        staffId = payment.getStaffId();
        amount = payment.getAmount();
        paymentDate = payment.getPaymentDate();
        lastUpdate = payment.getLastUpdate();

        PaymentRequestDto paymentRequestDto = new PaymentRequestDto( id, customerId, staffId, rental, amount, paymentDate, lastUpdate );

        return paymentRequestDto;
    }

    @Override
    public PaymentResponseDto toResponseDto(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        Integer rentalId = null;
        Integer id = null;
        Integer customerId = null;
        Short staffId = null;
        BigDecimal amount = null;
        Instant paymentDate = null;
        Instant lastUpdate = null;

        rentalId = paymentRentalId( payment );
        id = payment.getId();
        customerId = payment.getCustomerId();
        staffId = payment.getStaffId();
        amount = payment.getAmount();
        paymentDate = payment.getPaymentDate();
        lastUpdate = payment.getLastUpdate();

        PaymentResponseDto paymentResponseDto = new PaymentResponseDto( id, customerId, staffId, rentalId, amount, paymentDate, lastUpdate );

        return paymentResponseDto;
    }

    @Override
    public Payment partialUpdate(PaymentRequestDto paymentRequestDto, Payment payment) {
        if ( paymentRequestDto == null ) {
            return payment;
        }

        if ( paymentRequestDto.rental() != null ) {
            if ( payment.getRental() == null ) {
                payment.setRental( new Rental() );
            }
            rentalMapper.partialUpdate( paymentRequestDto.rental(), payment.getRental() );
        }
        if ( paymentRequestDto.id() != null ) {
            payment.setId( paymentRequestDto.id() );
        }
        if ( paymentRequestDto.customerId() != null ) {
            payment.setCustomerId( paymentRequestDto.customerId() );
        }
        if ( paymentRequestDto.staffId() != null ) {
            payment.setStaffId( paymentRequestDto.staffId() );
        }
        if ( paymentRequestDto.amount() != null ) {
            payment.setAmount( paymentRequestDto.amount() );
        }
        if ( paymentRequestDto.paymentDate() != null ) {
            payment.setPaymentDate( paymentRequestDto.paymentDate() );
        }
        if ( paymentRequestDto.lastUpdate() != null ) {
            payment.setLastUpdate( paymentRequestDto.lastUpdate() );
        }

        return payment;
    }

    private Integer paymentRentalId(Payment payment) {
        Rental rental = payment.getRental();
        if ( rental == null ) {
            return null;
        }
        return rental.getId();
    }
}
