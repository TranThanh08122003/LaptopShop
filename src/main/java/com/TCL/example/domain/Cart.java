package com.TCL.example.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Min(value = 0)
    private int sum;

    // user_id
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // cart_detail_id
    @OneToMany(mappedBy = "cart")
    private List<CartDetail> cartDetails;

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", sum=" + sum +
                ", userId=" + (user != null ? user.getId() : "null") +
                ", cartDetailsCount=" + (cartDetails != null ? cartDetails.size() : 0) +
                '}';
    }
}
