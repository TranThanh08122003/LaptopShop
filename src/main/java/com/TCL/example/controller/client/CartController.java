package com.TCL.example.controller.client;

import com.TCL.example.domain.Coupon;
import com.TCL.example.service.CouponService;
import com.TCL.example.service.CouponServiceImpl;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CartController {

    @Autowired
    private CouponServiceImpl couponService;

    // 1. Xử lý form submit bình thường (redirect về trang /cart)
@PostMapping("/apply-coupon")
public String applyCoupon(
        @RequestParam String couponCode,
        HttpSession session,
        RedirectAttributes redirectAttributes) {

    Coupon coupon = couponService.findByCode(couponCode);

    if (coupon == null || !coupon.isActive()) {
        redirectAttributes.addFlashAttribute("couponMessage", "Mã giảm giá không hợp lệ hoặc đã hết hạn.");
        return "redirect:/cart";
    }

    LocalDate today = LocalDate.now();
    if ((coupon.getStartDate() != null && coupon.getStartDate().isAfter(today)) ||
        (coupon.getEndDate() != null && coupon.getEndDate().isBefore(today))) {
        redirectAttributes.addFlashAttribute("couponMessage", "Mã giảm giá đã hết hạn hoặc chưa đến ngày sử dụng.");
        return "redirect:/cart";
    }

    if (coupon.getQuantity() <= 0) {
        redirectAttributes.addFlashAttribute("couponMessage", "Mã giảm giá đã hết lượt sử dụng.");
        return "redirect:/cart";
    }

    Double totalPrice = (Double) session.getAttribute("totalPrice");
    if (totalPrice == null) totalPrice = 0.0;

    double discountAmount = totalPrice * (coupon.getDiscountValue() / 100.0);
    double finalPrice = totalPrice - discountAmount;

    // Gán thông tin vào session
    session.setAttribute("appliedCoupon", coupon);
    session.setAttribute("discountAmount", discountAmount);
    session.setAttribute("finalPrice", finalPrice);
    session.setAttribute("couponCode", couponCode);

    redirectAttributes.addFlashAttribute("discountAmount", discountAmount);
    redirectAttributes.addFlashAttribute("finalPrice", finalPrice);
    redirectAttributes.addFlashAttribute("couponCode", couponCode);
    redirectAttributes.addFlashAttribute("couponMessage", "Áp dụng mã giảm giá thành công!");

    return "redirect:/cart";
}


    // 2. Xử lý Ajax POST, trả về JSON
    @PostMapping("/api/apply-coupon")
    @ResponseBody
    public ResponseEntity<?> applyCouponAjax(@RequestParam String couponCode, HttpSession session) {
        try {
            Coupon coupon = couponService.findByCode(couponCode);

            if (coupon == null || !coupon.isActive()) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Mã giảm giá không hợp lệ hoặc đã hết hạn."
                ));
            }

            LocalDate today = LocalDate.now();
            if ((coupon.getStartDate() != null && coupon.getStartDate().isAfter(today)) ||
                (coupon.getEndDate() != null && coupon.getEndDate().isBefore(today))) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Mã giảm giá đã hết hạn hoặc chưa đến ngày sử dụng."
                ));
            }

            if (coupon.getQuantity() <= 0) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Mã giảm giá đã hết lượt sử dụng."
                ));
            }

            Object totalObj = session.getAttribute("totalPrice");
            double totalPrice = 0.0;
            if (totalObj instanceof Number) {
                totalPrice = ((Number) totalObj).doubleValue();
            } else if (totalObj instanceof String) {
                try {
                    totalPrice = Double.parseDouble((String) totalObj);
                } catch (NumberFormatException e) {
                    totalPrice = 0.0;
                }
            }

            double discountAmount = totalPrice * (coupon.getDiscountValue() / 100.0);
            double finalPrice = totalPrice - discountAmount;

            // Gán thông tin vào session
            session.setAttribute("appliedCoupon", coupon);
            session.setAttribute("discountAmount", discountAmount);
            session.setAttribute("finalPrice", finalPrice);
            session.setAttribute("couponCode", couponCode);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "discount", discountAmount,
                    "finalPrice", finalPrice,
                    "message", "Áp dụng mã giảm giá thành công!"
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Lỗi server: " + e.getMessage()
            ));
        }
    }




}
