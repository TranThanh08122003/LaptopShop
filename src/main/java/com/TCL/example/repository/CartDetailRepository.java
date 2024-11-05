package com.TCL.example.repository;


import com.TCL.example.domain.Cart;
import com.TCL.example.domain.CartDetail;
import com.TCL.example.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    boolean existsByCartAndProduct(Cart cart, Product product);

    CartDetail findByCartAndProduct(Cart cart, Product product);
}
