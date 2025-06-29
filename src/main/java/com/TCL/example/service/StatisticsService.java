package com.TCL.example.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TCL.example.domain.DTO.DateCountDto;
import com.TCL.example.domain.DTO.FactoryCountDto;
import com.TCL.example.repository.OrderRepository;

public interface StatisticsService {
    List<DateCountDto> getSalesStatistics(LocalDate startDate, LocalDate endDate);
    List<FactoryCountDto> getSalesByFactory(LocalDate startDate, LocalDate endDate);
}

