package com.ecommerce.api.controller;


import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final ProductRepository productRepository;

    private CategoryController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    // GET /api/v1/categories → liste toutes les catégories

    @GetMapping
    public ResponseEntity<List<String>> getAll(){
        List<String> categories = productRepository.findAllCategories();
        return ResponseEntity.ok().body(categories);
    }
}
