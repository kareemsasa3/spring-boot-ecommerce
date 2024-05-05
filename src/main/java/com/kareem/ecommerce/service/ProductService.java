package com.kareem.ecommerce.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.kareem.ecommerce.controller.ProductController;
import com.kareem.ecommerce.model.Category;
import com.kareem.ecommerce.model.Product;
import com.kareem.ecommerce.repository.CategoryRepository;
import com.kareem.ecommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    private final Cloudinary cloudinary;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, Cloudinary cloudinary, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.cloudinary = cloudinary;
        this.categoryRepository = categoryRepository;
    }

    // Create operation
    public Product addProduct(Product product, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            Map<?, ?> map = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) map.get("secure_url");
            product.setImageUrl(imageUrl);
        }
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

    public Product updateImageById(Long id, MultipartFile file) throws IOException {
        Product product = productRepository.findById(id).orElse(null);

        if (product != null) {
            try {
                Map<?, ?> map = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                product.setImageUrl((String) map.get("secure_url"));
                return productRepository.save(product);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
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

    public void assignProductsToCategory(Long categoryId, List<Long> productIds) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new RuntimeException("Category not found")
        );

        List<Product> products = productRepository.findAllById(productIds);
        for (Product product : products) {
            product.setCategory(category);
        }

        productRepository.saveAll(products);
    }

    public List<Product> findProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
}
