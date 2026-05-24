package com.me.learning.rental.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.Instant;

/**
 * Static metamodel for {@link com.me.learning.rental.entity.Rental}
 **/
@StaticMetamodel(Rental.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Rental_ {

	
	/**
	 * @see #id
	 **/
	public static final String ID = "id";
	
	/**
	 * @see #rentalDate
	 **/
	public static final String RENTAL_DATE = "rentalDate";
	
	/**
	 * @see #inventoryId
	 **/
	public static final String INVENTORY_ID = "inventoryId";
	
	/**
	 * @see #customerId
	 **/
	public static final String CUSTOMER_ID = "customerId";
	
	/**
	 * @see #returnDate
	 **/
	public static final String RETURN_DATE = "returnDate";
	
	/**
	 * @see #staffId
	 **/
	public static final String STAFF_ID = "staffId";
	
	/**
	 * @see #lastUpdate
	 **/
	public static final String LAST_UPDATE = "lastUpdate";
	
	/**
	 * @see #payments
	 **/
	public static final String PAYMENTS = "payments";

	
	/**
	 * Static metamodel type for {@link com.me.learning.rental.entity.Rental}
	 **/
	public static volatile EntityType<Rental> class_;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.rental.entity.Rental#id}
	 **/
	public static volatile SingularAttribute<Rental, Integer> id;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.rental.entity.Rental#rentalDate}
	 **/
	public static volatile SingularAttribute<Rental, Instant> rentalDate;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.rental.entity.Rental#inventoryId}
	 **/
	public static volatile SingularAttribute<Rental, Integer> inventoryId;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.rental.entity.Rental#customerId}
	 **/
	public static volatile SingularAttribute<Rental, Integer> customerId;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.rental.entity.Rental#returnDate}
	 **/
	public static volatile SingularAttribute<Rental, Instant> returnDate;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.rental.entity.Rental#staffId}
	 **/
	public static volatile SingularAttribute<Rental, Short> staffId;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.rental.entity.Rental#lastUpdate}
	 **/
	public static volatile SingularAttribute<Rental, Instant> lastUpdate;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.rental.entity.Rental#payments}
	 **/
	public static volatile SetAttribute<Rental, Payment> payments;

}

