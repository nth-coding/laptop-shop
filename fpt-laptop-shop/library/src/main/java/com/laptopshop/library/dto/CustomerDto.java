package com.laptopshop.library.dto;

import com.laptopshop.library.model.City;
import com.laptopshop.library.model.Country;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    @Size(min = 3, max = 10, message = "First name contains 3-10 characters")
    private String firstName;

    @Size(min = 3, max = 10, message = "Last name contains 3-10 characters")
    private String lastName;
    private String username;
    @Size(min = 3, max = 15, message = "Password contains 3-10 characters")
    private String password;

    @Size(min = 10, max = 15, message = "Phone number contains 10-15 characters")
    private String phoneNumber;

    private String address;
    private String gender;
    private LocalDate birthday;
    private String confirmPassword;
    private City city;
    private String image;
    private Country country;
    private String otp;
    private LocalDateTime otpGeneratedTime;
}
