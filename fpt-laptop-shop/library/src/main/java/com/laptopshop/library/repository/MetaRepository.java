package com.laptopshop.library.repository;

import com.laptopshop.library.model.Country;
import com.laptopshop.library.model.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetaRepository extends JpaRepository<Meta, Long> {
    @Query("SELECT m FROM Meta m WHERE m.product.id = ?1")
    List<Meta> findMetaByProductId(Long id);
}
