package com.kareem.ecommerce.service;

import com.kareem.ecommerce.model.CartItem;
import com.kareem.ecommerce.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    public CartItem createCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public Optional<CartItem> getCartItemById(Long id) {
        return cartItemRepository.findById(id);
    }

    public CartItem updateCartItem(Long id, CartItem updatedItem) {
        Optional<CartItem> existingCartItemOptional = cartItemRepository.findById(id);
        if (existingCartItemOptional.isPresent()) {
            CartItem existingCartItem = existingCartItemOptional.get();
            existingCartItem.setProduct(updatedItem.getProduct());
            existingCartItem.setOrder(updatedItem.getCustomerOrder());
            existingCartItem.setQuantity(updatedItem.getQuantity());
            return cartItemRepository.save(existingCartItem);
        } else {
            return null;
        }
    }

    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }
}
