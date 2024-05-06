package com.kareem.ecommerce.controller;

import com.kareem.ecommerce.model.Category;
import com.kareem.ecommerce.model.Product;
import com.kareem.ecommerce.service.CategoryService;
import com.kareem.ecommerce.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    public List<Product> getAllProducts() {
        try {
            return productService.getAllProducts();
        } catch (Exception e) {
            logger.error("Error fetching products: {}", e.getMessage());
            throw e; // rethrow the exception to let the Spring handle it
        }
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("categoryId") Long categoryId, // New parameter for category ID
            @RequestBody MultipartFile image // Image file for product
    ) {
        try {
            // Find the specified category
            Category category = categoryService.findCategoryById(categoryId);
            if (category == null) {
                return ResponseEntity.badRequest().body(null); // Category not found
            }

            // Create a new product and set its attributes
            Product newProduct = new Product(name, description, price);
            newProduct.setCategory(category); // Associate the product with the category

            // Add the product to the service, with image processing
            Product createdProduct = productService.addProduct(newProduct, image);

            return ResponseEntity.ok(createdProduct); // Return the created product
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build(); // Internal server error
        }
    }

    @PutMapping
    public ResponseEntity<Product> updateImageById(@RequestParam("id") Long id, @RequestBody MultipartFile image) {
        try {
            Product updatedProduct = productService.updateImageById(id, image);
            if (updatedProduct != null) {
                return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
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
}
