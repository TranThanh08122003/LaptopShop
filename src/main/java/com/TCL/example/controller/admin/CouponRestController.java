package com.TCL.example.controller.admin;


import com.TCL.example.domain.Coupon;
import com.TCL.example.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/coupons")
public class CouponRestController {

    @Autowired
    private CouponService couponService;

    // Lấy danh sách tất cả coupon
    @GetMapping
    public List<Coupon> getAllCoupons() {
        return couponService.getAllCoupons();
    }

    // Lấy 1 coupon theo id
    @GetMapping("/{id}")
    public Optional<Coupon> getCoupon(@PathVariable Long id) {
        return couponService.getCouponById(id);
    }

    // Tạo mới 1 coupon
    @PostMapping
    public Coupon createCoupon(@RequestBody Coupon coupon) {
        return couponService.saveCoupon(coupon);
    }

    // Xóa coupon
    @DeleteMapping("/{id}")
    public void deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
    }
}