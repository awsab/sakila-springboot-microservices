package com.me.learning.catalog.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.Instant;

/**
 * Static metamodel for {@link com.me.learning.catalog.entity.Language}
 **/
@StaticMetamodel(Language.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Language_ {

	
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
	 * Static metamodel type for {@link com.me.learning.catalog.entity.Language}
	 **/
	public static volatile EntityType<Language> class_;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Language#id}
	 **/
	public static volatile SingularAttribute<Language, Short> id;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Language#name}
	 **/
	public static volatile SingularAttribute<Language, String> name;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Language#lastUpdate}
	 **/
	public static volatile SingularAttribute<Language, Instant> lastUpdate;

}

