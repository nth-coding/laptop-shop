package com.laptopshop.customer.controller;

import com.laptopshop.customer.config.CustomerConfiguration;
import com.laptopshop.customer.utils.OtpUtil;
import com.laptopshop.customer.utils.SendMail;
import com.laptopshop.library.dto.CustomerDto;
import com.laptopshop.library.model.City;
import com.laptopshop.library.model.Customer;
import com.laptopshop.library.repository.CustomerRepository;
import com.laptopshop.library.service.CustomerService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OtpUtil otpUtil;
    private final SendMail sendMail;

    @Autowired
    private CustomerConfiguration customerConfiguration;


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("title", "Login Page");
        model.addAttribute("page", "Home");

        return "login";
    }


    @GetMapping("/register")
    public String register(Model model, @ModelAttribute("error") Optional<String> error) {
        model.addAttribute("title", "Register");
        model.addAttribute("page", "Register");
        model.addAttribute("customerDto", new CustomerDto());
        error.ifPresent(e -> model.addAttribute("error", e));
        return "register";
    }

    @GetMapping("/confirm-otp")
    public String confirmOtpPage(@RequestParam(required = false) String username, @RequestParam(required = false) String otp, Model model) {
//        Customer customer = customerService.findByUsername(username);
//        if (customer == null) {
//            model.addAttribute("error", "Invalid username!");
//            return "login";
//        }
        model.addAttribute("username", username);
        model.addAttribute("otp", otp);
        return "confirm-otp";
    }

    @GetMapping("/resend-otp")
    public String resendOtpPage(@RequestParam(required = false) String username,  Model model) {
        model.addAttribute("username", username);
        return "resend-otp";
    }

//    @PostMapping("/do-login")
//    public String doLogin(@RequestParam String username, @RequestParam String password, Model model) throws Exception {
//        Customer customer = customerService.findByUsername(username);
//        if (customer == null) {
//            model.addAttribute("error", "Invalid username!");
//            return "login";
//        }
//        if (!customer.isActivated()) {
//            String otp = otpUtil.generateOTP();
//            try {
//                sendMail.sendOtpMail(username, otp);
//                customer.setOtp(otp);
//                customer.setOtpGeneratedTime(LocalDateTime.now());
//                customerRepository.save(customer);
//            } catch (MessagingException e) {
//                e.printStackTrace();
//                model.addAttribute("error", "Failed to send OTP. Please try again.");
//                return "login";
//            }
//            return String.format("redirect:http://localhost:8020/shop/confirm-otp?username=%s&otp=%s", username, "");
//        }
//        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(username, password);
//        // Check if the provided password matches the stored password
//        if (passwordEncoder.matches(password, customer.getPassword())) {
//            // Authenticate the user
////            Authentication auth = customerConfiguration.authenticationManagerBean().authenticate(authReq);
//            // Set the authentication in the SecurityContextHolder
////            SecurityContextHolder.getContext().setAuthentication(auth);
//            return "redirect:/index";
//        } else {
//            model.addAttribute("error", "Password is incorrect!");
//            return "login";
//        }
//    }

    @PostMapping("/do-register")
    public String registerCustomer(@RequestParam String firstName,
                                      @RequestParam String lastName,
                                        @RequestParam String username,
                                          @RequestParam String phoneNumber,
                                            @RequestParam String address,
//                                              @RequestParam City city,
//                                                @RequestParam String country,
                                                    @RequestParam String gender,
                                   @RequestParam String birthday,
                                                      @RequestParam String password,
                                                        @RequestParam String confirmPassword,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        try {
            if (customerService.findByUsername(username) != null) {
                model.addAttribute("error", "Email has been register!");
                return "register";
            }
            LocalDate parsedBirthday = LocalDate.parse(birthday);

            CustomerDto customerDto = new CustomerDto();
            customerDto.setFirstName(firstName);
            customerDto.setLastName(lastName);
            customerDto.setUsername(username);
            customerDto.setGender(gender);
            customerDto.setBirthday(parsedBirthday);
            customerDto.setPassword(password);
            customerDto.setConfirmPassword(confirmPassword);

            customerDto.setPhoneNumber(phoneNumber);
            customerDto.setAddress(address);
//            customerDto.setCity(city);
//            customerDto.setCountry(country);

            String otp = otpUtil.generateOTP();
            if (password.equals(confirmPassword)) {
                sendMail.sendOtpMail(username, otp);
                customerDto.setOtp(otp);
                customerDto.setOtpGeneratedTime(LocalDateTime.now());

                customerService.save(customerDto);
//                model.addAttribute("success", "Register successfully!");

                redirectAttributes.addFlashAttribute("info", "A mail with the new password has been sent to your email.");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("error", "Confirm password is incorrect!");
                return "register";
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Server is error, try again later!");
        }
        return "register";
    }

    @PostMapping("/do-confirm-otp")
    public String confirmOtp(@RequestParam String username,
                             @RequestParam String otp,
                             Model model) {
        Customer customer = customerService.findByUsername(username);
        if (customer == null) {
            model.addAttribute("error", "Invalid username!");
            return "login";
        }
        if (otpUtil.validateOTP(customer, otp, customer.getOtpGeneratedTime())) {
            customer.setActivated(true);
            customerRepository.save(customer);
            model.addAttribute("success", "OTP verified successfully !");
            return "login";
        } else if (!customer.getOtp().equals(otp)) {
            model.addAttribute("error", "OTP is incorrect!");
            return "confirm-otp";
        } else {
            model.addAttribute("error", "OTP is expired!");
            return "confirm-otp";
        }
    }

    @PostMapping("/do-resend-otp")
    public String resendOtp(@RequestParam String username,
                            HttpServletRequest request,
                            Model model) throws MessagingException {
        Customer customer = customerService.findByUsername(username);
        if (customer == null) {
            model.addAttribute("error", "Invalid username!");
            return "login";
        }
        String otp = otpUtil.generateOTP();
        sendMail.sendOtpMail(username, otp);
        customer.setOtp(otp);
        customer.setOtpGeneratedTime(LocalDateTime.now());
        customerRepository.save(customer);
        model.addAttribute("success", "OTP resent successfully !");
        request.getSession().removeAttribute("errorMessage");
        return "confirm-otp";
    }
}
