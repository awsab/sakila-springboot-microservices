-- Rental-service schema for MySQL/Flyway.
-- Matches the entity model in rental-service/src/main/java/com/me/learning/rental/entity.
CREATE TABLE IF NOT EXISTS rental (
    rental_id INT NOT NULL AUTO_INCREMENT,
    rental_date DATETIME NOT NULL,
    inventory_id MEDIUMINT UNSIGNED NOT NULL,
    customer_id SMALLINT UNSIGNED NOT NULL,
    return_date DATETIME DEFAULT NULL,
    staff_id TINYINT UNSIGNED NOT NULL,
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (rental_id),
    UNIQUE KEY rental_date (rental_date, inventory_id, customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS payment (
    payment_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    customer_id SMALLINT UNSIGNED NOT NULL,
    staff_id TINYINT UNSIGNED NOT NULL,
    rental_id INT DEFAULT NULL,
    amount DECIMAL(5,2) NOT NULL,
    payment_date DATETIME NOT NULL,
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (payment_id),
    KEY idx_payment_rental_id (rental_id),
    KEY idx_payment_customer_id (customer_id),
    KEY idx_payment_staff_id (staff_id),
    CONSTRAINT fk_payment_rental
        FOREIGN KEY (rental_id) REFERENCES rental (rental_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
