package com.laptopshop.library.service.impl;

import com.laptopshop.library.model.Accessory;
import com.laptopshop.library.repository.AccessoryRepository;
import com.laptopshop.library.service.AccessoryService;
import com.laptopshop.library.utils.ImageUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessoryServiceImpl implements AccessoryService {
    private final AccessoryRepository accessoryRepository;
    private final ImageUpload imageUpload;

    @Override
    public void save(MultipartFile imageAccessory, Accessory accessory) {
        try {
            if (imageAccessory == null) {
                accessory.setImage(null);
            } else {
                imageUpload.uploadFile(imageAccessory);
                accessory.setImage(Base64.getEncoder().encodeToString(imageAccessory.getBytes()));
            }
            accessoryRepository.save(accessory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Accessory> allAccessories() {
        return accessoryRepository.findAll();
    }
}
