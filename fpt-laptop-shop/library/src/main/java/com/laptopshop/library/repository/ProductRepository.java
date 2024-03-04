package com.laptopshop.library.repository;

import com.laptopshop.library.model.Brand;
import com.laptopshop.library.model.Category;
import com.laptopshop.library.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.is_deleted = false")
    List<Product> findAll();

    @Query("select p from Product p where p.is_deleted = false and p.is_activated = true")
    List<Product> getAllProduct();

    @Query("select p from Product p where p.name like %?1% or p.description like %?1%")
    List<Product> findAllByNameOrDescription(String keyword);

    @Query("select p from Product p inner join Category c ON c.id = p.category.id" +
            " where p.category.name = ?1 and p.is_activated = true and p.is_deleted = false")
    List<Product> findAllByCategory(String category);

    @Query("select p from Product p where p.category.name IN :categoryNames")
    List<Product> findAllByCategories(List<String> categoryNames);

    @Query("select p from Product p inner join Brand b ON b.id = p.brand.id" +
            " where p.brand.name = ?1 and p.is_activated = true and p.is_deleted = false")
    List<Product> findAllByBrand(String brand);

    @Query("select p from Product p where p.brand.name IN :brandNames")
    List<Product> findAllByBrands(List<String> brandNames);

    @Query("select p from Product p where p.brand.name IN :brandNames and p.category.name IN :categoryNames")
    List<Product> findByBrandNamesAndCategoryNames(@Param("brandNames") List<String> brandNames, @Param("categoryNames") List<String> categoryNames);

    @Query(value = "select " +
            "p.product_id, p.name, p.description, p.current_quantity, p.cost_price, p.category_id, p.sale_price, p.image, p.is_activated, p.is_deleted " +
            "from products p where p.is_activated = true and p.is_deleted = false order by rand() limit 9", nativeQuery = true)
    List<Product> randomProduct();

    @Query(value = "select " +
            "p.product_id, p.name, p.description, p.current_quantity, p.cost_price, p.category_id, p.sale_price, p.image, p.is_activated, p.is_deleted " +
            "from products p where p.is_deleted = false and p.is_activated = true order by p.cost_price desc limit 9", nativeQuery = true)
    List<Product> filterHighProducts();

    @Query(value = "SELECT * FROM products WHERE price BETWEEN ?1 AND ?2", nativeQuery = true)
    List<Product> filterLowerProducts();

    @Query(value = "SELECT * FROM products WHERE brand.name = ?1 AND category.name = ?2", nativeQuery = true)
    List<Product> filterProducts(String brand, String category);

    @Query(value = "SELECT * FROM products WHERE brand.name = ?1", nativeQuery = true)
    List<Product> filterByBrand(String brand);

    @Query(value = "SELECT * FROM products WHERE category.name = ?1", nativeQuery = true)
    List<Product> filterByCategory(String category);

//    @Query(value = "SELECT * FROM products WHERE price BETWEEN :minPrice AND :maxPrice", nativeQuery = true)
//    List<Product> findByPriceBetween(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);


    @Query(value = "select p.product_id, p.name, p.description, p.current_quantity, p.cost_price, p.category_id, p.sale_price, p.image, p.is_activated, p.is_deleted from products p where p.is_deleted = false and p.is_activated = true limit 4", nativeQuery = true)
    List<Product> listViewProduct();


    @Query(value = "select p from Product p inner join Category c on c.id = ?1 and p.category.id = ?1 where p.is_activated = true and p.is_deleted = false")
    List<Product> getProductByCategoryId(Long id);

    @Query(value = "select p from Product p inner join Brand b on b.id = ?1 and p.brand.id = ?1 where p.is_activated = true and p.is_deleted = false")
    List<Product> getProductByBrandId(Long id);


    @Query("select p from Product p where p.name like %?1% or p.description like %?1%")
    List<Product> searchProducts(String keyword);

    @Query("select p from Product p where p.category = ?1")
    List<Product> findByCategory(Category category);

    @Query("select p from Product p where p.brand = ?1")
    List<Product> findByBrand(Brand brand);
}
