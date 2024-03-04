package com.laptopshop.library.repository;

import com.laptopshop.library.dto.BrandDto;
import com.laptopshop.library.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Query(value = "update Brand set name = ?1 where id = ?2")
    Brand update(String name, Long id);

    @Query(value = "select * from brands where is_activated = true", nativeQuery = true)
    List<Brand> findAllByActivatedTrue();

    @Query(value = "select * from brands where is_activated = true and is_deleted = false and name = ?1", nativeQuery = true)
    Optional<Brand> findByName(String name);

    @Query(value = "select new com.laptopshop.library.dto.BrandDto(c.id, c.name, count(p.brand.id)) " +
            "from Brand c left join Product p on c.id = p.brand.id " +
            "where c.activated = true and c.deleted = false " +
            "group by c.id ")
    List<BrandDto> getBrandsAndSize();

    @Query(value = "select * from brands where is_activated = true and is_deleted = false", nativeQuery = true)
    List<Brand> findAllByActivatedTrueAndDeletedFalse();
}
