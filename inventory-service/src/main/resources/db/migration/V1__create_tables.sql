-- Inventory-service schema for MySQL/Flyway.
-- Matches the entity model in inventory-service/src/main/java/com/me/learning/inventory/entity.
CREATE TABLE IF NOT EXISTS store (
    store_id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    manager_staff_id TINYINT UNSIGNED NOT NULL,
    address_id SMALLINT UNSIGNED NOT NULL,
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (store_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS inventory (
    inventory_id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT,
    film_id SMALLINT UNSIGNED NOT NULL,
    store_id TINYINT UNSIGNED NOT NULL,
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (inventory_id),
    KEY idx_inventory_film_id (film_id),
    KEY idx_inventory_store_id (store_id),
    CONSTRAINT fk_inventory_store
        FOREIGN KEY (store_id) REFERENCES store (store_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
