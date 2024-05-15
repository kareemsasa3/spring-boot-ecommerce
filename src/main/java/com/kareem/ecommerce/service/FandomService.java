package com.kareem.ecommerce.service;

import com.kareem.ecommerce.model.Fandom;
import com.kareem.ecommerce.model.Product;
import com.kareem.ecommerce.repository.FandomRepository;
import com.kareem.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FandomService {

    private static final Logger logger = LoggerFactory.getLogger(FandomService.class);
    private static final String errorMessage = "Fandom or Product not found (Fandom ID: {}, Product ID: {})";

    private final FandomRepository fandomRepository;
    private final ProductRepository productRepository;

    @Autowired
    public FandomService(FandomRepository fandomRepository, ProductRepository productRepository) {
        this.fandomRepository = fandomRepository;
        this.productRepository = productRepository;
    }

    public List<Fandom> getAllFandoms() {
        return fandomRepository.findAll();
    }

    public Optional<Fandom> getFandomById(Long id) {
        return fandomRepository.findById(id);
    }

    @Transactional
    public Fandom createFandom(Fandom fandom) {
        return fandomRepository.save(fandom);
    }

    public Fandom updateFandom(Long id, Fandom fandomDetails) {
        Optional<Fandom> optionalFandom = fandomRepository.findById(id);
        if (optionalFandom.isPresent()) {
            Fandom existingFandom = optionalFandom.get();
            existingFandom.setName(fandomDetails.getName());
            existingFandom.setDescription(fandomDetails.getDescription());
            return fandomRepository.save(existingFandom);
        } else {
            // Handle the case where the Fandom with the given id doesn't exist
            // You can throw an exception or return null, depending on your design
            return null;
        }
    }

    /**
     * Adds a product to a fandom.
     * @param fandomId The ID of the fandom.
     * @param productId The ID of the product.
     */
    @Transactional
    public void addProductToFandom(Long fandomId, Long productId) {
        Optional<Fandom> optionalFandom = fandomRepository.findById(fandomId);
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalFandom.isPresent() && optionalProduct.isPresent()) {
            Fandom fandom = optionalFandom.get();
            Product product = optionalProduct.get();

            if (product.getFandom() != null && !product.getFandom().equals(fandom)) {
                logger.error("Product with ID {} already belongs to another fandom", productId);
                throw new RuntimeException("Product already belongs to another fandom");
            }

            product.setFandom(fandom);
            fandom.getProducts().add(product);

            productRepository.save(product);
            fandomRepository.save(fandom);
        } else {
            logger.error(errorMessage, fandomId, productId);
            throw new RuntimeException("Category or Product not found");
        }
    }

    public void deleteFandom(Long id) {
        fandomRepository.deleteById(id);
    }
}
