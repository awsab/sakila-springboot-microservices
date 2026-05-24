-- Customer-service schema for MySQL/Flyway.
-- Matches the entity model in customer-service/src/main/java/com/me/learning/customerservice/entity.

CREATE TABLE IF NOT EXISTS country (
    country_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    country VARCHAR(50) NOT NULL,
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (country_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS city (
    city_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    city VARCHAR(50) NOT NULL,
    country_id SMALLINT UNSIGNED NOT NULL,
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (city_id),
    KEY idx_city_country_id (country_id),
    CONSTRAINT fk_city_country
        FOREIGN KEY (country_id) REFERENCES country (country_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS address (
    address_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    address VARCHAR(50) NOT NULL,
    address2 VARCHAR(50) DEFAULT NULL,
    district VARCHAR(20) NOT NULL,
    city_id SMALLINT UNSIGNED NOT NULL,
    postal_code VARCHAR(10) DEFAULT NULL,
    phone VARCHAR(20) NOT NULL,
    location GEOMETRY DEFAULT NULL,
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (address_id),
    KEY idx_address_city_id (city_id),
    CONSTRAINT fk_address_city
        FOREIGN KEY (city_id) REFERENCES city (city_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS staff (
    staff_id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(45) NOT NULL,
    last_name VARCHAR(45) NOT NULL,
    address_id SMALLINT UNSIGNED NOT NULL,
    picture LONGBLOB,
    email VARCHAR(50) DEFAULT NULL,
    active TINYINT(1) NOT NULL DEFAULT 1,
    username VARCHAR(16) NOT NULL,
    password VARCHAR(40) DEFAULT NULL,
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (staff_id),
    KEY idx_staff_address_id (address_id),
    CONSTRAINT fk_staff_address
        FOREIGN KEY (address_id) REFERENCES address (address_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS customer (
    customer_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(45) NOT NULL,
    last_name VARCHAR(45) NOT NULL,
    email VARCHAR(50) DEFAULT NULL,
    address_id SMALLINT UNSIGNED NOT NULL,
    active TINYINT(1) NOT NULL DEFAULT 1,
    create_date DATETIME NOT NULL,
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (customer_id),
    KEY idx_customer_address_id (address_id),
    CONSTRAINT fk_customer_address
        FOREIGN KEY (address_id) REFERENCES address (address_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

