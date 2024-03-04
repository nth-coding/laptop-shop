package com.laptopshop.library.service.impl;

import com.laptopshop.library.model.*;
import com.laptopshop.library.repository.CustomerRepository;
import com.laptopshop.library.repository.OrderDetailRepository;
import com.laptopshop.library.repository.OrderRepository;
import com.laptopshop.library.service.OrderService;
import com.laptopshop.library.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository detailRepository;
    private final CustomerRepository customerRepository;
    private final ShoppingCartService cartService;

    @Override
    @Transactional
    public Order save(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setCustomer(shoppingCart.getCustomer());
        order.setTax(2);
        order.setTotalPrice(shoppingCart.getTotalPrice());
        order.setAccept(false);
        order.setPaymentMethod("Cash");
        order.setOrderStatus("Pending");
        order.setQuantity(shoppingCart.getTotalItems());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (CartItem item : shoppingCart.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(item.getProduct());
            orderDetail.setQuantity(item.getQuantity());
            detailRepository.save(orderDetail);
            orderDetailList.add(orderDetail);
        }
        order.setOrderDetailList(orderDetailList);
        cartService.deleteCartById(shoppingCart.getId());
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAll(String username) {
        Customer customer = customerRepository.findByUsername(username);
        List<Order> orders = customer.getOrders();
        return orders;
    }

    @Override
    public List<Order> findALlOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(null);
    }


    @Override
    public Order acceptOrder(Long id) {
        Optional<Order> curr = orderRepository.findById(id);
        if(curr.isPresent()){
            Order order = curr.get();
            order.setAccept(true);
            order.setDeliveryDate(new Date());
            order.setOrderStatus("Delivered");
            return orderRepository.save(order);
        }
        return null;
    }

    @Override
    public Order cancelOrder(Long id) {
        Optional<Order> curr = orderRepository.findById(id);
        if(curr.isPresent()){
            Order order = curr.get();
            order.setAccept(true);
            order.setDeliveryDate(new Date());
            order.setOrderStatus("Cancelled");
            return orderRepository.save(order);
        }
        return null;
    }

    @Override
    public Order confirmOrder(Long id) {
        Optional<Order> curr = orderRepository.findById(id);
        if(curr.isPresent()){
            Order order = curr.get();
            order.setAccept(true);
            order.setDeliveryDate(new Date());
            order.setOrderStatus("Confirmed");
            return orderRepository.save(order);
        }
        return null;
    }

    @Override
    public void updateOrder(Order order) {
        Order curr = orderRepository.findById(order.getId()).orElse(null);
        if(curr != null){
            curr.setOrderStatus(order.getOrderStatus());
            curr.setPaymentMethod(order.getPaymentMethod());
            curr.setTotalPrice(order.getTotalPrice());
            curr.setTax(order.getTax());
            curr.setQuantity(order.getQuantity());
            orderRepository.save(curr);
        }
    }


}
