package com.TCL.example.service;


import com.TCL.example.domain.Order;
import com.TCL.example.domain.User;
import com.TCL.example.repository.OrderDetailRepository;
import com.TCL.example.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public void handleSaveOrder(Order order) {
        this.orderRepository.save(order);
    }

    public Page<Order> fetchOrders(Pageable pageable) {
        return this.orderRepository.findAll(pageable);
    }

    public Optional<Order> fetchOrderById(long id) {
        return this.orderRepository.findById(id);
    }

    public void updateOrder(Order order) {
        Optional<Order> orderOptional = this.fetchOrderById(order.getId());
        if (orderOptional.isPresent()) {
            Order currentOrder = orderOptional.get();
            currentOrder.setStatus(order.getStatus());
            this.handleSaveOrder(currentOrder);
        }
    }

    public void deleteOrderById(long id) {
        Optional<Order> orderOptional = this.fetchOrderById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            this.orderDetailRepository.deleteAll(order.getOrderDetails());
            this.orderRepository.deleteById(id);
        }
    }

    public List<Order> fetchOrdersByUser(User user) {
        return this.orderRepository.findByUser(user);
    }

    public List<Map<String, Object>> getSalesStatistics(LocalDate startDate, LocalDate endDate) {
        Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(endDate.atStartOfDay());
        return orderRepository.getSalesStatisticsByFactory(startTimestamp, endTimestamp);
    }

    public List<Map<String, Object>> getSalesStatisticsByFactory(LocalDate startDate, LocalDate endDate) {
        Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(endDate.atStartOfDay());
        return orderRepository.getSalesStatisticsByFactory(startTimestamp, endTimestamp);
    }

    public double getTotalAmountByMonth(){
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        double result = orderRepository.getTotalAmountByMonth(Timestamp.valueOf(startDate.atStartOfDay()), Timestamp.valueOf(endDate.atTime(23, 59, 59))).orElse(0.0);
        return result;
    }

    public Page<Order> getAllOrders(String status, Pageable pageable) {
        return this.orderRepository.filterOrderByStatus(status, pageable);
    }
}
