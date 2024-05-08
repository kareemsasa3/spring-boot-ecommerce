package com.kareem.ecommerce.service;

import com.kareem.ecommerce.model.Category;
import com.kareem.ecommerce.model.Product;
import com.kareem.ecommerce.repository.CategoryRepository;
import com.kareem.ecommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private static final String errorMessage = "Category or Product not found (Category ID: {}, Product ID: {})";

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    /**
     * Creates a new category with the specified name and description.
     * @param name The name of the category.
     * @param description A brief description of the category.
     * @return The created category.
     */
    @Transactional
    public Category createCategory(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        return categoryRepository.save(category);
    }

    /**
     * Retrieves all categories.
     * @return A list of all categories.
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Retrieves a category by its ID.
     * @param id The ID of the category to retrieve.
     * @return The category, or null if not found.
     */
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    /**
     * Deletes a category by its ID.
     * @param id The ID of the category to delete.
     */
    @Transactional
    public void deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            logger.warn("Category with ID {} not found for deletion", id);
        }
    }

    /**
     * Adds a product to a category.
     * @param categoryId The ID of the category.
     * @param productId The ID of the product.
     */
    @Transactional
    public void addProductToCategory(Long categoryId, Long productId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalCategory.isPresent() && optionalProduct.isPresent()) {
            Category category = optionalCategory.get();
            Product product = optionalProduct.get();

            if (product.getCategory() != null && !product.getCategory().equals(category)) {
                logger.error("Product with ID {} already belongs to another category", productId);
                throw new RuntimeException("Product already belongs to another category");
            }

            product.setCategory(category);
            category.getProducts().add(product);

            productRepository.save(product);
            categoryRepository.save(category);
        } else {
            logger.error(errorMessage, categoryId, productId);
            throw new RuntimeException("Category or Product not found");
        }
    }

    /**
     * Removes a product from a category.
     * @param categoryId The ID of the category.
     * @param productId The ID of the product.
     */
    @Transactional
    public void removeProductFromCategory(Long categoryId, Long productId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalCategory.isPresent() && optionalProduct.isPresent()) {
            Category category = optionalCategory.get();
            Product product = optionalProduct.get();

            if (product.getCategory() == null || !product.getCategory().equals(category)) {
                logger.error("Product with ID {} is not part of this category", productId);
                throw new RuntimeException("Product is not part of this category");
            }

            product.setCategory(null);
            category.getProducts().remove(product);

            productRepository.save(product);
            categoryRepository.save(category);
        } else {
            logger.error(errorMessage, categoryId, productId);
            throw new RuntimeException("Category or Product not found");
        }
    }

    /**
     * Finds a category by its ID.
     * @param categoryId The ID of the category to find.
     * @return The category, or null if not found.
     */
    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }
}
