package com.me.learning.catalog.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.Instant;

/**
 * Static metamodel for {@link com.me.learning.catalog.entity.Actor}
 **/
@StaticMetamodel(Actor.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Actor_ {

	
	/**
	 * @see #id
	 **/
	public static final String ID = "id";
	
	/**
	 * @see #firstName
	 **/
	public static final String FIRST_NAME = "firstName";
	
	/**
	 * @see #lastName
	 **/
	public static final String LAST_NAME = "lastName";
	
	/**
	 * @see #lastUpdate
	 **/
	public static final String LAST_UPDATE = "lastUpdate";
	
	/**
	 * @see #filmActors
	 **/
	public static final String FILM_ACTORS = "filmActors";

	
	/**
	 * Static metamodel type for {@link com.me.learning.catalog.entity.Actor}
	 **/
	public static volatile EntityType<Actor> class_;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Actor#id}
	 **/
	public static volatile SingularAttribute<Actor, Integer> id;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Actor#firstName}
	 **/
	public static volatile SingularAttribute<Actor, String> firstName;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Actor#lastName}
	 **/
	public static volatile SingularAttribute<Actor, String> lastName;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Actor#lastUpdate}
	 **/
	public static volatile SingularAttribute<Actor, Instant> lastUpdate;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Actor#filmActors}
	 **/
	public static volatile SetAttribute<Actor, FilmActor> filmActors;

}

