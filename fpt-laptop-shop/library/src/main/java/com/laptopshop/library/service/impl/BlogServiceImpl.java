package com.laptopshop.library.service.impl;

import com.laptopshop.library.model.Blog;
import com.laptopshop.library.repository.BlogRepository;
import com.laptopshop.library.service.BlogService;
import com.laptopshop.library.utils.ImageUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;

    private final ImageUpload imageUpload;

    @Override
    public Blog save(MultipartFile imageProduct, MultipartFile thumbnail, Blog blog) throws IOException {
        Blog blog1 = new Blog();
        if (imageProduct == null) {
            blog1.setImage(null);
        } else {
            imageUpload.uploadFile(imageProduct);
            blog1.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
        }
        if (thumbnail == null) {
            blog1.setThumbnailImage(null);
        } else {
            imageUpload.uploadFile(thumbnail);
            blog1.setThumbnailImage(Base64.getEncoder().encodeToString(thumbnail.getBytes()));
        }
        blog1.setName(blog.getName());
        blog1.setDescription(blog.getDescription());
        blog1.setContent(blog.getContent());
        blog1.setAuthor(blog.getAuthor());

        return blogRepository.save(blog1);
    }

    @Override
    public Blog update(MultipartFile imageProduct, MultipartFile thumbnail, Blog blog) throws IOException {
        Blog blogUpdate = blogRepository.getReferenceById(blog.getId());
        if (imageProduct.getBytes().length > 0) {
            if (imageUpload.checkExist(imageProduct)) {
                blogUpdate.setImage(blog.getImage());
            } else {
                imageUpload.uploadFile(imageProduct);
                blogUpdate.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
        }
        if (thumbnail != null && thumbnail.getBytes().length > 0) {
            if (imageUpload.checkExist(thumbnail)) {
                blogUpdate.setThumbnailImage(blog.getThumbnailImage());
            } else {
                imageUpload.uploadFile(thumbnail);
                blogUpdate.setThumbnailImage(Base64.getEncoder().encodeToString(thumbnail.getBytes()));
            }
        }
        blogUpdate.setName(blog.getName());
        blogUpdate.setDescription(blog.getDescription());
        blogUpdate.setContent(blog.getContent());
        blogUpdate.setAuthor(blog.getAuthor());
        return blogRepository.save(blogUpdate);
    }

    @Override
    public Blog findById(Long id) {
        return blogRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        Blog blog = blogRepository.findById(id).orElse(null);
        if (blog != null) {
            blog.setDeleted(true);
            blogRepository.save(blog);
        }
    }

    @Override
    public void enableById(Long id) {
        Blog blog = blogRepository.findById(id).orElse(null);
        if (blog != null) {
            blog.setDeleted(false);
            blogRepository.save(blog);
        }
    }

    @Override
    public List<Blog> findALl() {
        return blogRepository.findAll();
    }
}
