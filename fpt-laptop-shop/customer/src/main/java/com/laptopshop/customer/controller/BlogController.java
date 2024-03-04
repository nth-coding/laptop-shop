package com.laptopshop.customer.controller;

import com.laptopshop.library.model.Blog;
import com.laptopshop.library.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @GetMapping("/blog-detail/{id}")
    public String blogDetail(@PathVariable("id") Long id, Model model) {
        Blog blog = blogService.findById(id);
        model.addAttribute("blog", blog);
        return "blog-detail";
    }

    @GetMapping("/blog")
    public String blog(Model model) {
        model.addAttribute("title", "TIN MỚI");
        model.addAttribute("page", "Blogs");

        List<Blog> blogs = blogService.findALl();
        blogs.stream().forEach(blog -> {
            if (blog.isDeleted()) {
                blogs.remove(blog);
            }
        });
        model.addAttribute("blogs", blogs);
        return "blog-view";
    }

}
