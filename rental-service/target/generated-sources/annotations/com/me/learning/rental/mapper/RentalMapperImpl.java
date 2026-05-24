package com.me.learning.rental.mapper;

import com.me.learning.rental.dto.RentalRequestDto;
import com.me.learning.rental.dto.RentalResponseDto;
import com.me.learning.rental.entity.Rental;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-24T07:45:55+0400",
    comments = "version: 1.6.2, compiler: javac, environment: Java 25.0.2 (Eclipse Adoptium)"
)
@Component
public class RentalMapperImpl implements RentalMapper {

    @Override
    public Rental toEntity(RentalRequestDto rentalRequestDto) {
        if ( rentalRequestDto == null ) {
            return null;
        }

        Rental rental = new Rental();

        rental.setId( rentalRequestDto.id() );
        rental.setRentalDate( rentalRequestDto.rentalDate() );
        rental.setInventoryId( rentalRequestDto.inventoryId() );
        rental.setCustomerId( rentalRequestDto.customerId() );
        rental.setReturnDate( rentalRequestDto.returnDate() );
        rental.setStaffId( rentalRequestDto.staffId() );
        rental.setLastUpdate( rentalRequestDto.lastUpdate() );

        return rental;
    }

    @Override
    public RentalRequestDto toDto(Rental rental) {
        if ( rental == null ) {
            return null;
        }

        Integer id = null;
        Instant rentalDate = null;
        Integer inventoryId = null;
        Integer customerId = null;
        Instant returnDate = null;
        Short staffId = null;
        Instant lastUpdate = null;

        id = rental.getId();
        rentalDate = rental.getRentalDate();
        inventoryId = rental.getInventoryId();
        customerId = rental.getCustomerId();
        returnDate = rental.getReturnDate();
        staffId = rental.getStaffId();
        lastUpdate = rental.getLastUpdate();

        RentalRequestDto rentalRequestDto = new RentalRequestDto( id, rentalDate, inventoryId, customerId, returnDate, staffId, lastUpdate );

        return rentalRequestDto;
    }

    @Override
    public RentalResponseDto toResponseDto(Rental rental) {
        if ( rental == null ) {
            return null;
        }

        Integer id = null;
        Instant rentalDate = null;
        Integer inventoryId = null;
        Integer customerId = null;
        Instant returnDate = null;
        Short staffId = null;
        Instant lastUpdate = null;

        id = rental.getId();
        rentalDate = rental.getRentalDate();
        inventoryId = rental.getInventoryId();
        customerId = rental.getCustomerId();
        returnDate = rental.getReturnDate();
        staffId = rental.getStaffId();
        lastUpdate = rental.getLastUpdate();

        RentalResponseDto rentalResponseDto = new RentalResponseDto( id, rentalDate, inventoryId, customerId, returnDate, staffId, lastUpdate );

        return rentalResponseDto;
    }

    @Override
    public Rental partialUpdate(RentalRequestDto rentalRequestDto, Rental rental) {
        if ( rentalRequestDto == null ) {
            return rental;
        }

        if ( rentalRequestDto.id() != null ) {
            rental.setId( rentalRequestDto.id() );
        }
        if ( rentalRequestDto.rentalDate() != null ) {
            rental.setRentalDate( rentalRequestDto.rentalDate() );
        }
        if ( rentalRequestDto.inventoryId() != null ) {
            rental.setInventoryId( rentalRequestDto.inventoryId() );
        }
        if ( rentalRequestDto.customerId() != null ) {
            rental.setCustomerId( rentalRequestDto.customerId() );
        }
        if ( rentalRequestDto.returnDate() != null ) {
            rental.setReturnDate( rentalRequestDto.returnDate() );
        }
        if ( rentalRequestDto.staffId() != null ) {
            rental.setStaffId( rentalRequestDto.staffId() );
        }
        if ( rentalRequestDto.lastUpdate() != null ) {
            rental.setLastUpdate( rentalRequestDto.lastUpdate() );
        }

        return rental;
    }
}
