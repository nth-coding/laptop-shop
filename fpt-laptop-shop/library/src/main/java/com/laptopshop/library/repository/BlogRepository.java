package com.laptopshop.library.repository;

import com.laptopshop.library.model.Blog;
import com.laptopshop.library.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepository  extends JpaRepository<Blog, Long> {
}
