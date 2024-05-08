package com.kareem.ecommerce.controller;

import com.kareem.ecommerce.model.Product;
import com.kareem.ecommerce.service.ProductService;
import com.kareem.ecommerce.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public AdminController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    /**
     * Endpoint to add a new product.
     * @param product The product to add.
     * @param image The image for the product (optional).
     * @return A response indicating success or failure.
     */
    @PostMapping("/add-product")
    public ResponseEntity<Void> addProduct(@RequestBody Product product, @RequestParam(required = false) MultipartFile image) {
        try {
            productService.addProduct(product, image);
            return ResponseEntity.ok().build(); // Return success status
        } catch (IOException e) {
            logger.error("Error adding product: ", e);
            return ResponseEntity.status(500).build(); // Return internal server error on exception
        }
    }

    /**
     * Endpoint to update an existing product.
     * @param id The ID of the product to update.
     * @param updatedProduct The updated product data.
     * @param image The new image for the product (optional).
     * @return The updated product, or an error response.
     */
    @PutMapping("/update-product/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct, @RequestParam(required = false) MultipartFile image) {
        try {
            Product product = productService.updateProduct(id, updatedProduct, image);
            if (product == null) {
                return ResponseEntity.status(404).body(null); // Return not found status
            }
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            logger.error("Error updating product: ", e);
            return ResponseEntity.status(500).body(null); // Return internal server error on exception
        }
    }
}
