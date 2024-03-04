package com.laptopshop.library.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Entity
@Table(name = "customers", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class Customer implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_id")
    private Long id;

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String phoneNumber;
    private String address;
    private String gender;

    private LocalDate birthday;

    @Nullable
    private String otp;

    @Nullable
    private LocalDateTime otpGeneratedTime;

    @Column(name = "is_deleted")
    private boolean deleted;

    @Column(name = "is_activated")
    private boolean activated;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", referencedColumnName = "country_id")
    private Country country;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "customer_role", joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private Collection<Role> roles;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private ShoppingCart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;

    @Column(name = "created_date")
    @CreationTimestamp
    Date createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    Date updatedDate;

    public Customer() {
        this.cart = new ShoppingCart();
        this.orders = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", city=" + city.getName() +
                ", country='" + country + '\'' +
                ", roles=" + roles +
                ", cart=" + cart.getId() +
                ", orders=" + orders.size() +
                ", deleted=" + deleted +
                ", activated=" + activated +
                '}';
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Assuming roles is a collection of Role objects and each Role object has a name that represents the authority
        return this.roles.stream()
                .map(role -> (GrantedAuthority) role::getName)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Assuming an account never expires
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Assuming an account is never locked
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Assuming credentials never expire
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.activated && !this.deleted;
    }
}
