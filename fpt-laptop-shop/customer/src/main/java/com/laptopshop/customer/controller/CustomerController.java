package com.laptopshop.customer.controller;

import com.laptopshop.customer.utils.RandomPasswordGenerator;
import com.laptopshop.customer.utils.SendMail;
import com.laptopshop.library.dto.CustomerDto;
import com.laptopshop.library.dto.ForgotPasswordRequest;
import com.laptopshop.library.model.*;
import com.laptopshop.library.service.CityService;
import com.laptopshop.library.service.CustomerService;
import com.laptopshop.library.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final CountryService countryService;
    private final PasswordEncoder passwordEncoder;
    private final CityService cityService;

    private final SendMail sendMail;

    @GetMapping("/profile")
    public String profile(Model model, Principal principal, @ModelAttribute("success") Optional<String> success) {
        if(principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        CustomerDto customer = customerService.getCustomer(username);
        List<Country> countryList = countryService.findAll();
        List<City> cities = cityService.findAll();
        model.addAttribute("customer", customer);
        model.addAttribute("cities", cities);
        model.addAttribute("countries", countryList);
        model.addAttribute("title", "Profile");
        model.addAttribute("page", "Profile");
        success.ifPresent(s -> model.addAttribute("success", s));
        return "customer-information";

    }

    @PostMapping("/update-profile")
    public String updateProfile(@Valid @ModelAttribute("customer") CustomerDto customerDto,
                                BindingResult result,
                                RedirectAttributes attributes,
                                Model model,
                                Principal principal) {
        if(principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        CustomerDto customer = customerService.getCustomer(username);
        List<Country> countryList = countryService.findAll();
        List<City> cities = cityService.findAll();
        model.addAttribute("countries", countryList);
        model.addAttribute("cities", cities);
        if (result.hasErrors()) {
            return "customer-information";
        }
        customerService.update(customerDto);
        CustomerDto customerUpdate = customerService.getCustomer(principal.getName());
        attributes.addFlashAttribute("success", "Update successfully!");
        model.addAttribute("customer", customerUpdate);
        return "redirect:/profile";

    }

    @GetMapping("/change-password")
    public String changePassword(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("title", "Change password");
        model.addAttribute("page", "Change password");
        return "change-password";
    }

    @PostMapping("/do-change-password")
    public String changePass(@RequestParam("password") String password,
                             @RequestParam("newpassword") String newpassword,
                             @RequestParam("confirmPassword") String confirmPassword,
                             RedirectAttributes attributes,
                             Model model,
                             Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            CustomerDto customer = customerService.getCustomer(principal.getName());

// Kiểm tra nếu mật khẩu hiện tại không khớp với mật khẩu đã lưu
            if (!passwordEncoder.matches(password, customer.getPassword())) {
                model.addAttribute("message", "Mật khẩu hiện tại của bạn không chính xác");
                return "change-password";
            } else if (passwordEncoder.matches(newpassword, password)) {
                model.addAttribute("message", "Mật khẩu mới của bạn không thể giống với mật khẩu hiện tại");
                return "change-password";
            } else if (passwordEncoder.matches(newpassword, customer.getPassword())) {
                model.addAttribute("message", "Mật khẩu mới của bạn không thể giống với mật khẩu trước đó");
                return "change-password";
            } else if (!confirmPassword.equals(newpassword)) {
                model.addAttribute("message", "Mật khẩu mới và mật khẩu xác nhận không khớp nhau");
                return "change-password";
            } else {
                // Cập nhật mật khẩu của khách hàng với mật khẩu mới đã được mã hóa
                customer.setPassword(passwordEncoder.encode(newpassword));

                // Lưu thay đổi vào cơ sở dữ liệu
                customerService.changePass(customer);

                // Thêm thông báo thành công vào flash attributes
                attributes.addFlashAttribute("success", "Mật khẩu của bạn đã được thay đổi thành công!");

                // Chuyển hướng đến trang "/profile"
                return "redirect:/profile";
            }
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        model.addAttribute("title", "Forgot Password");
        model.addAttribute("forgotPasswordRequest", new ForgotPasswordRequest());
        return "forgot-password";
    }

    @PostMapping("/do-loss-pass")
    public String forgotPasswordNew(@ModelAttribute("forgotPasswordRequest") ForgotPasswordRequest forgotPasswordRequest, RedirectAttributes redirectAttributes) {
        try {
            String email = forgotPasswordRequest.getEmail();
            CustomerDto customer = customerService.getCustomer(email);

            // Check if customer is null
            if (customer == null) {
                redirectAttributes.addFlashAttribute("error", "No account associated with provided email.");
                return "redirect:/forgot-password";
            }

            RandomPasswordGenerator rpg = new RandomPasswordGenerator();
            String pass = rpg.generateRandomPassword();
            customer.setPassword(passwordEncoder.encode(pass));

            customerService.changePass(customer);

            String bodyEmail = "Mật khẩu mới cho tài khoản " + customer.getUsername() + " là: " + pass;
            sendMail.sendMailRender(customer.getUsername(), "PASSWORD RECOVER", bodyEmail);
            redirectAttributes.addFlashAttribute("success", "Password reset successful. Check your email for the new password.");
        } catch (Exception e) {
            log.error("Error when send password to email: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error occurred while resetting password.");
        }
        return "redirect:/login";
    }
}
