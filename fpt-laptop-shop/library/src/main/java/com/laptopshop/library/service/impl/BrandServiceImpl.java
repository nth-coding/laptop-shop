package com.laptopshop.library.service.impl;

import com.laptopshop.library.dto.BrandDto;
import com.laptopshop.library.model.Brand;
import com.laptopshop.library.model.Product;
import com.laptopshop.library.repository.BrandRepository;
import com.laptopshop.library.service.BrandService;
import com.laptopshop.library.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    private final ProductService productService;

    @Override
    public Brand save(Brand brand) {
        Brand brandSave = new Brand();
        brandSave.setName(brand.getName());
        brandSave.setDescription(brand.getDescription());
        brandSave.setActivated(true);
        brandSave.setDeleted(false);
        return brandRepository.save(brandSave);
    }

    @Override
    public Brand update(Brand brand) {
        Brand brandUpdate = brandRepository.getReferenceById(brand.getId());
        brandUpdate.setName(brand.getName());
        brandUpdate.setDescription(brand.getDescription());
        return brandRepository.save(brandUpdate);
    }

    @Override
    public List<Brand> findAllByActivatedTrue() {
        return brandRepository.findAllByActivatedTrue();
    }

    @Override
    public List<Brand> findALl() {
        // find all brand is active and not deleted
        return brandRepository.findAllByActivatedTrueAndDeletedFalse();
    }

    @Override
    public Optional<Brand> findById(Long id) {
        return brandRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Brand> curr = brandRepository.findById(id);
        if(curr.isPresent()){
            List<Product> products = productService.getProductsByBrand(curr.get());
            productService.deleteAll(products);
            brandRepository.delete(curr.get());
        } else {
            throw new RuntimeException("Brand not found");
        }
    }

    @Override
    public void enableById(Long id) {
        Optional<Brand> curr = brandRepository.findById(id);
        if(curr.isPresent()){
            Brand brand = curr.get();
            brand.setActivated(true);
            brand.setDeleted(false);
            brandRepository.save(brand);
        }

    }

    @Override
    public List<BrandDto> getBrandsAndSize() {
        List<BrandDto> brands = brandRepository.getBrandsAndSize();
        return brands;
    }

    @Override
    public Optional<Brand> findByName(String name) {
        return brandRepository.findByName(name);
    }

}


