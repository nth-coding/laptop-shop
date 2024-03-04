package com.laptopshop.library.service;

import com.laptopshop.library.model.ImportHistory;

import java.util.List;

public interface ImportHistoryService {
    void saveImportHistory(Long productId, int quantity, String adminUsername);

    void deleteImportHistory(Long id);

    ImportHistory getImportHistory(Long id);

    List<ImportHistory> getAllImportHistory();
}
