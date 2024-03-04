package com.laptopshop.customer.config;

import com.laptopshop.library.model.Admin;
import com.laptopshop.library.model.Customer;
import com.laptopshop.library.repository.AdminRepository;
import com.laptopshop.library.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username);
        if (customer != null) {
            return customer;
        }

        Admin admin = adminRepository.findByUsername(username);
        if (admin != null) {
            throw new UsernameNotFoundException("Admins are not allowed to login from this endpoint");
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}