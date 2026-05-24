package com.me.learning.catalog.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.Instant;

/**
 * Static metamodel for {@link com.me.learning.catalog.entity.FilmCategory}
 **/
@StaticMetamodel(FilmCategory.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class FilmCategory_ {

	
	/**
	 * @see #id
	 **/
	public static final String ID = "id";
	
	/**
	 * @see #film
	 **/
	public static final String FILM = "film";
	
	/**
	 * @see #category
	 **/
	public static final String CATEGORY = "category";
	
	/**
	 * @see #lastUpdate
	 **/
	public static final String LAST_UPDATE = "lastUpdate";

	
	/**
	 * Static metamodel type for {@link com.me.learning.catalog.entity.FilmCategory}
	 **/
	public static volatile EntityType<FilmCategory> class_;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.FilmCategory#id}
	 **/
	public static volatile SingularAttribute<FilmCategory, FilmCategoryId> id;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.FilmCategory#film}
	 **/
	public static volatile SingularAttribute<FilmCategory, Film> film;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.FilmCategory#category}
	 **/
	public static volatile SingularAttribute<FilmCategory, Category> category;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.FilmCategory#lastUpdate}
	 **/
	public static volatile SingularAttribute<FilmCategory, Instant> lastUpdate;

}

