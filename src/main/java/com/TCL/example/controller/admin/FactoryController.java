package com.TCL.example.controller.admin;

import com.TCL.example.domain.Factory;
import com.TCL.example.repository.FactoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/factory")
public class FactoryController {

    @Autowired
    private FactoryRepository factoryRepository;

    @GetMapping
    public String listFactories(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(required = false) String activeStatus,
                                Model model) {

        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<Factory> factoryPage;

        if ("true".equals(activeStatus)) {
            factoryPage = factoryRepository.findByActive(true, pageable);
        } else if ("false".equals(activeStatus)) {
            factoryPage = factoryRepository.findByActive(false, pageable);
        } else {
            factoryPage = factoryRepository.findAll(pageable);
        }

        model.addAttribute("factories", factoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", factoryPage.getTotalPages());
        model.addAttribute("activeStatus", activeStatus);

        return "admin/factory/list";
    }
 // 🆕 Hàm tạo
    @GetMapping("/create")
    public String createFactoryForm(Model model) {
        model.addAttribute("factory", new Factory());
        return "admin/factory/form";
    }

    // 🆕 Hàm cập nhật
    @GetMapping("/update/{id}")
    public String updateFactoryForm(@PathVariable Long id, Model model) {
        Factory factory = factoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy factory ID: " + id));
        model.addAttribute("factory", factory);
        return "admin/factory/form";
    }

    // 🆕 Hàm lưu
    @PostMapping("/save")
    public String saveFactory(@ModelAttribute("factory") Factory factory) {
        factoryRepository.save(factory);
        return "redirect:/admin/factory";
    }

    // 🆕 Hàm xoá
    @GetMapping("/delete/{id}")
    public String deleteFactory(@PathVariable Long id) {
        factoryRepository.deleteById(id);
        return "redirect:/admin/factory";
    }
}
