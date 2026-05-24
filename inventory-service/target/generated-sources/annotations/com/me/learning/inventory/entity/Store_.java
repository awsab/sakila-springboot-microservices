package com.me.learning.inventory.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.Instant;

/**
 * Static metamodel for {@link com.me.learning.inventory.entity.Store}
 **/
@StaticMetamodel(Store.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Store_ {

	
	/**
	 * @see #id
	 **/
	public static final String ID = "id";
	
	/**
	 * @see #managerStaffId
	 **/
	public static final String MANAGER_STAFF_ID = "managerStaffId";
	
	/**
	 * @see #addressId
	 **/
	public static final String ADDRESS_ID = "addressId";
	
	/**
	 * @see #lastUpdate
	 **/
	public static final String LAST_UPDATE = "lastUpdate";
	
	/**
	 * @see #inventories
	 **/
	public static final String INVENTORIES = "inventories";

	
	/**
	 * Static metamodel type for {@link com.me.learning.inventory.entity.Store}
	 **/
	public static volatile EntityType<Store> class_;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.inventory.entity.Store#id}
	 **/
	public static volatile SingularAttribute<Store, Short> id;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.inventory.entity.Store#managerStaffId}
	 **/
	public static volatile SingularAttribute<Store, Short> managerStaffId;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.inventory.entity.Store#addressId}
	 **/
	public static volatile SingularAttribute<Store, Integer> addressId;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.inventory.entity.Store#lastUpdate}
	 **/
	public static volatile SingularAttribute<Store, Instant> lastUpdate;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.inventory.entity.Store#inventories}
	 **/
	public static volatile SetAttribute<Store, Inventory> inventories;

}

