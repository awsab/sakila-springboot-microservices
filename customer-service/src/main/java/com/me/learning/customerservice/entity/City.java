package com.me.learning.customerservice.entity;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table (name = "city")
public class City {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "city_id", columnDefinition = "smallint UNSIGNED not null")
    private Integer id;

    @Size (max = 50)
    @NotNull
    @Column (name = "city", nullable = false, length = 50)
    private String city;

    @NotNull
    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "country_id", nullable = false)
    private Country country;

    @NotNull
    @ColumnDefault ("CURRENT_TIMESTAMP")
    @Column (name = "last_update", nullable = false)
    private Instant lastUpdate;

    @OneToMany
    @JoinColumn (name = "city_id")
    private Set<Address> addresses = new LinkedHashSet<> ();

}
