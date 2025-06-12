package com.TCL.example.controller.admin;

import com.TCL.example.domain.Category;
import com.TCL.example.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/category")
public class CategoryViewController {

    private final CategoryService categoryService;

    public CategoryViewController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "admin/category/show";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("newCategory", new Category());
        return "admin/category/create";
    }

    @PostMapping("/create")
    public String createCategory(
            @Valid @ModelAttribute("newCategory") Category category,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "admin/category/create";
        }
        categoryService.create(category);
        return "redirect:/admin/category";
    }

    // ----- Bổ sung: Hiển thị form cập nhật -----
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.findById(id);
        if (category == null) {
            // Nếu không tìm thấy category thì có thể redirect hoặc báo lỗi
            return "redirect:/admin/category?error=notfound";
        }
        model.addAttribute("categoryForm", category);
        return "admin/category/update"; 
    }

    @PostMapping("/update/{id}")
    public String updateCategory(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("category") Category category,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "admin/category/update";
        }
        categoryService.update(id, category);
        return "redirect:/admin/category";
    }
}

