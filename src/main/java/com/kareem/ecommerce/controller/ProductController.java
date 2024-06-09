package com.kareem.ecommerce.controller;

import com.kareem.ecommerce.model.Category;
import com.kareem.ecommerce.model.Product;
import com.kareem.ecommerce.model.dto.ProductDTO;
import com.kareem.ecommerce.service.CategoryService;
import com.kareem.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        try {
            List<ProductDTO> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error("Error fetching products: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Endpoint to add a new product.
     * @param product The product details from the request body.
     * @param categoryId The ID of the category to which the product belongs.
     * @param image Optional image file for the product.
     * @return The created product, or an error response.
     */
    @PostMapping
    public ResponseEntity<Product> addProduct(
            @RequestBody Product product, // Product details from the request body
            @RequestParam("categoryId") Long categoryId, // Category ID from the request param
            @RequestParam(value = "image", required = false) MultipartFile image // Optional image
    ) {
        try {
            Category category = categoryService.findCategoryById(categoryId);
            if (category == null) {
                logger.warn("Category with ID {} not found", categoryId);
                return ResponseEntity.badRequest().body(null); // Return bad request if category not found
            }

            product.setCategory(category); // Associate the product with the category

            Product createdProduct = productService.addProduct(product, image); // Add the product with image

            return ResponseEntity.ok(createdProduct); // Return the created product
        } catch (IOException e) {
            logger.error("Error adding product with image: ", e);
            return ResponseEntity.status(500).build(); // Internal server error on exception
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Assigns a list of products to a specified category.
     *
     * @param categoryId The ID of the category to assign products to.
     * @param productIds A list of product IDs to assign to the category.
     * @return A ResponseEntity indicating the result of the operation.
     */
    @PostMapping("/assign-to-category/{categoryId}")
    public ResponseEntity<?> assignProductsToCategory(
            @PathVariable Long categoryId, // Category ID as a path variable
            @RequestBody List<Long> productIds // List of product IDs in the request body
    ) {
        try {
            productService.assignProductsToCategory(categoryId, productIds); // Call the service method
            return ResponseEntity.ok("Products assigned to category successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Handle errors
        }
    }

    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.findProductsByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category-by-product/{productId}")
    public ResponseEntity<Long> getCategoryByProductId(@PathVariable Long productId) {
        Optional<Product> product = productService.getProductById(productId);

        if (product.isPresent() && product.get().getCategory() != null) {
            return ResponseEntity.ok(product.get().getCategory().getId());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String q) {
        return productService.searchProducts(q);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        logger.info("Received request to update product with id: {}", id);
        System.out.println("Hello");
        Optional<Product> updatedProduct = productService.updateProduct(id, productDTO);
        return updatedProduct.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
