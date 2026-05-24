package com.me.learning.catalog.mapper;

import com.me.learning.catalog.dto.ActorRequestDto;
import com.me.learning.catalog.dto.ActorResponseDto;
import com.me.learning.catalog.dto.ActorSummaryDto;
import com.me.learning.catalog.entity.Actor;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-24T07:43:08+0400",
    comments = "version: 1.6.2, compiler: javac, environment: Java 25.0.2 (Eclipse Adoptium)"
)
@Component
public class ActorMapperImpl implements ActorMapper {

    @Override
    public Actor toEntity(ActorRequestDto actorRequestDto) {
        if ( actorRequestDto == null ) {
            return null;
        }

        Actor actor = new Actor();

        actor.setId( actorRequestDto.id() );
        actor.setFirstName( actorRequestDto.firstName() );
        actor.setLastName( actorRequestDto.lastName() );
        actor.setLastUpdate( actorRequestDto.lastUpdate() );

        return actor;
    }

    @Override
    public ActorResponseDto toResponseDto(Actor actor) {
        if ( actor == null ) {
            return null;
        }

        Integer id = null;
        String firstName = null;
        String lastName = null;
        Instant lastUpdate = null;

        id = actor.getId();
        firstName = actor.getFirstName();
        lastName = actor.getLastName();
        lastUpdate = actor.getLastUpdate();

        ActorResponseDto actorResponseDto = new ActorResponseDto( id, firstName, lastName, lastUpdate );

        return actorResponseDto;
    }

    @Override
    public ActorSummaryDto toSummaryDto(Actor actor) {
        if ( actor == null ) {
            return null;
        }

        Integer id = null;
        String firstName = null;
        String lastName = null;

        id = actor.getId();
        firstName = actor.getFirstName();
        lastName = actor.getLastName();

        ActorSummaryDto actorSummaryDto = new ActorSummaryDto( id, firstName, lastName );

        return actorSummaryDto;
    }

    @Override
    public Actor partialUpdate(ActorRequestDto actorRequestDto, Actor actor) {
        if ( actorRequestDto == null ) {
            return actor;
        }

        if ( actorRequestDto.id() != null ) {
            actor.setId( actorRequestDto.id() );
        }
        if ( actorRequestDto.firstName() != null ) {
            actor.setFirstName( actorRequestDto.firstName() );
        }
        if ( actorRequestDto.lastName() != null ) {
            actor.setLastName( actorRequestDto.lastName() );
        }
        if ( actorRequestDto.lastUpdate() != null ) {
            actor.setLastUpdate( actorRequestDto.lastUpdate() );
        }

        return actor;
    }
}
