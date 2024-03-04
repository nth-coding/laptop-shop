package com.laptopshop.library.service.impl;

import com.laptopshop.library.dto.ProductDto;
import com.laptopshop.library.model.Brand;
import com.laptopshop.library.model.Category;
import com.laptopshop.library.model.Meta;
import com.laptopshop.library.model.Product;
import com.laptopshop.library.repository.CartItemRepository;
import com.laptopshop.library.repository.MetaRepository;
import com.laptopshop.library.repository.ProductRepository;
import com.laptopshop.library.service.BrandService;
import com.laptopshop.library.service.ImportHistoryService;
import com.laptopshop.library.service.ProductService;
import com.laptopshop.library.utils.ImageUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private final ImageUpload imageUpload;

    private final CartItemRepository cartItemRepository;

    private final MetaRepository metaRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductDto> products() {
        return transferData(productRepository.getAllProduct());
    }

    @Override
    public List<ProductDto> allProduct() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = transferData(products);
        return productDtos;
    }

    @Override
    public Product save(MultipartFile imageProduct, ProductDto productDto) {
        Product product = new Product();
        try {
            if (imageProduct == null) {
                product.setImage(null);
            } else {
                imageUpload.uploadFile(imageProduct);
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }

            List<Meta> metas = new ArrayList<>();
            for (Meta meta : productDto.getMeta()) {
                Meta meta1 = new Meta();
                meta1.setTitle(meta.getTitle());
                meta1.setContent(meta.getContent());
                meta1.setProduct(product);

                metas.add(meta1);
            }
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setCostPrice(productDto.getCostPrice());
            product.setSalePrice(productDto.getSalePrice());
            product.setCategory(productDto.getCategory());
            product.setBrand(productDto.getBrand());
            product.setMeta(metas);

            product.setScreen(productDto.getScreen());
            product.setCpu(productDto.getCpu());
            product.setRam(productDto.getRam());
            product.setStorage(productDto.getStorage());
            product.setVga(productDto.getVga());

            product.set_deleted(false);
            product.set_activated(true);
            return productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product update(MultipartFile imageProduct, ProductDto productDto) {
        try {
            Product productUpdate = productRepository.getReferenceById(productDto.getId());
            if (imageProduct.getBytes().length > 0) {
                if (imageUpload.checkExist(imageProduct)) {
                    productUpdate.setImage(productUpdate.getImage());
                } else {
                    imageUpload.uploadFile(imageProduct);
                    productUpdate.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
                }
            }
            productUpdate.setCategory(productDto.getCategory());
            productUpdate.setBrand(productDto.getBrand());
            productUpdate.setId(productUpdate.getId());
            productUpdate.setName(productDto.getName());
            productUpdate.setDescription(productDto.getDescription());
            productUpdate.setCostPrice(productDto.getCostPrice());
            productUpdate.setSalePrice(productDto.getSalePrice());
            productUpdate.setCurrentQuantity(productDto.getCurrentQuantity());
            productUpdate.setMeta(productDto.getMeta());
            productUpdate.setScreen(productDto.getScreen());
            productUpdate.setCpu(productDto.getCpu());
            productUpdate.setRam(productDto.getRam());
            return productRepository.save(productUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void enableById(Long id) {
        Optional<Product> curr = productRepository.findById(id);
        if(curr.isPresent()){
            Product product = curr.get();
            product.set_activated(true);
            product.set_deleted(false);
            productRepository.save(product);
        }
    }

    @Override
    public void deleteById(Long id) {
        Optional<Product> curr = productRepository.findById(id);
        if(curr.isPresent()) {
//            Product product = curr.get();
//            product.set_deleted(true);
//            product.set_activated(false);
//            productRepository.save(product);
//            cartItemRepository.deleteByProductId(id);
            curr.get().set_deleted(true);
//            curr.get().setBrand(null);
//            curr.get().setCategory(null);
//            productRepository.deleteById(id);
//            productRepository.delete(curr.get());
            productRepository.save(curr.get());
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    @Override
    public ProductDto getById(Long id) {
        ProductDto productDto = new ProductDto();
        Optional<Product> curr = productRepository.findById(id);
        if(curr.isPresent()) {
            Product product = curr.get();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setCategory(product.getCategory());
            productDto.setBrand(product.getBrand());
            productDto.setImage(product.getImage());
            productDto.setScreen(product.getScreen());
            productDto.setCpu(product.getCpu());
            productDto.setRam(product.getRam());
            productDto.setStorage(product.getStorage());
            productDto.setVga(product.getVga());
            productDto.setActivated(product.is_activated());
            productDto.setDeleted(product.is_deleted());
            return productDto;
        }
        return productDto;
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).get();
    }

    @Override
    public List<Product> filterProducts(List<ProductDto> products, String brand, String category) {
        if ( brand==null && category==null || brand.equals("all") && category.equals("all") || brand.isEmpty() && category.isEmpty() || brand.equals("all") && category.isEmpty())
        {
            return products.stream().map(productDto -> productRepository.getReferenceById(productDto.getId())).toList();
        }
        List<Product> productFilter = new ArrayList<>();
        for (ProductDto productDto : products) {
            if (brand != "" && category != "") {
                if (productDto.getBrand().getName().equals(brand) && productDto.getCategory().getName().equals(category)) {
                    productFilter.add(productRepository.getReferenceById(productDto.getId()));
                }
            } else if (brand != "" && category == "") {
                if (productDto.getBrand().getName().equals(brand)) {
                    productFilter.add(productRepository.getReferenceById(productDto.getId()));
                }
            } else if (brand == "" && category != "") {
                if (productDto.getCategory().getName().equals(category)) {
                    productFilter.add(productRepository.getReferenceById(productDto.getId()));
                }
            } else {
                productFilter.add(productRepository.getReferenceById(productDto.getId()));
            }
        }
        return productFilter;
    }

    @Override
    public List<ProductDto> randomProduct() {
        return transferData(productRepository.randomProduct());
    }

    @Override
    public Page<ProductDto> searchProducts(int pageNo, String keyword) {
        List<Product> products = productRepository.findAllByNameOrDescription(keyword);
        List<ProductDto> productDtoList = transferData(products);
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<ProductDto> dtoPage = toPage(productDtoList, pageable);
        return dtoPage;
    }

    @Override
    public Page<ProductDto> getAllProducts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<ProductDto> productDtoLists = this.allProduct();
        Page<ProductDto> productDtoPage = toPage(productDtoLists, pageable);
        return productDtoPage;
    }

    @Override
    public Page<ProductDto> getAllProductsForCustomer(int pageNo) {
        return null;
    }

    @Override
    public List<ProductDto> findAllByCategory(String category) {
        return transferData(productRepository.findAllByCategory(category));
    }

    @Override
    public List<ProductDto> findAllByCategories(List<String> categoryNames) {
        return transferData(productRepository.findAllByCategories(categoryNames));
    }

    @Override
    public List<ProductDto> findAllByBrand(String brand) {
        return transferData(productRepository.findAllByBrand(brand));
    }

    @Override
    public List<ProductDto> findAllByBrands(List<String> brandNames) {
        return transferData(productRepository.findAllByBrands(brandNames));
    }

    @Override
    public List<ProductDto> getProductsByBrandAndCategory(List<String> brandNames, List<String> categories) {
        List<Product> products = productRepository.findByBrandNamesAndCategoryNames(brandNames, categories);
            return transferData(products);
    }

    @Override
    public List<ProductDto> filterHighProducts() {
        return transferData(productRepository.filterHighProducts());
    }

    @Override
    public List<ProductDto> filterLowerProducts() {
        return transferData(productRepository.filterLowerProducts());
    }

    @Override
    public List<ProductDto> filterByPriceRange(int type, List<ProductDto> productDtos) {
//        if (type == 1) {
//            return transferData(productRepository.findByPriceBetween(0, 3000000));
//        } else if (type == 2) {
//            return transferData(productRepository.findByPriceBetween(3000000, 8000000));
//        } else if (type == 3) {
//            return transferData(productRepository.findByPriceBetween(8000000, 15000000));
//        } else if (type == 4) {
//            return transferData(productRepository.findByPriceBetween(15000000, 100000000));
//        }
        List<ProductDto> productDtos1 = new ArrayList<>();

        for (ProductDto productDto : productDtos) {
            if (type == 1) {
                if (productDto.getSalePrice() > 0 && productDto.getSalePrice() <= 3000000) {
                    productDtos1.add(productDto);
                }
            } else if (type == 2) {
                if (productDto.getSalePrice() > 3000000 && productDto.getSalePrice() <= 8000000) {
                    productDtos1.add(productDto);
                }
            } else if (type == 3) {
                if (productDto.getSalePrice() > 8000000 && productDto.getSalePrice() <= 15000000) {
                    productDtos1.add(productDto);
                }
            } else if (type == 4) {
                if (productDto.getSalePrice() > 15000000 && productDto.getSalePrice() <= 100000000) {
                    productDtos1.add(productDto);
                }
            } else {
                productDtos1.add(productDto);
            }
        }
        return productDtos1;
    }

    @Override
    public List<ProductDto> listViewProducts() {
        return transferData(productRepository.listViewProduct());
    }

    @Override
    public List<ProductDto> findByCategoryId(Long id) {
        return transferData(productRepository.getProductByCategoryId(id));
    }

    @Override
    public List<ProductDto> findByBrandId(Long id) {
        return transferData(productRepository.getProductByBrandId(id));
    }

    @Override
    public List<ProductDto> searchProducts(String keyword) {
        return transferData(productRepository.searchProducts(keyword));
    }

    private Page toPage(List list, Pageable pageable) {
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }

    private List<ProductDto> transferData(List<Product> products) {
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setDescription(product.getDescription());
            productDto.setImage(product.getImage());
            productDto.setCategory(product.getCategory());
            productDto.setBrand(product.getBrand());
            productDto.setActivated(product.is_activated());
            productDto.setDeleted(product.is_deleted());
            productDto.setScreen(product.getScreen());
            productDto.setCpu(product.getCpu());
            productDto.setRam(product.getRam());
            productDto.setStorage(product.getStorage());
            productDto.setVga(product.getVga());

            productDtos.add(productDto);
        }
        return productDtos;
    }

    @Override
    public List<Product> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> getProductsByBrand(Brand brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public void deleteAll(List<Product> products) {
        for (Product product : products) {
            product.setBrand(null);
            product.setCategory(null);
            productRepository.delete(product);
        }
    }

    @Override
    public void updateQuantity(ProductDto productDto, String adminUsername) {
        Product product = productRepository.getReferenceById(productDto.getId());
        product.setCurrentQuantity(productDto.getCurrentQuantity());
        productRepository.save(product);
    }
}
