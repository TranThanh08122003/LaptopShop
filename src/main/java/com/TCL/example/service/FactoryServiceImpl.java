package com.TCL.example.service;

import org.springframework.stereotype.Service;

import com.TCL.example.domain.Factory;
import com.TCL.example.repository.FactoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FactoryServiceImpl implements FactoryService {

    private final FactoryRepository factoryRepository;

    public FactoryServiceImpl(FactoryRepository factoryRepository) {
        this.factoryRepository = factoryRepository;
    }

    @Override
    public List<Factory> findAll() {
        return factoryRepository.findAll();
    }

    @Override
    public Optional<Factory> findById(Long id) {
        return factoryRepository.findById(id);
    }

    @Override
    public Factory save(Factory factory) {
        return factoryRepository.save(factory);
    }

    @Override
    public void deleteById(Long id) {
        factoryRepository.deleteById(id);
    }
}

