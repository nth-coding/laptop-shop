package com.laptopshop.customer.controller;

import com.laptopshop.library.dto.ProductDto;
import com.laptopshop.library.model.Blog;
import com.laptopshop.library.model.Customer;
import com.laptopshop.library.model.ShoppingCart;
import com.laptopshop.library.service.BlogService;
import com.laptopshop.library.service.CustomerService;
import com.laptopshop.library.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final CustomerService customerService;
    private final ProductService productService;
    private final BlogService blogService;

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String home(Model model, Principal principal, HttpSession session) {
        model.addAttribute("title", "Home");
        model.addAttribute("page", "Home");
        List<ProductDto> products = productService.allProduct();
        model.addAttribute("products", products);
        if (principal != null) {
            Customer customer = customerService.findByUsername(principal.getName());
            session.setAttribute("username", customer.getUsername());
            ShoppingCart shoppingCart = customer.getCart();
            if (shoppingCart != null) {
                session.setAttribute("totalItems", shoppingCart.getTotalItems());
            }
        }
        return "home";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("title", "Contact");
        model.addAttribute("page", "Contact");
        return "contact-us";
    }
}
