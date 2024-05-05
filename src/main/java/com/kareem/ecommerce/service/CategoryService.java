package com.kareem.ecommerce.service;

import com.kareem.ecommerce.model.Category;
import com.kareem.ecommerce.model.Product;
import com.kareem.ecommerce.repository.CategoryRepository;
import com.kareem.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public Category createCategory(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public void addProductToCategory(Long categoryId, Long productId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalCategory.isPresent() && optionalProduct.isPresent()) {
            Category category = optionalCategory.get();
            Product product = optionalProduct.get();

            if (product.getCategory() != null && !product.getCategory().equals(category)) {
                throw new RuntimeException("Product already belongs to another category");
            }

            product.setCategory(category); // Assign the product to the category
            category.getProducts().add(product); // Add product to category's list

            // Save the changes to persist the relationship
            productRepository.save(product); // Save the product to update its category
            categoryRepository.save(category); // Save the category to add the product
        } else {
            throw new RuntimeException("Category or Product not found");
        }
    }

    public void removeProductFromCategory(Long categoryId, Long productId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalCategory.isPresent() && optionalProduct.isPresent()) {
            Category category = optionalCategory.get();
            Product product = optionalProduct.get();

            if (product.getCategory() == null || !product.getCategory().equals(category)) {
                throw new RuntimeException("Product is not part of this category");
            }

            product.setCategory(null); // Unlink the product from the category
            category.getProducts().remove(product); // Remove from category's list

            // Save the changes to persist the updated relationship
            productRepository.save(product); // Save the product to update its category
            categoryRepository.save(category); // Save the category to remove the product
        } else {
            throw new RuntimeException("Category or Product not found");
        }
    }

    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }
}
