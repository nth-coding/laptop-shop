package com.laptopshop.library.service;

import com.laptopshop.library.model.Customer;
import com.laptopshop.library.model.Feedback;

import java.util.List;

public interface FeedbackService {
    void saveFeedback(Customer customer, String message, Long productId);

    void deleteFeedback(Long id);

    void updateFeedback(Long id, String feedback);

    List<Feedback> findAll();

    List<Feedback> findByProductId(Long productId);

    List<Feedback> findByUsername(String username);

    Feedback findById(Long id);
}
