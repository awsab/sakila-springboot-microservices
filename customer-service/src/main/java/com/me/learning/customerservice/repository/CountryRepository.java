package com.me.learning.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.me.learning.customerservice.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Integer>, JpaSpecificationExecutor<Country> {
}
