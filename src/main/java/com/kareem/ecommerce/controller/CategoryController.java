package com.kareem.ecommerce.controller;

import com.kareem.ecommerce.model.Category;
import com.kareem.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category.getName(), category.getDescription());
    }

    @PatchMapping("/{categoryId}/add-product/{productId}")
    public ResponseEntity<?> addProductToCategory(@PathVariable Long categoryId, @PathVariable Long productId) {
        categoryService.addProductToCategory(categoryId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{categoryId}/remove-product/{productId}")
    public ResponseEntity<?> removeProductFromCategory(
            @PathVariable Long categoryId,
            @PathVariable Long productId
    ) {
        categoryService.removeProductFromCategory(categoryId, productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
