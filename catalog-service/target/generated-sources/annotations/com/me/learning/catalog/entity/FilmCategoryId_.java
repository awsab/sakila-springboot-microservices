package com.me.learning.catalog.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

/**
 * Static metamodel for {@link com.me.learning.catalog.entity.FilmCategoryId}
 **/
@StaticMetamodel(FilmCategoryId.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class FilmCategoryId_ {

	
	/**
	 * @see #filmId
	 **/
	public static final String FILM_ID = "filmId";
	
	/**
	 * @see #categoryId
	 **/
	public static final String CATEGORY_ID = "categoryId";

	
	/**
	 * Static metamodel type for {@link com.me.learning.catalog.entity.FilmCategoryId}
	 **/
	public static volatile EmbeddableType<FilmCategoryId> class_;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.FilmCategoryId#filmId}
	 **/
	public static volatile SingularAttribute<FilmCategoryId, Integer> filmId;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.FilmCategoryId#categoryId}
	 **/
	public static volatile SingularAttribute<FilmCategoryId, Short> categoryId;

}

