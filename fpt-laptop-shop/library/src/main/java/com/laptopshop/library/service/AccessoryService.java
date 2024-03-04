package com.laptopshop.library.service;

import com.laptopshop.library.model.Accessory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AccessoryService {
    void save(MultipartFile imageAccessory, Accessory accessory);

    List<Accessory> allAccessories();
}
