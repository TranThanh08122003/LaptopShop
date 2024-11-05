package com.TCL.example.repository;

import com.TCL.example.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long>{
    void deleteAllByProductId(Long productId);
}
