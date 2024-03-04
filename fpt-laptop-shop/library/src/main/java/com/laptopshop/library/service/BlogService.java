package com.laptopshop.library.service;

import com.laptopshop.library.model.Blog;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BlogService {
    Blog save(MultipartFile imageProduct, MultipartFile thumbnail, Blog blog) throws IOException;

    Blog update(MultipartFile imageProduct, MultipartFile thumbnail, Blog blog) throws IOException;

    Blog findById(Long id);

    void deleteById(Long id);

    void enableById(Long id);

    List<Blog> findALl();
}
