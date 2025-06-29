package com.TCL.example.service;


import com.TCL.example.domain.*;

import com.TCL.example.domain.DTO.RegisterDTO;
import com.TCL.example.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;


    public Page<User> getAllUser(String fullName, String roleDes,Pageable pageable) {
        return this.userRepository.filterUserByNameAndEmailAndRoleDes(fullName, roleDes, pageable);
    }

    public List<User> getListUser() {
        return this.userRepository.findAll();
    }

    public List<User> getAllUserByEmail(String email) {
        return this.userRepository.findOneByEmail(email);
    }

    public User getUserById(long id) {
        return this.userRepository.findFirstById(id);
    }
@Transactional
public void deleteAUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

    // ✅ Xóa từng OrderDetail trước
    List<Order> orders = orderRepository.findByUserId(userId);
    if (!orders.isEmpty()) {
        for (Order order : orders) {
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);
            if (!orderDetails.isEmpty()) {
                orderDetailRepository.deleteAll(orderDetails);
            }
        }

        // Sau khi xóa orderDetail thì mới được xóa order
        orderRepository.deleteAll(orders);
    }

    // ✅ Xử lý xóa giỏ hàng như trước
    Cart cart = user.getCart();
    if (cart != null) {
        List<CartDetail> details = cart.getCartDetails();
        if (details != null) {
            cartDetailRepository.deleteAll(details);
        }
        cartRepository.delete(cart);
    }

    // ✅ Xóa cuối cùng là user
    userRepository.delete(user);
}

    public Role getRoleByName(String name) {
        return this.roleRepository.findByName(name);
    }

    public User handleSaveUser(User user) {
        User savedUser = this.userRepository.save(user);
        return savedUser;
    }

    public User registerDTOtoUser(RegisterDTO registerDTO) {
        User user = new User();
        user.setFullName(registerDTO.getFirstName() + " " + registerDTO.getLastName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        return user;
    }

    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public long countUsers() {
        return this.userRepository.count();
    }
    public long countOrders() {
        return this.orderRepository.count();
    }

    public long countProducts() {
        return this.productRepository.count();
    }

    public void updatePassword(String email, String password) {
        this.userRepository.updatePassword(email, password);
    }
}
