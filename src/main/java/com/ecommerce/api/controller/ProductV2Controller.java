package com.ecommerce.api.controller;

import com.ecommerce.api.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/products")
public class ProductV2Controller {

    private final ProductService productService;
    public ProductV2Controller(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable) {
        // V2 retourne juste les données, sans les _links HATEOAS
        // (pour montrer la différence entre v1 et v2)
        return ResponseEntity.ok(productService.findAll(pageable));
    }
}