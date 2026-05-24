package com.me.learning.rental.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Static metamodel for {@link com.me.learning.rental.entity.Payment}
 **/
@StaticMetamodel(Payment.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Payment_ {

	
	/**
	 * @see #id
	 **/
	public static final String ID = "id";
	
	/**
	 * @see #customerId
	 **/
	public static final String CUSTOMER_ID = "customerId";
	
	/**
	 * @see #staffId
	 **/
	public static final String STAFF_ID = "staffId";
	
	/**
	 * @see #rental
	 **/
	public static final String RENTAL = "rental";
	
	/**
	 * @see #amount
	 **/
	public static final String AMOUNT = "amount";
	
	/**
	 * @see #paymentDate
	 **/
	public static final String PAYMENT_DATE = "paymentDate";
	
	/**
	 * @see #lastUpdate
	 **/
	public static final String LAST_UPDATE = "lastUpdate";

	
	/**
	 * Static metamodel type for {@link com.me.learning.rental.entity.Payment}
	 **/
	public static volatile EntityType<Payment> class_;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.rental.entity.Payment#id}
	 **/
	public static volatile SingularAttribute<Payment, Integer> id;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.rental.entity.Payment#customerId}
	 **/
	public static volatile SingularAttribute<Payment, Integer> customerId;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.rental.entity.Payment#staffId}
	 **/
	public static volatile SingularAttribute<Payment, Short> staffId;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.rental.entity.Payment#rental}
	 **/
	public static volatile SingularAttribute<Payment, Rental> rental;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.rental.entity.Payment#amount}
	 **/
	public static volatile SingularAttribute<Payment, BigDecimal> amount;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.rental.entity.Payment#paymentDate}
	 **/
	public static volatile SingularAttribute<Payment, Instant> paymentDate;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.rental.entity.Payment#lastUpdate}
	 **/
	public static volatile SingularAttribute<Payment, Instant> lastUpdate;

}

