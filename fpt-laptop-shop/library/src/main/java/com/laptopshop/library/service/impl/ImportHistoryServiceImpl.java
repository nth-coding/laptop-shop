package com.laptopshop.library.service.impl;

import com.laptopshop.library.model.ImportHistory;
import com.laptopshop.library.repository.ImportHistoryRepository;
import com.laptopshop.library.service.AdminService;
import com.laptopshop.library.service.ImportHistoryService;
import com.laptopshop.library.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportHistoryServiceImpl implements ImportHistoryService {
    private final ProductService productService;
    private final AdminService adminService;

    private final ImportHistoryRepository importHistoryRepository;

    @Override
    public void saveImportHistory(Long productId, int quantity, String adminUsername) {
        ImportHistory importHistory = new ImportHistory();
        importHistory.setProduct(productService.findById(productId));
        importHistory.setQuantity(quantity);
        importHistory.setAdmin(adminService.findByUsername(adminUsername));
        importHistoryRepository.save(importHistory);
    }

    @Override
    public void deleteImportHistory(Long id) {
        importHistoryRepository.deleteById(id);
    }

    @Override
    public ImportHistory getImportHistory(Long id) {
        return importHistoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<ImportHistory> getAllImportHistory() {
        return importHistoryRepository.findAll();
    }
}