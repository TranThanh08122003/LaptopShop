package com.TCL.example.repository;

import com.TCL.example.domain.Factory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactoryRepository extends JpaRepository<Factory, Long> {
    Page<Factory> findByActive(boolean active, Pageable pageable);

}
