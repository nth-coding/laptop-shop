package com.laptopshop.library.service;

import com.laptopshop.library.model.Meta;

import java.util.List;

public interface MetaService {
    void saveMeta(Meta meta);

    List<Meta> findMetaByProductId(Long id);
}
