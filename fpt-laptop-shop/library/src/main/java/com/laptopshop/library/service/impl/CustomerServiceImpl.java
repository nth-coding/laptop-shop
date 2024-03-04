package com.laptopshop.library.service.impl;

import com.laptopshop.library.dto.CustomerDto;
import com.laptopshop.library.model.Customer;
import com.laptopshop.library.model.Customer;
import com.laptopshop.library.repository.CustomerRepository;
import com.laptopshop.library.repository.RoleRepository;
import com.laptopshop.library.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Customer save(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        customer.setUsername(customerDto.getUsername());
        customer.setAddress(customerDto.getAddress());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setGender(customerDto.getGender());
        customer.setBirthday(customerDto.getBirthday());
        customer.setCity(customerDto.getCity());
        customer.setCountry(customerDto.getCountry());
        customer.setOtp(customerDto.getOtp());
        customer.setOtpGeneratedTime(customerDto.getOtpGeneratedTime());
        customer.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));
        return customerRepository.save(customer);
    }

    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public List<Customer> findALl() {
        // find all customer is active and not deleted
        return customerRepository.findAllByDeletedFalse();
    }

    @Override
    public CustomerDto getCustomer(String username) {
        CustomerDto customerDto = new CustomerDto();
        Customer customer = customerRepository.findByUsername(username);
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setUsername(customer.getUsername());
        customerDto.setPassword(customer.getPassword());
        customerDto.setAddress(customer.getAddress());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        customerDto.setCity(customer.getCity());
        customerDto.setCountry(customer.getCountry());
        customerDto.setGender(customer.getGender());
        customerDto.setBirthday(customer.getBirthday());
        return customerDto;
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Customer> curr = customerRepository.findById(id);
        if(curr.isPresent()){
//            Customer customer = curr.get();
//            customer.setActivated(false);
//            customer.setDeleted(true);
//            customerRepository.save(customer);
            customerRepository.deleteById(id);
        } else {
            throw new RuntimeException("Customer not found");
        }
    }

    @Override
    public boolean enableById(Long id) {
        Optional<Customer> curr = customerRepository.findById(id);
        if(curr.isPresent()) {
            Customer customer = curr.get();
            customer.setActivated(!customer.isActivated());
            customer.setDeleted(false);
            customerRepository.save(customer);


            return customer.isActivated();
        }
        return false;
    }

    @Override
    public boolean existsByUsername(String email) {
        return customerRepository.existsByUsername(email);
    }

    @Override
    public Customer changePass(CustomerDto customerDto) {
        Customer customer = customerRepository.findByUsername(customerDto.getUsername());
        customer.setPassword(customerDto.getPassword());
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(CustomerDto dto) {
        Customer customer = customerRepository.findByUsername(dto.getUsername());

        // Update the fields of the existing customer with the new values from the form, but only if the new values are not null
        customer.setFirstName(dto.getFirstName() != null ? dto.getFirstName() : customer.getFirstName());
        customer.setLastName(dto.getLastName() != null ? dto.getLastName() : customer.getLastName());
        customer.setAddress(dto.getAddress() != null ? dto.getAddress() : customer.getAddress());
        customer.setCity(dto.getCity() != null ? dto.getCity() : customer.getCity());
        customer.setCountry(dto.getCountry() != null ? dto.getCountry() : customer.getCountry());
        customer.setPhoneNumber(dto.getPhoneNumber() != null ? dto.getPhoneNumber() : customer.getPhoneNumber());
        customer.setGender(dto.getGender() != null ? dto.getGender() : customer.getGender());
        customer.setBirthday(dto.getBirthday() != null ? dto.getBirthday() : customer.getBirthday());

        return customerRepository.save(customer);
    }

}
