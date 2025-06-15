package com.TCL.example.service;

import com.TCL.example.domain.Coupon;
import com.TCL.example.repository.CouponRepository;
import com.TCL.example.service.CouponService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public Optional<Coupon> getCouponById(Long id) {
        return couponRepository.findById(id);
    }

    @Override
    public Coupon saveCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

    @Override
    public Page<Coupon> getCoupons(int page, String activeStatus) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());

        if (activeStatus == null || activeStatus.isEmpty()) {
            return couponRepository.findAll(pageable);
        }

        Boolean active = null;
        if ("true".equalsIgnoreCase(activeStatus)) {
            active = true;
        } else if ("false".equalsIgnoreCase(activeStatus)) {
            active = false;
        }

        if (active == null) {
            return couponRepository.findAll(pageable);
        } else {
            return couponRepository.findByActive(active, pageable);
        }
    }
        @Override
        public Coupon findById(Long id) {
            return couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id " + id));
        }

        @Override
        public void deleteById(Long id) {
            couponRepository.deleteById(id);
        }

            @Override
    public Coupon findByCode(String code) {
        return couponRepository.findByCode(code);
    }

    public boolean applyCoupon(String code) {
    Coupon coupon = couponRepository.findByCode(code);
    if (coupon != null && coupon.isActive() && coupon.getQuantity() > 0) {
        coupon.setQuantity(coupon.getQuantity() - 1);
        couponRepository.save(coupon);
        return true;
    }
    return false;
}
public void save(Coupon coupon) {
    couponRepository.save(coupon);
}
    
}

