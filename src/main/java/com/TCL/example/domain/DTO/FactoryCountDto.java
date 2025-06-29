package com.TCL.example.domain.DTO;

public class FactoryCountDto {
    private String factory;
    private Long counts;

    public FactoryCountDto(String factory, Long counts) {
        this.factory = factory;
        this.counts = counts;
    }

    public String getFactory() {
        return factory;
    }

    public Long getCounts() {
        return counts;
    }
}

