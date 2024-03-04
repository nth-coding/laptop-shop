package com.laptopshop.library.service.impl;

import com.laptopshop.library.model.Customer;
import com.laptopshop.library.model.Feedback;
import com.laptopshop.library.model.Product;
import com.laptopshop.library.repository.CustomerRepository;
import com.laptopshop.library.repository.FeedbackRepository;
import com.laptopshop.library.repository.ProductRepository;
import com.laptopshop.library.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    public void saveFeedback(Customer customer, String message, Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (customer != null && product != null) {
            Feedback newFeedback = new Feedback();
            newFeedback.setCustomer(customer);
            newFeedback.setMessage(message);
            newFeedback.setProduct(product);
            feedbackRepository.save(newFeedback);
        }
    }

    @Override
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    @Override
    public void updateFeedback(Long id, String feedback) {
        Feedback existingFeedback = feedbackRepository.findById(id).orElse(null);
        if (existingFeedback != null) {
            existingFeedback.setMessage(feedback);
            feedbackRepository.save(existingFeedback);
        }
    }

    @Override
    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    @Override
    public List<Feedback> findByProductId(Long productId) {
        return feedbackRepository.findByProduct_Id(productId);
    }

    @Override
    public List<Feedback> findByUsername(String username) {
        return feedbackRepository.findByCustomer_Username(username);
    }

    @Override
    public Feedback findById(Long id) {
        return feedbackRepository.findById(id).orElse(null);
    }
}