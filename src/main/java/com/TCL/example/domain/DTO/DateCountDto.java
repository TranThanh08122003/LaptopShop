package com.TCL.example.domain.DTO;

import java.time.LocalDate;


import java.sql.Date;

public class DateCountDto {
    private Date date;
    private Long counts;

    public DateCountDto(Date date, Long counts) {
        this.date = date;
        this.counts = counts;
    }

    public Date getDate() {
        return date;
    }

    public Long getCounts() {
        return counts;
    }
}


