package com.kareem.ecommerce.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.kareem.ecommerce.model.Category;
import com.kareem.ecommerce.model.Fandom;
import com.kareem.ecommerce.model.Product;
import com.kareem.ecommerce.model.dto.ProductDTO;
import com.kareem.ecommerce.repository.CategoryRepository;
import com.kareem.ecommerce.repository.FandomRepository;
import com.kareem.ecommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final Cloudinary cloudinary;
    private final CategoryRepository categoryRepository;
    private final FandomRepository fandomRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository, Cloudinary cloudinary, CategoryRepository categoryRepository, FandomRepository fandomRepository) {
        this.productRepository = productRepository;
        this.cloudinary = cloudinary;
        this.categoryRepository = categoryRepository;
        this.fandomRepository = fandomRepository;
    }

    /**
     * Adds a new product to the repository.
     * @param product The product to be added.
     * @param file The image file to be uploaded (can be null).
     * @return The saved product.
     * @throws IOException If there's an error uploading the image.
     */
    @Transactional
    public Product addProduct(Product product, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            Map<?, ?> map = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) map.get("secure_url");
            product.setImageUrl(imageUrl);
        }
        return productRepository.save(product);
    }

    /**
     * Updates an existing product with new data, optionally updating the image.
     * @param id The ID of the product to update.
     * @param updatedProduct The updated product data.
     * @param imageFile An optional image file to update the product's image.
     * @return The updated product, or null if the product does not exist.
     * @throws IOException If there's an error uploading the image.
     */
    @Transactional
    public Product updateProduct(Long id, Product updatedProduct, MultipartFile imageFile) throws IOException {
        Optional<Product> existingProductOptional = productRepository.findById(id);
        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();

            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setDescription(updatedProduct.getDescription());

            return addProduct(existingProduct, imageFile);
        } else {
            logger.warn("Product with ID {} not found for update", id);
            return null;
        }
    }

    /**
     * Retrieves all products from the repository.
     * @return A list of all products.
     */
    public List<ProductDTO> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            return products.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching all products", e);
            throw new RuntimeException("Error fetching all products", e);
        }
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setImageUrl(product.getImageUrl());
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
        }
        if (product.getFandom() != null) {
            dto.setFandomId(product.getFandom().getId());
        }
        return dto;
    }

    /**
     * Retrieves a product by its ID.
     * @param id The product ID.
     * @return An Optional containing the product if found.
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Deletes a product by its ID.
     * @param id The product ID.
     */
    @Transactional
    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            logger.warn("Product with ID {} not found for deletion", id);
        }
    }

    /**
     * Assigns products to a specific category.
     * @param categoryId The ID of the category.
     * @param productIds The IDs of the products to assign.
     */
    @Transactional
    public void assignProductsToCategory(Long categoryId, List<Long> productIds) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new RuntimeException("Category not found: " + categoryId)
        );

        List<Product> products = productRepository.findAllById(productIds);
        for (Product product : products) {
            product.setCategory(category);
        }

        productRepository.saveAll(products);
    }

    /**
     * Finds products by category ID.
     * @param categoryId The category ID.
     * @return A list of products in the specified category.
     */
    public List<Product> findProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> searchProducts(String query) {
        return productRepository.searchByNameOrDescription(query);
    }

    public Optional<Product> updateProduct(Long id, ProductDTO productDTO) {
        logger.info(String.valueOf(productDTO));
        return productRepository.findById(id).map(product -> {
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            product.setDescription(productDTO.getDescription());
            product.setImageUrl(productDTO.getImageUrl());

            if (productDTO.getCategoryId() != null) {
                try {
                    Optional<Category> category = categoryRepository.findById(productDTO.getCategoryId());
                    category.ifPresent(product::setCategory);
                } catch (NumberFormatException e) {
                    logger.error("Invalid categoryId format: {}", productDTO.getCategoryId());
                }
            }

            if (productDTO.getFandomId() != null) {
                try {
                    Optional<Fandom> fandom = fandomRepository.findById(productDTO.getFandomId());
                    fandom.ifPresent(product::setFandom);
                } catch (NumberFormatException e) {
                    logger.error("Invalid fandomId format: {}", productDTO.getFandomId());
                }

            }

            return productRepository.save(product);
        });
    }
}
