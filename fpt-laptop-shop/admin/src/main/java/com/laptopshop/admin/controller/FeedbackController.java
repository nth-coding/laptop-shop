package com.laptopshop.admin.controller;

import com.laptopshop.library.model.Feedback;
import com.laptopshop.library.model.Order;
import com.laptopshop.library.service.FeedbackService;
import com.laptopshop.library.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping("/feedback/{id}")
    public String getFeedbackWithId(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            List<Feedback> feedbackList = feedbackService.findByProductId(id);
            model.addAttribute("feedbacks", feedbackList);
            return "feedbacks";
        }
    }

    @GetMapping("/feedbacks")
    public String feedbacks(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            List<Feedback> feedbackList = feedbackService.findAll();
            model.addAttribute("feedbacks", feedbackList);
            return "feedbacks";
        }
    }
}