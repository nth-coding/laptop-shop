package com.laptopshop.customer.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Authentication failed";

        if (exception.getMessage().equalsIgnoreCase("Bad credentials")) {
            errorMessage = "Invalid username or password";
        } else if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
            errorMessage = "Tài khoản chưa được kích hoạt. Hãy kích hoạt qua đường dẫn <a href='/shop/resend-otp'>đây</a>";
        } else if (exception.getMessage().equalsIgnoreCase("User account has expired")) {
            errorMessage = "Tài khoản đã hết hạn sử dụng";
        }

        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect("/shop/login");
    }
}