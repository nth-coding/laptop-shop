package com.laptopshop.customer.controller;

import com.laptopshop.library.dto.BrandDto;
import com.laptopshop.library.dto.CategoryDto;
import com.laptopshop.library.dto.ProductDto;
import com.laptopshop.library.model.Brand;
import com.laptopshop.library.model.Category;
import com.laptopshop.library.model.Customer;
import com.laptopshop.library.model.Product;
import com.laptopshop.library.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    private final CategoryService categoryService;

    private final BrandService brandService;

    private final FeedbackService feedbackService;

    private final CustomerService customerService;

    private final MetaService metaService;

    @GetMapping("/menu")
    public String menu(Model model) {
        model.addAttribute("page", "Products");
        model.addAttribute("title", "Menu");
        List<Category> categories = categoryService.findAllByActivatedTrue();
        List<BrandDto> brands = brandService.getBrandsAndSize();
        List<ProductDto> products = productService.products();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        return "index";
    }

    @GetMapping("/product-detail/{id}")
    public String details(@PathVariable("id") Long id, Model model, Principal principal, HttpSession session) {
        ProductDto product = productService.getById(id);
        List<ProductDto> productDtoList = productService.findAllByCategory(product.getCategory().getName());
        model.addAttribute("products", productDtoList);
        model.addAttribute("title", "Product Detail");
        model.addAttribute("page", "Product Detail");
        model.addAttribute("productDetail", product);
        model.addAttribute("feedbacks", feedbackService.findByProductId(id));
        model.addAttribute("metaList", metaService.findMetaByProductId(id));
        if (principal != null)
            model.addAttribute("username", principal.getName());
        return "product-detail";
    }

    @GetMapping("/shop-detail")
    public String shopDetail(Model model) {
        List<CategoryDto> categories = categoryService.getCategoriesAndSize();
        model.addAttribute("categories", categories);
        List<BrandDto> brands = brandService.getBrandsAndSize();
        model.addAttribute("brands", brands);
        List<ProductDto> products = productService.randomProduct();
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("title", "Shop Detail");
        model.addAttribute("page", "Shop Detail");
        model.addAttribute("products", products);
        return "shop-detail";
    }


    @GetMapping("/high-price")
    public String filterHighPrice(Model model) {
        List<CategoryDto> categories = categoryService.getCategoriesAndSize();
        model.addAttribute("categories", categories);
        List<BrandDto> brands = brandService.getBrandsAndSize();
        model.addAttribute("brands", brands);
        List<ProductDto> products = productService.filterHighProducts();
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("title", "Shop Detail");
        model.addAttribute("page", "Shop Detail");
        model.addAttribute("productViews", listView);
        model.addAttribute("products", products);
        return "shop-detail";
    }


    @GetMapping("/lower-price")
    public String filterLowerPrice(Model model) {
        List<CategoryDto> categories = categoryService.getCategoriesAndSize();
        model.addAttribute("categories", categories);
        List<BrandDto> brands = brandService.getBrandsAndSize();
        model.addAttribute("brands", brands);
        List<ProductDto> products = productService.filterLowerProducts();
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("title", "Shop Detail");
        model.addAttribute("page", "Shop Detail");
        model.addAttribute("products", products);
        return "shop-detail";
    }

    @GetMapping("/find-products/{id}")
    public String productsInCategory(@PathVariable("id") Long id, Model model) {
        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        List<ProductDto> productDtos = productService.findByCategoryId(id);
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("categories", categoryDtos);
        model.addAttribute("title", productDtos.get(0).getCategory().getName());
        model.addAttribute("page", "Product-Category");
        model.addAttribute("products", productDtos);
        return "products";
    }

//    @GetMapping("/product-result")
//    public String resultProduct(@RequestParam(value = "keyword") String keyword,
//                                Model model
//    ) {
//        List<ProductDto> products = productService.searchProducts(keyword);
//        model.addAttribute("title", "Result Search Products");
//        model.addAttribute("products", products);
//        return "product-result";
//    }

    @GetMapping("/search-products")
    public String searchProduct(@RequestParam(value = "keyword") String keyword,
                                @RequestParam(name = "category", required = false) String category,
                                @RequestParam(name = "brand", required = false) String brand,
                                Model model) {
        List<ProductDto> products = productService.searchProducts(keyword);
        List<Product> filteredProducts = productService.filterProducts(products, brand, category);
        model.addAttribute("title", "Result Search Products");
        model.addAttribute("products", filteredProducts);
        model.addAttribute("categories", categoryService.findALl());
        model.addAttribute("brands", brandService.findALl());
        return "product-result";
    }

    @GetMapping("/home")
    public String category(@RequestParam(value = "categories", required = false) List<String> categories,
                           @RequestParam(value = "brands", required = false) List<String> brands,
                           @RequestParam(value = "price", required = false) Integer price,
                           Model model) {
//        // Check if categoryName is an empty string and set it to null
//        if ("".equals(categoryName)) {
//            categoryName = null;
//        }
//
//        // Check if brands contains an empty string and remove it
//        if (brands != null && brands.contains("")) {
//            brands.remove("");
//            if (brands.isEmpty()) {
//                brands = null;
//            }
//        }

        List<ProductDto> productDtos;
        if (categories == null) {
            if (brands == null) {
                productDtos = productService.allProduct();
            } else {
                productDtos = productService.findAllByBrands(brands);
            }
        } else {
            if (brands == null) {
                productDtos = productService.findAllByCategories(categories);
            } else {
                productDtos = productService.getProductsByBrandAndCategory(brands, categories);
            }
        }

        List<Brand> brandDtos = brandService.findALl();
        List<Category> categoryDtos = categoryService.findALl();

        int priceValue = 0;
        if (price != null) {
            priceValue = price.intValue();
        } else {
            productDtos = productService.filterByPriceRange(0, productDtos);
        }

        if (priceValue == 1) {
            productDtos = productService.filterByPriceRange(1, productDtos);
        } else if (priceValue == 2) {
            productDtos = productService.filterByPriceRange(2, productDtos);
        } else if (priceValue == 3) {
            productDtos = productService.filterByPriceRange(3, productDtos);
        } else if (priceValue == 4) {
            productDtos = productService.filterByPriceRange(4, productDtos);
        } else {
            productDtos = productService.filterByPriceRange(0, productDtos);
        }

        productDtos.removeIf(ProductDto::isDeleted);

        model.addAttribute("title", "Home");
        model.addAttribute("page", "Product-Category");
        model.addAttribute("products", productDtos);
        model.addAttribute("brands", brandDtos);
        model.addAttribute("categories", categoryDtos);

        return "home-filter";
    }

    @PostMapping("/add-feedback")
    public String addFeedback(@RequestParam(value = "productId") Long productId,
                              @RequestParam(value = "message") String message,
                              Principal principal,
                              HttpServletRequest request,
                              Model model) {
        HttpSession session = request.getSession();
        String referrer = request.getHeader("referer");

        if (principal == null) {
            session.setAttribute("referrer", referrer);
            return "redirect:/login";
        }
        session.removeAttribute("referrer");

        if (message.isEmpty()) {
            model.addAttribute("error", "Message is empty");
            return "redirect:" + referrer;
        } else if (message.length() > 255) {
            model.addAttribute("error", "Message is too long");
            return "redirect:" + referrer;
        }

        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        feedbackService.saveFeedback(customer, message, productId);
        return "redirect:" + referrer;
    }

    @PostMapping("/delete-feedback")
    public String deleteFeedback(@RequestParam(value = "id") Long id, HttpServletRequest request) {
        String referrer = request.getHeader("referer");
        feedbackService.deleteFeedback(id);
        return "redirect:" + referrer;
    }

    @PostMapping("/update-feedback")
    public String updateFeedback(@RequestParam(value = "id") Long id,
                                 @RequestParam(value = "feedback") String feedback,
                                 HttpServletRequest request) {
        String referrer = request.getHeader("referer");
        feedbackService.updateFeedback(id, feedback);
        return "redirect:" + referrer;
    }
}
