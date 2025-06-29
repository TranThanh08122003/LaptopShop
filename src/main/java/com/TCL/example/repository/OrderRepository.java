package com.TCL.example.repository;

import com.TCL.example.domain.Order;
import com.TCL.example.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByUser(User user);
    List<Order> findByUserId(Long userId);
    Page<Order> findAll(Pageable pageable);

    @Query("SELECT DATE(o.createAt) as date, SUM(d.quantity) as counts " +
            "FROM Order o " +
            "INNER JOIN OrderDetail d ON o.id = d.order.id " +
            "WHERE o.createAt BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(o.createAt)" +
            "ORDER BY DATE(o.createAt) ASC "
    )
    List<Map<String, Object>> getSalesStatistics(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("SELECT p.factory as factory, COUNT(p.factory) as counts " +
            "FROM Order o " +
            "INNER JOIN OrderDetail d ON o.id = d.order.id " +
            "INNER JOIN Product p ON d.product.id = p.id " +
            "WHERE o.createAt BETWEEN :startDate AND :endDate " +
            "GROUP BY (p.factory)")
    List<Map<String, Object>> getSalesStatisticsByFactory(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("SELECT SUM(d.quantity * d.price) as amount " +
            "FROM Order o " +
            "INNER JOIN OrderDetail d ON o.id = d.order.id " +
            "WHERE o.createAt BETWEEN :startDate AND :endDate "
    )
    Optional<Double> getTotalAmountByMonth(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query(" SELECT o " +
            "FROM Order o " +
            "WHERE (?1 IS NULL OR LOWER(o.status) LIKE CONCAT('%',LOWER(?1),'%')) "
    )
    Page<Order> filterOrderByStatus(String status, Pageable pageable);
}
