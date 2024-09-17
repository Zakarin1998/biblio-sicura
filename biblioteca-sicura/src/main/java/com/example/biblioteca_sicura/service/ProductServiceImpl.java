package com.example.biblioteca_sicura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.biblioteca_sicura.model.Product;
import com.example.biblioteca_sicura.repository.CategoryRepository;
import com.example.biblioteca_sicura.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    @Override
    public void addProduct(Product product) {
        // Validate and ensure all categories exist
        if (product.getCategories() != null) {
            product.getCategories().forEach(category -> {
                if (category.getId() != null && categoryRepository.findById(category.getId()).isEmpty()) {
                    throw new RuntimeException("Category with ID " + category.getId() + " not found");
                }
                // Optionally handle cases where categories are not persisted and need to be
            });
        }

        // Save product
        productRepository.save(product);
    }
}
