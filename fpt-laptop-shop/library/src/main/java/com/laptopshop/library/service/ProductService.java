package com.laptopshop.library.service;

import com.laptopshop.library.dto.ProductDto;
import com.laptopshop.library.model.Admin;
import com.laptopshop.library.model.Brand;
import com.laptopshop.library.model.Category;
import com.laptopshop.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<Product> findAll();

    List<ProductDto> products();

    List<ProductDto> allProduct();

    Product save(MultipartFile imageProduct, ProductDto product);

    Product update(MultipartFile imageProduct, ProductDto productDto);

    void enableById(Long id);

    void deleteById(Long id);

    ProductDto getById(Long id);

    Product findById(Long id);

    List<Product> filterProducts(List<ProductDto> products, String brand, String category);

    List<ProductDto> randomProduct();

    Page<ProductDto> searchProducts(int pageNo, String keyword);

    Page<ProductDto> getAllProducts(int pageNo);

    Page<ProductDto> getAllProductsForCustomer(int pageNo);


    List<ProductDto> findAllByCategory(String category);

    List<ProductDto> findAllByCategories(List<String> categoryNames);

    List<ProductDto> findAllByBrand(String brand);

    List<ProductDto> findAllByBrands(List<String> brandNames);

    List<ProductDto> getProductsByBrandAndCategory(List<String> brandNames, List<String> categoryName);

    List<ProductDto> filterHighProducts();

    List<ProductDto> filterLowerProducts();

    // In ProductService.java
    List<ProductDto> filterByPriceRange(int type, List<ProductDto> products);

    List<ProductDto> listViewProducts();

    List<ProductDto> findByCategoryId(Long id);

    List<ProductDto> findByBrandId(Long id);

    List<ProductDto> searchProducts(String keyword);

    List<Product> getProductsByCategory(Category category);

    List<Product> getProductsByBrand(Brand brand);

    void deleteAll(List<Product> products);

    void updateQuantity(ProductDto productDto, String adminUsername);
}
