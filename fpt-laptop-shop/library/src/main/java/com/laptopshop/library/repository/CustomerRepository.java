package com.laptopshop.library.repository;

import com.laptopshop.library.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByUsername(String username);
    boolean existsByUsername(String email);

    @Query("SELECT c FROM Customer c WHERE c.activated = true AND c.deleted = false")
    List<Customer> findAllByActivatedTrueAndDeletedFalse();

    @Query("SELECT c FROM Customer c WHERE c.deleted = false")
    List<Customer> findAllByDeletedFalse();
}
