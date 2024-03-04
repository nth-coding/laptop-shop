package com.laptopshop.customer.config;

import com.laptopshop.library.model.Customer;
import com.laptopshop.library.repository.CustomerRepository;
import com.laptopshop.library.service.CustomerService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class AccountStatusFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails customer = (UserDetails) authentication.getPrincipal();


            if (customer != null && !customer.isEnabled()) {
                // Redirect or throw exception if the account is disabled
                throw new ServletException("Account is deleted or unauthorized");
            }
        }

        filterChain.doFilter(request, response);
    }
}