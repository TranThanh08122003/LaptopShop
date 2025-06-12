package com.TCL.example.controller.admin;


import com.TCL.example.domain.Coupon;
import com.TCL.example.service.CouponService;
import java.util.List;

import java.util.stream.Collectors;
import com.TCL.example.domain.DTO.CouponDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping
    public String listCoupons(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String activeStatus,
            Model model) {
Page<Coupon> couponPage = couponService.getCoupons(page, activeStatus);
List<CouponDto> couponDtos = couponPage.getContent().stream()
    .map(CouponDto::new)
    .collect(Collectors.toList());
model.addAttribute("coupons", couponDtos);
model.addAttribute("currentPage", page);
model.addAttribute("totalPages", couponPage.getTotalPages());
model.addAttribute("activeStatus", activeStatus == null ? "" : activeStatus);
return "admin/coupon/list";
    }

    @GetMapping("/edit")
    public String createCouponForm(Model model) {
        model.addAttribute("coupon", new Coupon()); // tạo mới
        return "admin/coupon/edit";
    }

    @GetMapping("/edit/{id}")
    public String editCouponForm(@PathVariable Long id, Model model) {
        Coupon coupon = couponService.findById(id);
        model.addAttribute("coupon", coupon);
        return "admin/coupon/edit";
    }

    // POST create
@PostMapping("/create")
public String createCoupon(@ModelAttribute Coupon coupon,
                           @RequestParam(value = "active", required = false) String active,
                           RedirectAttributes redirectAttributes) {
    coupon.setActive("true".equals(active));
    couponService.saveCoupon(coupon);
    redirectAttributes.addFlashAttribute("successMessage", "Thêm mã giảm giá thành công!");
    return "redirect:/admin/coupon";
}
@PostMapping("/update/{id}")
public String updateCoupon(@PathVariable Long id, 
                           @ModelAttribute Coupon coupon,
                           @RequestParam(value = "active", required = false) String active,
                           RedirectAttributes redirectAttributes) {
    coupon.setId(id);
    coupon.setActive("true".equals(active));
    couponService.saveCoupon(coupon);
    redirectAttributes.addFlashAttribute("successMessage", "Cập nhật mã giảm giá thành công!");
    return "redirect:/admin/coupon";
}

    // Delete coupon
    @GetMapping("/delete/{id}")
    public String deleteCoupon(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        couponService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa mã giảm giá thành công!");
        return "redirect:/admin/coupon";
    }
}

