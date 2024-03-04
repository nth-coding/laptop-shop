package com.laptopshop.library.model;

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
@Table(name = "accessories")
public class Accessory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private int quantity;
    private boolean is_activated;
    private boolean is_deleted;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_accessory",
            joinColumns = @JoinColumn(name = "accessory_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    @Column(name = "created_date")
    @CreationTimestamp
    Date createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    Date updatedDate;
}
