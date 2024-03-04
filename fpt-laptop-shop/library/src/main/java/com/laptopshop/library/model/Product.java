package com.laptopshop.library.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "image"}))
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;
    private String description;
    private int currentQuantity;
    private double costPrice;
    private double salePrice;

    private String screen;
    private String cpu;
    private String ram;
    private String storage;
    private String vga;

    private boolean is_activated;
    private boolean is_deleted;
    private boolean buyWithAccessory = false;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id")
    private Brand brand;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Nullable
    private List<Meta> meta;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Nullable
    private List<Feedback> feedbacks;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_accessory",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "accessory_id")
    )
    @Nullable
    private List<Accessory> accessories;

    @Column(name = "created_date")
    @CreationTimestamp
    Date createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    Date updatedDate;
}
