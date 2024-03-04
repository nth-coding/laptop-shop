package com.laptopshop.admin.controller;

import com.laptopshop.library.dto.ProductDto;
import com.laptopshop.library.model.Accessory;
import com.laptopshop.library.model.Brand;
import com.laptopshop.library.model.Category;
import com.laptopshop.library.service.AccessoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.projection.Accessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccessoryController {

    private final AccessoryService accessoryService;

    @GetMapping("/accessories")
    public String accessories(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<Accessory> accessories = accessoryService.allAccessories();
        model.addAttribute("accessories", accessories);
        model.addAttribute("title", "Manage Accessories");
        model.addAttribute("size", accessories.size());
        return "accessories";
    }

    @GetMapping("/add-accessory")
    public String addAccessory(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("title", "Add Accessory");
        model.addAttribute("accessory", new Accessory());
        return "add-accessory";
    }

    @PostMapping("/save-accessory")
    public String saveAccessory(@ModelAttribute("accessory") Accessory accessory,
                                @RequestParam("picture") MultipartFile imageAccessory,
                                RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            accessoryService.save(imageAccessory, accessory);
            redirectAttributes.addFlashAttribute("success", "Add new accessory successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add new accessory!");
        }
        return "redirect:/accessories";
    }
}
