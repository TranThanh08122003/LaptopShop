package com.TCL.example.service;

import java.util.List;
import java.util.Optional;

import com.TCL.example.domain.Factory;

public interface FactoryService {
    List<Factory> findAll();
    Optional<Factory> findById(Long id);
    Factory save(Factory factory);
    void deleteById(Long id);
}

