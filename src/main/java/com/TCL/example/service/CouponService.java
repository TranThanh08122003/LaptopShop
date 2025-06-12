package com.TCL.example.service;
import com.TCL.example.domain.Coupon;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface CouponService {
    List<Coupon> getAllCoupons();
    Optional<Coupon> getCouponById(Long id);
    Coupon saveCoupon(Coupon coupon);
    void deleteCoupon(Long id);
    Page<Coupon> getCoupons(int page, String activeStatus);
    Coupon findById(Long id);
    void deleteById(Long id);
    Coupon findByCode(String code);

    

}

