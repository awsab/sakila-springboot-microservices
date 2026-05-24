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
@Table (name = "address")
public class Address {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "address_id", columnDefinition = "smallint UNSIGNED not null")
    private Integer id;

    @Size (max = 50)
    @NotNull
    @Column (name = "address", nullable = false, length = 50)
    private String address;

    @Size (max = 50)
    @Column (name = "address2", length = 50)
    private String address2;

    @Size (max = 20)
    @NotNull
    @Column (name = "district", nullable = false, length = 20)
    private String district;

    @NotNull
    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "city_id", nullable = false)
    private City city;

    @Size (max = 10)
    @Column (name = "postal_code", length = 10)
    private String postalCode;

    @Size (max = 20)
    @NotNull
    @Column (name = "phone", nullable = false, length = 20)
    private String phone;

    // geometry is an optional spatial field; no business logic reads/writes it via JPA.
    // Keeping nullable here allows JPA-based inserts (e.g. in tests) without requiring
    // a full JTS / hibernate-spatial stack.
    @Column (name = "location", columnDefinition = "geometry")
    private Object location;

    @NotNull
    @ColumnDefault ("CURRENT_TIMESTAMP")
    @Column (name = "last_update", nullable = false)
    private Instant lastUpdate;

    @OneToMany
    @JoinColumn (name = "address_id")
    private Set<Staff> staff = new LinkedHashSet<> ();

}
