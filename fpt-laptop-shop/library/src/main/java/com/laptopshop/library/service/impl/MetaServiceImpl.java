package com.laptopshop.library.service.impl;

import com.laptopshop.library.model.Meta;
import com.laptopshop.library.repository.MetaRepository;
import com.laptopshop.library.service.MetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetaServiceImpl implements MetaService {

    private final MetaRepository metaRepository;

    @Override
    public void saveMeta(Meta meta) {
        metaRepository.save(meta);
    }

    @Override
    public List<Meta> findMetaByProductId(Long id) {
        return metaRepository.findMetaByProductId(id);
    }
}
