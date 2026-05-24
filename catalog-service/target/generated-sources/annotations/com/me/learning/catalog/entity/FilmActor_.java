package com.me.learning.catalog.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.Instant;

/**
 * Static metamodel for {@link com.me.learning.catalog.entity.FilmActor}
 **/
@StaticMetamodel(FilmActor.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class FilmActor_ {

	
	/**
	 * @see #id
	 **/
	public static final String ID = "id";
	
	/**
	 * @see #film
	 **/
	public static final String FILM = "film";
	
	/**
	 * @see #actor
	 **/
	public static final String ACTOR = "actor";
	
	/**
	 * @see #lastUpdate
	 **/
	public static final String LAST_UPDATE = "lastUpdate";

	
	/**
	 * Static metamodel type for {@link com.me.learning.catalog.entity.FilmActor}
	 **/
	public static volatile EntityType<FilmActor> class_;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.FilmActor#id}
	 **/
	public static volatile SingularAttribute<FilmActor, FilmActorId> id;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.FilmActor#film}
	 **/
	public static volatile SingularAttribute<FilmActor, Film> film;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.FilmActor#actor}
	 **/
	public static volatile SingularAttribute<FilmActor, Actor> actor;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.FilmActor#lastUpdate}
	 **/
	public static volatile SingularAttribute<FilmActor, Instant> lastUpdate;

}

