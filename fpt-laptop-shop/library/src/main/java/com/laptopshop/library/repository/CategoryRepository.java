package com.laptopshop.library.repository;

import com.laptopshop.library.dto.CategoryDto;
import com.laptopshop.library.model.CartItem;
import com.laptopshop.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "update Category set name = ?1 , description = ?2 where id = ?3")
    Category update(String name, String description, Long id);

    @Query(value = "select * from categories where is_activated = true", nativeQuery = true)
    List<Category> findAllByActivatedTrue();

    @Query(value = "select * from categories where is_activated = true and is_deleted = false and id = ?1", nativeQuery = true)
    Optional<Category> findByName(String name);

    @Query(value = "select new com.laptopshop.library.dto.CategoryDto(c.id, c.name, count(p.category.id)) " +
            "from Category c left join Product p on c.id = p.category.id " +
            "where c.activated = true and c.deleted = false " +
            "group by c.id ")
    List<CategoryDto> getCategoriesAndSize();

    @Query(value = "select * from categories where is_activated = true and is_deleted = false", nativeQuery = true)
    List<Category> findAllByActivatedTrueAndDeletedFalse();
}
