package com.me.learning.inventory.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.Instant;

/**
 * Static metamodel for {@link com.me.learning.inventory.entity.Inventory}
 **/
@StaticMetamodel(Inventory.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Inventory_ {

	
	/**
	 * @see #id
	 **/
	public static final String ID = "id";
	
	/**
	 * @see #filmId
	 **/
	public static final String FILM_ID = "filmId";
	
	/**
	 * @see #store
	 **/
	public static final String STORE = "store";
	
	/**
	 * @see #lastUpdate
	 **/
	public static final String LAST_UPDATE = "lastUpdate";

	
	/**
	 * Static metamodel type for {@link com.me.learning.inventory.entity.Inventory}
	 **/
	public static volatile EntityType<Inventory> class_;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.inventory.entity.Inventory#id}
	 **/
	public static volatile SingularAttribute<Inventory, Integer> id;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.inventory.entity.Inventory#filmId}
	 **/
	public static volatile SingularAttribute<Inventory, Integer> filmId;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.inventory.entity.Inventory#store}
	 **/
	public static volatile SingularAttribute<Inventory, Store> store;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.inventory.entity.Inventory#lastUpdate}
	 **/
	public static volatile SingularAttribute<Inventory, Instant> lastUpdate;

}

