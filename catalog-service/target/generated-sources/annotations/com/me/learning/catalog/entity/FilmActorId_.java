package com.me.learning.catalog.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

/**
 * Static metamodel for {@link com.me.learning.catalog.entity.FilmActorId}
 **/
@StaticMetamodel(FilmActorId.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class FilmActorId_ {

	
	/**
	 * @see #filmId
	 **/
	public static final String FILM_ID = "filmId";
	
	/**
	 * @see #actorId
	 **/
	public static final String ACTOR_ID = "actorId";

	
	/**
	 * Static metamodel type for {@link com.me.learning.catalog.entity.FilmActorId}
	 **/
	public static volatile EmbeddableType<FilmActorId> class_;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.FilmActorId#filmId}
	 **/
	public static volatile SingularAttribute<FilmActorId, Integer> filmId;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.FilmActorId#actorId}
	 **/
	public static volatile SingularAttribute<FilmActorId, Integer> actorId;

}

