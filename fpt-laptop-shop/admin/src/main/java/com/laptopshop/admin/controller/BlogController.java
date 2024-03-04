package com.laptopshop.admin.controller;

import com.laptopshop.library.dto.ProductDto;
import com.laptopshop.library.model.Blog;
import com.laptopshop.library.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @GetMapping("/blogs")
    public String blogs(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        model.addAttribute("title", "Manage Blog");
        List<Blog> blogs = blogService.findALl();
        model.addAttribute("blogs", blogs);
        model.addAttribute("size", blogs.size());
        model.addAttribute("blogNew", new Blog());
        return "blogs";
    }

    @PostMapping("/save-blog")
    public String save(@ModelAttribute("blogNew") Blog blog,
                       @RequestParam("picture") MultipartFile imageProduct,
                       @RequestParam("thumbnail") MultipartFile thumbnail,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        try {
            blogService.save(imageProduct, thumbnail, blog);
            model.addAttribute("blogNew", blog);
            redirectAttributes.addFlashAttribute("success", "Add successfully!");
        } catch (DataIntegrityViolationException e1) {
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Exception at adding new blog!");
        } catch (Exception e2) {
            e2.printStackTrace();
            model.addAttribute("blogNew", blog);
            redirectAttributes.addFlashAttribute("error",
                    "Error server");
        }
        return "redirect:/blogs";
    }

    @RequestMapping(value = "/findByBlogId", method = {RequestMethod.PUT, RequestMethod.GET})
    @ResponseBody
    public Optional<Blog> findByBlogId(Long id) {
        return Optional.ofNullable(blogService.findById(id));
    }

    @GetMapping("/update-blog/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Blog blog = blogService.findById(id);
        model.addAttribute("blog", blog);
        return "update-blog";
    }

    @PostMapping("/update-blog/{id}")
    public String update(@ModelAttribute("blog") Blog blog,
                         @RequestParam("imageBlog") MultipartFile imageProduct,
                         @RequestParam("thumbnail") MultipartFile thumbnail,
                         RedirectAttributes redirectAttributes,
                         Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            blogService.update(imageProduct, thumbnail, blog);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (DataIntegrityViolationException e1) {
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Exception at editing new blog!");
        } catch (Exception e2) {
            e2.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error from server or duplicate name of blog, please check again!");
        }
        return "redirect:/blogs";
    }

    @RequestMapping(value = "/delete-blog", method = {RequestMethod.GET, RequestMethod.PUT})
    public String delete(Long id, RedirectAttributes redirectAttributes) {
        try {
            blogService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Deleted successfully!");
        } catch (DataIntegrityViolationException e1) {
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of blog, please check again!");
        } catch (Exception e2) {
            e2.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server");
        }
        return "redirect:/blogs";
    }

    @RequestMapping(value = "/enable-blog", method = {RequestMethod.GET, RequestMethod.PUT})
    public String enable(Long id, RedirectAttributes redirectAttributes) {
        try {
            blogService.enableById(id);
            redirectAttributes.addFlashAttribute("success", "Enabled successfully!");
        } catch (DataIntegrityViolationException e1) {
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of blog, please check again!");
        } catch (Exception e2) {
            e2.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server");
        }
        return "redirect:/blogs";
    }
}