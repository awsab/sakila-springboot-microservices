package com.me.learning.catalog.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

/**
 * Static metamodel for {@link com.me.learning.catalog.entity.FilmText}
 **/
@StaticMetamodel(FilmText.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class FilmText_ {

	
	/**
	 * @see #filmId
	 **/
	public static final String FILM_ID = "filmId";
	
	/**
	 * @see #title
	 **/
	public static final String TITLE = "title";
	
	/**
	 * @see #description
	 **/
	public static final String DESCRIPTION = "description";

	
	/**
	 * Static metamodel type for {@link com.me.learning.catalog.entity.FilmText}
	 **/
	public static volatile EntityType<FilmText> class_;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.FilmText#filmId}
	 **/
	public static volatile SingularAttribute<FilmText, Integer> filmId;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.FilmText#title}
	 **/
	public static volatile SingularAttribute<FilmText, String> title;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.FilmText#description}
	 **/
	public static volatile SingularAttribute<FilmText, String> description;

}

