package com.laptopshop.library.service.impl;

import com.laptopshop.library.dto.CategoryDto;
import com.laptopshop.library.model.Category;
import com.laptopshop.library.model.Product;
import com.laptopshop.library.repository.CategoryRepository;
import com.laptopshop.library.service.CategoryService;
import com.laptopshop.library.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

    @Override
    public Category save(Category category) {
        Category categorySave = new Category();
        categorySave.setName(category.getName());
        categorySave.setDescription(category.getDescription());
        categorySave.setActivated(true);
        categorySave.setDeleted(false);
        return categoryRepository.save(categorySave);

    }

    @Override
    public Category update(Category category) {
        Category categoryUpdate = categoryRepository.getReferenceById(category.getId());
        categoryUpdate.setName(category.getName());
        categoryUpdate.setDescription(category.getDescription());
        return categoryRepository.save(categoryUpdate);
    }

    @Override
    public List<Category> findAllByActivatedTrue() {
        return categoryRepository.findAllByActivatedTrue();
    }

    @Override
    public List<Category> findALl() {
        // find all category is active and not deleted
        return categoryRepository.findAllByActivatedTrueAndDeletedFalse();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Category> curr = categoryRepository.findById(id);
        if (curr.isPresent()) {
            List<Product> products = productService.getProductsByCategory(curr.get());
            productService.deleteAll(products);
            categoryRepository.delete(curr.get());
        } else {
            throw new RuntimeException("Category not found");
        }
    }

    @Override
    public void enableById(Long id) {
        Optional<Category> curr = categoryRepository.findById(id);
        if(curr.isPresent()){
            Category category = curr.get();
            category.setActivated(true);
            category.setDeleted(false);
            categoryRepository.save(category);
        }
    }

    @Override
    public List<CategoryDto> getCategoriesAndSize() {
        List<CategoryDto> categories = categoryRepository.getCategoriesAndSize();
        return categories;
    }

}


