package com.laptopshop.customer.controller;

import com.laptopshop.library.dto.ProductDto;
import com.laptopshop.library.model.CartItem;
import com.laptopshop.library.model.Customer;
import com.laptopshop.library.model.ShoppingCart;
import com.laptopshop.library.service.CustomerService;
import com.laptopshop.library.service.ShoppingCartService;
import com.laptopshop.library.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.security.Principal;
import java.util.Set;

@Controller
public class CartController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShoppingCartService cartService;

    @Autowired
    private ProductService productService;

    @GetMapping("/cart")
    public String cart(Model model, Principal principal, HttpSession session){
        if(principal == null){
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        ShoppingCart shoppingCart = customer.getCart();
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            model.addAttribute("check", "No item in your cart");
        }
        session.setAttribute("totalItems", shoppingCart.getTotalItems());
        model.addAttribute("subTotal", shoppingCart.getTotalPrice());
        model.addAttribute("shoppingCart", shoppingCart);
        return "cart";
    }

    @PostMapping("/add-to-cart")
    public String addItemToCart(
            @RequestParam("id") Long productId,
            @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
            Principal principal,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes){

        if(principal == null){
            return "redirect:/login";
        }
        ProductDto product = productService.getById(productId);
        String username = principal.getName();

        if(quantity < 0 ){
            quantity = 0;
        }
        ShoppingCart cart = cartService.addItemToCart(product, quantity, username);
        if(cart == null){
            cart = new ShoppingCart();
        }
        redirectAttributes.addFlashAttribute("success", "Added " + product.getName() + " to cart");
        return "redirect:/cart";
    }


    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=update")
    public String updateCart(@RequestParam("quantity") int quantity,
                             @RequestParam("id") Long productId,
                             Model model,
                             Principal principal
    ){

        if(principal == null){
            return "redirect:/login";
        }else{
            String username = principal.getName();
            ProductDto product = productService.getById(productId);
            ShoppingCart cart = cartService.updateCart(product, quantity, username);

            model.addAttribute("shoppingCart", cart);
            return "redirect:/cart";
        }

    }

    @GetMapping("/update-to-cart")
    public String updateItemToCart(
            @RequestParam("id") Long productId,
            @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
            Principal principal,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes){

        if(principal == null){
            return "redirect:/login";
        }
        ProductDto product = productService.getById(productId);
        String username = principal.getName();

//        if (product.getCurrentQuantity() < quantity) {
//            redirectAttributes.addFlashAttribute("error", "Sorry, we don't have enough " + product.getName() + " in stock");
//            return "redirect:/cart";
//        } else if(quantity < 0 ){
//            redirectAttributes.addFlashAttribute("error", "Sorry, please enter a valid quantity");
//            return "redirect:/cart";
//        }
        Customer customer = customerService.findByUsername(username);
        ShoppingCart shoppingCart = customer.getCart();

        Set<CartItem> cartItemList = shoppingCart.getCartItems();
        CartItem cartItem = find(cartItemList, productId);

        if (cartItem != null) {
            if (cartItem.getQuantity() + quantity > product.getCurrentQuantity()) {
                redirectAttributes.addFlashAttribute("error", "Sorry, we don't have enough " + product.getName() + " in stock");
                return "redirect:/cart";
            } else if (cartItem.getQuantity() + quantity <= 0 ){
                redirectAttributes.addFlashAttribute("error", "Sorry, please enter a valid quantity");
                return "redirect:/cart";
            }
        }

        ShoppingCart cart = cartService.addItemToCart(product, quantity, username);
        if(cart == null){
            cart = new ShoppingCart();
        }
        redirectAttributes.addFlashAttribute("success", "Added " + product.getName() + " to cart");
        return "redirect:/cart";
    }


    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=delete")
    public String deleteItemFromCart(@RequestParam("id") Long productId,
                                     Model model,
                                     Principal principal){
        if(principal == null){
            return "redirect:/login";
        }else{
            String username = principal.getName();
            ProductDto product = productService.getById(productId);
            ShoppingCart cart = cartService.removeItemFromCart(product, username);
            model.addAttribute("shoppingCart", cart);
            return "redirect:/cart";
        }

    }

    private CartItem find(Set<CartItem> cartItems, long productId) {
        if (cartItems == null) {
            return null;
        }
        CartItem cartItem = null;
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                cartItem = item;
            }
        }
        return cartItem;
    }
}