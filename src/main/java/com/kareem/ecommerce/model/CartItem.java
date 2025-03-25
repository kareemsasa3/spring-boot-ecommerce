package com.kareem.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class CartItem {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    private Product product;

    @Setter
    private int quantity;

    @ManyToOne
    private CustomerOrder customerOrder;

    public void setOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }
}
