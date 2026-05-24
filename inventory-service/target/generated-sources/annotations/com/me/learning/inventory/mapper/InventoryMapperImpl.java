package com.me.learning.inventory.mapper;

import com.me.learning.inventory.dto.InventoryRequestDto;
import com.me.learning.inventory.dto.InventoryResponseDto;
import com.me.learning.inventory.dto.StoreRefDto;
import com.me.learning.inventory.entity.Inventory;
import com.me.learning.inventory.entity.Store;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-24T07:45:00+0400",
    comments = "version: 1.6.2, compiler: javac, environment: Java 25.0.2 (Eclipse Adoptium)"
)
@Component
public class InventoryMapperImpl implements InventoryMapper {

    @Override
    public Inventory toEntity(InventoryRequestDto inventoryRequestDto) {
        if ( inventoryRequestDto == null ) {
            return null;
        }

        Inventory inventory = new Inventory();

        inventory.setStore( storeRefDtoToStore( inventoryRequestDto.store() ) );
        inventory.setId( inventoryRequestDto.id() );
        inventory.setFilmId( inventoryRequestDto.filmId() );
        inventory.setLastUpdate( inventoryRequestDto.lastUpdate() );

        return inventory;
    }

    @Override
    public InventoryRequestDto toDto(Inventory inventory) {
        if ( inventory == null ) {
            return null;
        }

        StoreRefDto store = null;
        Integer id = null;
        Integer filmId = null;
        Instant lastUpdate = null;

        store = storeToStoreRefDto( inventory.getStore() );
        id = inventory.getId();
        filmId = inventory.getFilmId();
        lastUpdate = inventory.getLastUpdate();

        InventoryRequestDto inventoryRequestDto = new InventoryRequestDto( id, filmId, store, lastUpdate );

        return inventoryRequestDto;
    }

    @Override
    public InventoryResponseDto toResponseDto(Inventory inventory) {
        if ( inventory == null ) {
            return null;
        }

        Short storeId = null;
        Integer id = null;
        Integer filmId = null;
        Instant lastUpdate = null;

        storeId = inventoryStoreId( inventory );
        id = inventory.getId();
        filmId = inventory.getFilmId();
        lastUpdate = inventory.getLastUpdate();

        InventoryResponseDto inventoryResponseDto = new InventoryResponseDto( id, filmId, storeId, lastUpdate );

        return inventoryResponseDto;
    }

    @Override
    public Inventory partialUpdate(InventoryRequestDto inventoryRequestDto, Inventory inventory) {
        if ( inventoryRequestDto == null ) {
            return inventory;
        }

        if ( inventoryRequestDto.store() != null ) {
            if ( inventory.getStore() == null ) {
                inventory.setStore( new Store() );
            }
            storeRefDtoToStore1( inventoryRequestDto.store(), inventory.getStore() );
        }
        if ( inventoryRequestDto.id() != null ) {
            inventory.setId( inventoryRequestDto.id() );
        }
        if ( inventoryRequestDto.filmId() != null ) {
            inventory.setFilmId( inventoryRequestDto.filmId() );
        }
        if ( inventoryRequestDto.lastUpdate() != null ) {
            inventory.setLastUpdate( inventoryRequestDto.lastUpdate() );
        }

        return inventory;
    }

    protected Store storeRefDtoToStore(StoreRefDto storeRefDto) {
        if ( storeRefDto == null ) {
            return null;
        }

        Store store = new Store();

        store.setId( storeRefDto.id() );

        return store;
    }

    protected StoreRefDto storeToStoreRefDto(Store store) {
        if ( store == null ) {
            return null;
        }

        Short id = null;

        id = store.getId();

        StoreRefDto storeRefDto = new StoreRefDto( id );

        return storeRefDto;
    }

    private Short inventoryStoreId(Inventory inventory) {
        Store store = inventory.getStore();
        if ( store == null ) {
            return null;
        }
        return store.getId();
    }

    protected void storeRefDtoToStore1(StoreRefDto storeRefDto, Store mappingTarget) {
        if ( storeRefDto == null ) {
            return;
        }

        if ( storeRefDto.id() != null ) {
            mappingTarget.setId( storeRefDto.id() );
        }
    }
}
