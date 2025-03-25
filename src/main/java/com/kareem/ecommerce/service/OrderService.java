package com.kareem.ecommerce.service;

import com.kareem.ecommerce.model.CustomerOrder;
import com.kareem.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Create operation
    public CustomerOrder createCustomerOrder(CustomerOrder customerOrder) {
        return orderRepository.save(customerOrder);
    }

    // Read operation (Retrieve all orders)
    public List<CustomerOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    // Read operation (Retrieve order by ID)
    public Optional<CustomerOrder> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // Update operation
    public CustomerOrder updateOrder(Long id, CustomerOrder updatedOrder) {
        Optional<CustomerOrder> existingOrderOptional = orderRepository.findById(id);
        if (existingOrderOptional.isPresent()) {
            CustomerOrder existingOrder = existingOrderOptional.get();
            existingOrder.setOrderDate(updatedOrder.getOrderDate()); // Update fields as needed
            existingOrder.setCartItems(updatedOrder.getCartItems());
            // Set other fields as needed
            return orderRepository.save(existingOrder);
        } else {
            // Handle error (order not found)
            return null;
        }
    }

    // Delete operation
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
