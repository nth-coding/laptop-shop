package com.laptopshop.library.service;

import com.laptopshop.library.model.Order;
import com.laptopshop.library.model.ShoppingCart;

import java.util.List;


public interface OrderService {
    Order save(ShoppingCart shoppingCart);

    List<Order> findAll(String username);

    List<Order> findALlOrders();

    Order findById(Long id);

    Order acceptOrder(Long id);

    Order cancelOrder(Long id);

    Order confirmOrder(Long id);

    void updateOrder(Order order);
}
