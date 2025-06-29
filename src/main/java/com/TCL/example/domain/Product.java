package com.TCL.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "Tên sản phẩm không được để trống")
    private String name;

    @DecimalMin(value = "0.0", inclusive = false , message = "Giá sản phẩm phải lớn hơn 0")
    private double price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> ProductImages;

    @NotNull
    @NotEmpty(message = "Mô tả sản phẩm không được để trống")
    @Column(columnDefinition = "MediumText")
    private String detailDesc;

    @NotEmpty(message = "Mô tả ngắn sản phẩm không được để trống")
    private String shortDesc;

    @Min(value = 1, message = "Số lượng sản phẩm phải lớn hơn hoặc bằng 1")
    private long quantity;

    private long sold;
    private String factory;
    private String target;

    private String processor;
    private String ram;
    private String storage;
    private String display;
    private String resolution;
    @Column(name = "graphic_card")
    private String graphicCard;
    private String weight;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @JsonIgnore
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "Danh mục không được để trống")
    private Category category;
}
