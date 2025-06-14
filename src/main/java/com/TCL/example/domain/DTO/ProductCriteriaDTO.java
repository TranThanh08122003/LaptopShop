package com.TCL.example.domain.DTO;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public class ProductCriteriaDTO {
    private Optional<String> page = Optional.empty();
    private Optional<List<String>> factory = Optional.empty();
    private Optional<List<String>> target = Optional.empty();
    private Optional<List<String>> price = Optional.empty();
    private Optional<String> sort = Optional.empty();
    private Optional<Long> categoryId = Optional.empty();
    public Optional<String> getPage() {
        return page;
    }

    public void setPage(Optional<String> page) {
        this.page = page;
    }

    public Optional<List<String>> getFactory() {
        return factory;
    }

    public void setFactory(Optional<List<String>> factory) {
        this.factory = factory;
    }

    public Optional<List<String>> getTarget() {
        return target;
    }

    public void setTarget(Optional<List<String>> target) {
        this.target = target;
    }

    public Optional<List<String>> getPrice() {
        return price;
    }

    public void setPrice(Optional<List<String>> price) {
        this.price = price;
    }

    public Optional<String> getSort() {
        return sort;
    }

    public void setSort(Optional<String> sort) {
        this.sort = sort;
    }

    public Optional<Long> getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Optional<Long> categoryId) {
        this.categoryId = categoryId;
    }
}
