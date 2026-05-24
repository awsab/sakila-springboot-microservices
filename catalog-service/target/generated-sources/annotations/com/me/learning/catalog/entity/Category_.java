package com.me.learning.catalog.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.Instant;

/**
 * Static metamodel for {@link com.me.learning.catalog.entity.Category}
 **/
@StaticMetamodel(Category.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Category_ {

	
	/**
	 * @see #id
	 **/
	public static final String ID = "id";
	
	/**
	 * @see #name
	 **/
	public static final String NAME = "name";
	
	/**
	 * @see #lastUpdate
	 **/
	public static final String LAST_UPDATE = "lastUpdate";
	
	/**
	 * @see #filmCategories
	 **/
	public static final String FILM_CATEGORIES = "filmCategories";

	
	/**
	 * Static metamodel type for {@link com.me.learning.catalog.entity.Category}
	 **/
	public static volatile EntityType<Category> class_;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Category#id}
	 **/
	public static volatile SingularAttribute<Category, Short> id;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Category#name}
	 **/
	public static volatile SingularAttribute<Category, String> name;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Category#lastUpdate}
	 **/
	public static volatile SingularAttribute<Category, Instant> lastUpdate;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Category#filmCategories}
	 **/
	public static volatile SetAttribute<Category, FilmCategory> filmCategories;

}

