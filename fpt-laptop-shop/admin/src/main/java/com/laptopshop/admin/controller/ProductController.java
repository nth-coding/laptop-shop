package com.laptopshop.admin.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laptopshop.library.dto.ProductDto;
import com.laptopshop.library.model.*;
import com.laptopshop.library.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    private final ImportHistoryService importHistoryService;

    private final MetaService metaService;


    @GetMapping("/products")
    public String products(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<ProductDto> products = productService.allProduct();
        model.addAttribute("products", products);
        model.addAttribute("size", products.size());
        model.addAttribute("title", "Manage Products");
        return "products";
    }

    @GetMapping("/products/{pageNo}")
    public String allProducts(@PathVariable("pageNo") int pageNo, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Page<ProductDto> products = productService.getAllProducts(pageNo);
        model.addAttribute("title", "Manage Products");
        model.addAttribute("size", products.getSize());
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return "products";
    }

    @GetMapping("/search-products/{pageNo}")
    public String searchProduct(@PathVariable("pageNo") int pageNo,
                                @RequestParam(value = "keyword") String keyword,
                                Model model, Principal principal
    ) {
        if (principal == null) {
            return "redirect:/login";
        }
        Page<ProductDto> products = productService.searchProducts(pageNo, keyword);
        model.addAttribute("title", "Result Search Products");
        model.addAttribute("size", products.getSize());
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return "product-result";
    }

    @GetMapping("/add-product")
    public String addProductPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("title", "Add Product");
        List<Category> categories = categoryService.findAllByActivatedTrue();
        List<Brand> brands = brandService.findAllByActivatedTrue();
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("productDto", new ProductDto());
        return "add-product";
    }

    @GetMapping("/update-product-quantity")
    public String showUpdateProductQuantityForm(Model model) {
        try {
            // Lấy danh sách sản phẩm để hiển thị trong dropdown
            List<ProductDto> products = productService.products(); // Cần cập nhật phương thức này trong ProductService
            List<ImportHistory> importHistory = importHistoryService.getAllImportHistory();

            model.addAttribute("products", products);
            model.addAttribute("importHistory", importHistory);
            model.addAttribute("title", "Update Product Quantity");

            return "add-product-quantity";
        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý lỗi nếu cần thiết
            return "redirect:/products/0"; // Hoặc chuyển hướng đến trang lỗi
        }
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("productDto") ProductDto product,
                              @RequestParam("imageProduct") MultipartFile imageProduct,
                              @RequestParam("metaList") String metaListJson,
                              RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            ObjectMapper objectMapper = new ObjectMapper();
            List<Meta> metaList = objectMapper.readValue(metaListJson, new TypeReference<List<Meta>>(){});
            product.setMeta(metaList);
            product.setCurrentQuantity(1);
            productService.save(imageProduct, product);
            redirectAttributes.addFlashAttribute("success", "Add new product successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add new product!");
        }
        return "redirect:/products/0";
    }

    @GetMapping("/update-product/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAllByActivatedTrue();
        List<Brand> brands = brandService.findAllByActivatedTrue();
        ProductDto productDto = productService.getById(id);
        model.addAttribute("title", "Add Product");
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("productDto", productDto);
        return "update-product";
    }

    @PostMapping("/update-product/{id}")
    public String updateProduct(@ModelAttribute("productDto") ProductDto productDto,
                                @RequestParam("imageProduct") MultipartFile imageProduct,
                                RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            productService.update(imageProduct, productDto);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server, please try again!");
        }
        return "redirect:/products/0";
    }

    @PostMapping("/update-product-quantity")
    public String updateProductQuantity(@RequestParam("productDto") int productDtoId,
                                        @RequestParam("quantity") String quantity,
                                        RedirectAttributes redirectAttributes, Principal principal, Model model) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            if (quantity.isEmpty()) {
                model.addAttribute("error", "Quantity must not be empty");
                return "redirect:/update-product-quantity";
            }
            String adminUsername = principal.getName();
            int quantityInt = Integer.parseInt(quantity);

            ProductDto productDto = productService.getById((long) productDtoId);
            productDto.setCurrentQuantity(productDto.getCurrentQuantity() + quantityInt);

            productService.updateQuantity(productDto, adminUsername);
            importHistoryService.saveImportHistory(productDto.getId(), quantityInt, adminUsername);

            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server, please try again!");
        }
        return "redirect:/update-product-quantity";
    }

    @RequestMapping(value = "/enable-product", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enabledProduct(Long id, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            productService.enableById(id);
            redirectAttributes.addFlashAttribute("success", "Enabled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Enabled failed!");
        }
        return "redirect:/products/0";
    }

    @RequestMapping(value = "/delete-product", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deletedProduct(Long id, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Deleted failed!");
        }
        return "redirect:/products/0";
    }

    @RequestMapping(value = "/get-feedback", method = RequestMethod.GET)
    public String getProductsFeedback(Long id, RedirectAttributes redirectAttributes, Principal principal, Model model) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            redirectAttributes.addFlashAttribute("success", "Get feedback successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Get feedback failed!");
        }
        return "redirect:/feedback/" + id;
    }
}
