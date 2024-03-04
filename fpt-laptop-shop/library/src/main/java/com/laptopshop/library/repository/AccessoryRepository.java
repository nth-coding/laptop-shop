package com.laptopshop.library.repository;

import com.laptopshop.library.model.Accessory;
import com.laptopshop.library.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessoryRepository  extends JpaRepository<Accessory, Long> {
}
