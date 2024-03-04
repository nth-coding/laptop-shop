package com.laptopshop.library.dto;

import com.laptopshop.library.model.Brand;
import com.laptopshop.library.model.Category;
import com.laptopshop.library.model.Feedback;
import com.laptopshop.library.model.Meta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private int currentQuantity;
    private double costPrice;
    private double salePrice;
    private String image;
    private Category category;
    private Brand brand;
    private boolean activated;
    private boolean deleted;
    private String currentPage;

    private String screen;
    private String cpu;
    private String ram;
    private String storage;
    private String vga;

    private List<Meta> meta;
    private List<Feedback> feedbacks;

    public String getFormattedSalePrice() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        return decimalFormat.format(salePrice);
    }

    public List<Meta> getMeta() {
        return meta;
    }

    public void setMeta(List<Meta> meta) {
        this.meta = meta;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public String getFormattedCostPrice() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        return decimalFormat.format(costPrice);
    }
}
