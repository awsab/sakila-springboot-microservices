package com.me.learning.customerservice.entity;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table (name = "country")
public class Country {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "country_id", columnDefinition = "smallint UNSIGNED not null")
    private Integer id;

    @Size (max = 50)
    @NotNull
    @Column (name = "country", nullable = false, length = 50)
    private String country;

    @NotNull
    @ColumnDefault ("CURRENT_TIMESTAMP")
    @Column (name = "last_update", nullable = false)
    private Instant lastUpdate;

    @OneToMany
    @JoinColumn (name = "country_id")
    private Set<City> cities = new LinkedHashSet<> ();

}
