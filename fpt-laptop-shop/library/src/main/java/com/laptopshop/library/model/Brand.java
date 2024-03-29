package com.laptopshop.library.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "brands", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long id;

    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_activated")
    private boolean activated;

    @Column(name = "is_deleted")
    private boolean deleted;

    @Column(name = "created_date")
    @CreationTimestamp
    Date createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    Date updatedDate;
}
