package com.TCL.example.repository;

import com.TCL.example.domain.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    List<Category> findByNameContainingIgnoreCase(String keyword);
    
}
