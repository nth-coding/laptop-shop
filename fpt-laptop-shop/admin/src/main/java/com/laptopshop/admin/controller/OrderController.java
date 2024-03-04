package com.laptopshop.admin.controller;

import com.laptopshop.library.dto.ProductDto;
import com.laptopshop.library.model.Order;
import com.laptopshop.library.model.OrderDetail;
import com.laptopshop.library.repository.OrderDetailRepository;
import com.laptopshop.library.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final OrderDetailRepository orderDetailRepository;

    @GetMapping("/orders")
    public String getAll(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            List<Order> orderList = orderService.findALlOrders();
            model.addAttribute("orders", orderList);
            return "orders";
        }
    }

    @GetMapping("/order-detail/{id}")
    public String getOrderDetail(@PathVariable Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            Order order = orderService.findById(id);
            List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderId(id);
            model.addAttribute("order", order);
            model.addAttribute("orderDetails", orderDetails); // add order details to the model
            return "order-detail";
        }
    }



    @RequestMapping(value = "/accept-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String acceptOrder(Long id, RedirectAttributes attributes, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            orderService.acceptOrder(id);
            attributes.addFlashAttribute("success", "Order Accepted");
            return "redirect:/orders";
        }
    }

    @RequestMapping(value = "/cancel-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String cancelOrder(Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            orderService.cancelOrder(id);
            return "redirect:/orders";
        }
    }

    @GetMapping("/update-order/{id}")
    public String updateOrder(@PathVariable Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            Order order = orderService.findById(id);
            model.addAttribute("order", order);
            return "update-order";
        }
    }

    @PostMapping("/update-order/{id}")
    public String updateOrder(@ModelAttribute("order") Order order,
                              RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            orderService.updateOrder(order);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server, please try again!");
        }
        return "redirect:/orders";
    }
}
