package com.me.learning.catalog.mapper;

import com.me.learning.catalog.dto.ActorSummaryDto;
import com.me.learning.catalog.dto.CategorySummaryDto;
import com.me.learning.catalog.dto.FilmRequestDto;
import com.me.learning.catalog.dto.FilmResponseDto;
import com.me.learning.catalog.entity.Film;
import com.me.learning.catalog.entity.Language;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-24T07:43:08+0400",
    comments = "version: 1.6.2, compiler: javac, environment: Java 25.0.2 (Eclipse Adoptium)"
)
@Component
public class FilmMapperImpl implements FilmMapper {

    @Override
    public Film toEntity(FilmRequestDto filmRequestDto) {
        if ( filmRequestDto == null ) {
            return null;
        }

        Film film = new Film();

        film.setId( filmRequestDto.id() );
        film.setTitle( filmRequestDto.title() );
        film.setDescription( filmRequestDto.description() );
        film.setReleaseYear( filmRequestDto.releaseYear() );
        film.setRentalDuration( filmRequestDto.rentalDuration() );
        film.setRentalRate( filmRequestDto.rentalRate() );
        film.setLength( filmRequestDto.length() );
        film.setReplacementCost( filmRequestDto.replacementCost() );
        film.setRating( filmRequestDto.rating() );
        film.setSpecialFeatures( filmRequestDto.specialFeatures() );
        film.setLastUpdate( filmRequestDto.lastUpdate() );

        return film;
    }

    @Override
    public Film partialUpdate(FilmRequestDto filmRequestDto, Film film) {
        if ( filmRequestDto == null ) {
            return film;
        }

        if ( filmRequestDto.id() != null ) {
            film.setId( filmRequestDto.id() );
        }
        if ( filmRequestDto.title() != null ) {
            film.setTitle( filmRequestDto.title() );
        }
        if ( filmRequestDto.description() != null ) {
            film.setDescription( filmRequestDto.description() );
        }
        if ( filmRequestDto.releaseYear() != null ) {
            film.setReleaseYear( filmRequestDto.releaseYear() );
        }
        if ( filmRequestDto.rentalDuration() != null ) {
            film.setRentalDuration( filmRequestDto.rentalDuration() );
        }
        if ( filmRequestDto.rentalRate() != null ) {
            film.setRentalRate( filmRequestDto.rentalRate() );
        }
        if ( filmRequestDto.length() != null ) {
            film.setLength( filmRequestDto.length() );
        }
        if ( filmRequestDto.replacementCost() != null ) {
            film.setReplacementCost( filmRequestDto.replacementCost() );
        }
        if ( filmRequestDto.rating() != null ) {
            film.setRating( filmRequestDto.rating() );
        }
        if ( filmRequestDto.specialFeatures() != null ) {
            film.setSpecialFeatures( filmRequestDto.specialFeatures() );
        }
        if ( filmRequestDto.lastUpdate() != null ) {
            film.setLastUpdate( filmRequestDto.lastUpdate() );
        }

        return film;
    }

    @Override
    public FilmResponseDto toFilmResponseDto(Film film) {
        if ( film == null ) {
            return null;
        }

        Short languageId = null;
        String languageName = null;
        Short originalLanguageId = null;
        String originalLanguageName = null;
        Integer id = null;
        String title = null;
        String description = null;
        Short releaseYear = null;
        Short rentalDuration = null;
        BigDecimal rentalRate = null;
        Short length = null;
        BigDecimal replacementCost = null;
        String rating = null;
        String specialFeatures = null;
        Instant lastUpdate = null;

        languageId = filmLanguageId( film );
        languageName = filmLanguageName( film );
        originalLanguageId = filmOriginalLanguageId( film );
        originalLanguageName = filmOriginalLanguageName( film );
        id = film.getId();
        title = film.getTitle();
        description = film.getDescription();
        releaseYear = film.getReleaseYear();
        rentalDuration = film.getRentalDuration();
        rentalRate = film.getRentalRate();
        length = film.getLength();
        replacementCost = film.getReplacementCost();
        rating = film.getRating();
        specialFeatures = film.getSpecialFeatures();
        lastUpdate = film.getLastUpdate();

        List<ActorSummaryDto> actors = toActorSummaryDtos(film);
        List<CategorySummaryDto> categories = toCategorySummaryDtos(film);

        FilmResponseDto filmResponseDto = new FilmResponseDto( id, title, description, releaseYear, languageId, languageName, originalLanguageId, originalLanguageName, rentalDuration, rentalRate, length, replacementCost, rating, specialFeatures, lastUpdate, actors, categories );

        return filmResponseDto;
    }

    private Short filmLanguageId(Film film) {
        Language language = film.getLanguage();
        if ( language == null ) {
            return null;
        }
        return language.getId();
    }

    private String filmLanguageName(Film film) {
        Language language = film.getLanguage();
        if ( language == null ) {
            return null;
        }
        return language.getName();
    }

    private Short filmOriginalLanguageId(Film film) {
        Language originalLanguage = film.getOriginalLanguage();
        if ( originalLanguage == null ) {
            return null;
        }
        return originalLanguage.getId();
    }

    private String filmOriginalLanguageName(Film film) {
        Language originalLanguage = film.getOriginalLanguage();
        if ( originalLanguage == null ) {
            return null;
        }
        return originalLanguage.getName();
    }
}
