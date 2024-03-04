package com.laptopshop.library.repository;

import com.laptopshop.library.model.Customer;
import com.laptopshop.library.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByCustomer_Username(String username);

    List<Feedback> findByProduct_Id(Long productId);
}
