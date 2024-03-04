package com.laptopshop.library.repository;

import com.laptopshop.library.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    @Query("SELECT c FROM City c WHERE c.country.id = ?1")
    List<City> findByCountryId(Long countryId);
}
