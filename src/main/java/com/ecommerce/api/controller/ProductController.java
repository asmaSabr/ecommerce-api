package com.ecommerce.api.controller;

import com.ecommerce.api.dto.ProductRequest;
import com.ecommerce.api.dto.ProductResponse;
import com.ecommerce.api.entity.Product;
import com.ecommerce.api.entity.Seller;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.repository.SellerRepository;
import com.ecommerce.api.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.CacheControl;
import java.util.concurrent.TimeUnit;
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
        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("RESOURCE_NOT_FOUND"));

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

        ProductResponse response = toDto(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<ProductResponse> products = productRepository.findAll(pageable)
                .map(this::toDto);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
                .body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RESOURCE_NOT_FOUND"));

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES).cachePublic())
                .body(toDto(product));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RESOURCE_NOT_FOUND"));

        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        Product updated = productRepository.save(product);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RESOURCE_NOT_FOUND"));

        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }

    // ─── Helper ──────────────────────────────────────────────────────────────
    private ProductResponse toDto(Product product) {
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
    }
}
