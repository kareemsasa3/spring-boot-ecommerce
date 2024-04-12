package com.kareem.ecommerce.service;

import com.kareem.ecommerce.controller.ProductController;
import com.kareem.ecommerce.model.Product;
import com.kareem.ecommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Create operation
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        try {
            return productRepository.findAll();
        } catch (Exception e) {
            logger.error("Error fetching products: {}", e.getMessage());
            throw e;
        }

    }

    // Read operation (Retrieve product by ID)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Update operation
    public Product updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existingProductOptional = productRepository.findById(id);
        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            existingProduct.setName(updatedProduct.getName()); // Update fields as needed
            existingProduct.setPrice(updatedProduct.getPrice());
            // Set other fields as needed
            existingProduct.setDescription(updatedProduct.getDescription());
            return productRepository.save(existingProduct);
        } else {
            // Handle error (product not found)
            return null;
        }
    }

    // Delete operation
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
