package com.me.learning.catalog.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Static metamodel for {@link com.me.learning.catalog.entity.Film}
 **/
@StaticMetamodel(Film.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Film_ {

	
	/**
	 * @see #id
	 **/
	public static final String ID = "id";
	
	/**
	 * @see #title
	 **/
	public static final String TITLE = "title";
	
	/**
	 * @see #description
	 **/
	public static final String DESCRIPTION = "description";
	
	/**
	 * @see #releaseYear
	 **/
	public static final String RELEASE_YEAR = "releaseYear";
	
	/**
	 * @see #language
	 **/
	public static final String LANGUAGE = "language";
	
	/**
	 * @see #originalLanguage
	 **/
	public static final String ORIGINAL_LANGUAGE = "originalLanguage";
	
	/**
	 * @see #rentalDuration
	 **/
	public static final String RENTAL_DURATION = "rentalDuration";
	
	/**
	 * @see #rentalRate
	 **/
	public static final String RENTAL_RATE = "rentalRate";
	
	/**
	 * @see #length
	 **/
	public static final String LENGTH = "length";
	
	/**
	 * @see #replacementCost
	 **/
	public static final String REPLACEMENT_COST = "replacementCost";
	
	/**
	 * @see #rating
	 **/
	public static final String RATING = "rating";
	
	/**
	 * @see #specialFeatures
	 **/
	public static final String SPECIAL_FEATURES = "specialFeatures";
	
	/**
	 * @see #lastUpdate
	 **/
	public static final String LAST_UPDATE = "lastUpdate";
	
	/**
	 * @see #filmActors
	 **/
	public static final String FILM_ACTORS = "filmActors";
	
	/**
	 * @see #filmCategories
	 **/
	public static final String FILM_CATEGORIES = "filmCategories";

	
	/**
	 * Static metamodel type for {@link com.me.learning.catalog.entity.Film}
	 **/
	public static volatile EntityType<Film> class_;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Film#id}
	 **/
	public static volatile SingularAttribute<Film, Integer> id;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Film#title}
	 **/
	public static volatile SingularAttribute<Film, String> title;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Film#description}
	 **/
	public static volatile SingularAttribute<Film, String> description;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Film#releaseYear}
	 **/
	public static volatile SingularAttribute<Film, Short> releaseYear;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Film#language}
	 **/
	public static volatile SingularAttribute<Film, Language> language;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Film#originalLanguage}
	 **/
	public static volatile SingularAttribute<Film, Language> originalLanguage;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Film#rentalDuration}
	 **/
	public static volatile SingularAttribute<Film, Short> rentalDuration;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Film#rentalRate}
	 **/
	public static volatile SingularAttribute<Film, BigDecimal> rentalRate;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Film#length}
	 **/
	public static volatile SingularAttribute<Film, Short> length;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Film#replacementCost}
	 **/
	public static volatile SingularAttribute<Film, BigDecimal> replacementCost;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Film#rating}
	 **/
	public static volatile SingularAttribute<Film, String> rating;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Film#specialFeatures}
	 **/
	public static volatile SingularAttribute<Film, String> specialFeatures;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Film#lastUpdate}
	 **/
	public static volatile SingularAttribute<Film, Instant> lastUpdate;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Film#filmActors}
	 **/
	public static volatile SetAttribute<Film, FilmActor> filmActors;
	
	/**
	 * Static metamodel for attribute {@link com.me.learning.catalog.entity.Film#filmCategories}
	 **/
	public static volatile SetAttribute<Film, FilmCategory> filmCategories;

}

