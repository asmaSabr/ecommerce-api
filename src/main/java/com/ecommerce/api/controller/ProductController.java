package com.ecommerce.api.controller;

import com.ecommerce.api.dto.ProductRequest;
import com.ecommerce.api.dto.ProductResponse;
import com.ecommerce.api.entity.Product;
import com.ecommerce.api.entity.Seller;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.repository.SellerRepository;
import com.ecommerce.api.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    public ProductController(ProductRepository productRepository, SellerRepository sellerRepository) {
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        // Charger le Seller à partir de l’ID
        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        // Mapper le DTO vers l’entité
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCurrency(request.getCurrency());
        product.setStock(request.getStock());
        product.setImages(request.getImages());
        product.setThumbnail(request.getThumbnail());
        product.setCategory(request.getCategory());
        product.setSeller(seller);

        Product saved = productRepository.save(product);

        // Mapper l’entité vers le DTO de réponse
        ProductResponse response = new ProductResponse();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setPrice(saved.getPrice());
        response.setCurrency(saved.getCurrency());
        response.setStock(saved.getStock());
        response.setCategory(saved.getCategory());
        response.setSellerId(saved.getSeller().getId());
        response.setCreatedAt(saved.getCreatedAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    //Pagination
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        Page<ProductResponse> products = productRepository.findAll(pageable)
                .map(product -> {
                    ProductResponse dto = new ProductResponse();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice());
                    dto.setCurrency(product.getCurrency());
                    dto.setStock(product.getStock());
                    dto.setCategory(product.getCategory());
                    dto.setSellerId(product.getSeller().getId());
                    dto.setCreatedAt(product.getCreatedAt());
                    return dto;
                });

        return ResponseEntity.ok(products);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductResponse dto = new ProductResponse();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setCurrency(product.getCurrency());
        dto.setStock(product.getStock());
        dto.setCategory(product.getCategory());
        dto.setSellerId(product.getSeller().getId());
        dto.setCreatedAt(product.getCreatedAt());

        return ResponseEntity.ok(dto);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Seuls price et stock sont modifiables
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        Product updated = productRepository.save(product);

        ProductResponse dto = new ProductResponse();
        dto.setId(updated.getId());
        dto.setName(updated.getName());
        dto.setPrice(updated.getPrice());
        dto.setCurrency(updated.getCurrency());
        dto.setStock(updated.getStock());
        dto.setCategory(updated.getCategory());
        dto.setSellerId(updated.getSeller().getId());
        dto.setCreatedAt(updated.getCreatedAt());

        return ResponseEntity.ok(dto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }


}

