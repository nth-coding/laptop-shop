package com.laptopshop.customer.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsChecker implements UserDetailsChecker {

    @Override
    public void check(UserDetails toCheck) {
        // Kiểm tra xem tài khoản đã bị xóa hay chưa kích hoạt
        if (!toCheck.isAccountNonLocked()) {
            throw new UsernameNotFoundException("Tài khoản đã bị khoá.");
        }

        if (!toCheck.isEnabled()) {
            throw new UsernameNotFoundException("Tài khoản chưa được kích hoạt.");
        }
    }
}
