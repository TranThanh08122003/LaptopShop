package com.TCL.example.repository;


import com.TCL.example.domain.Order;
import com.TCL.example.domain.OrderDetail;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>{
        List<OrderDetail> findByOrder(Order order);
}
