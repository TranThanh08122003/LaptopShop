package com.TCL.example.domain.DTO;

import com.TCL.example.domain.Coupon;
import java.time.format.DateTimeFormatter;

public class CouponDto {
    private Long id;
    private String code;
    private double discountValue;
    private int quantity;
    private String startDateStr;
    private String endDateStr;
    private boolean active;

    public CouponDto(Coupon coupon) {
        this.id = coupon.getId();
        this.code = coupon.getCode();
        this.discountValue = coupon.getDiscountValue();
        this.quantity = coupon.getQuantity();
        this.active = coupon.isActive();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.startDateStr = coupon.getStartDate() != null ? coupon.getStartDate().format(formatter) : "";
        this.endDateStr = coupon.getEndDate() != null ? coupon.getEndDate().format(formatter) : "";
    }

    // Getters (vì JSP chỉ cần đọc)
    public Long getId() { return id; }
    public String getCode() { return code; }
    public double getDiscountValue() { return discountValue; }
    public int getQuantity() { return quantity; }
    public String getStartDateStr() { return startDateStr; }
    public String getEndDateStr() { return endDateStr; }
    public boolean isActive() { return active; }
}