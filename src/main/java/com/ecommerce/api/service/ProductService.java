package com.ecommerce.api.service;

import com.ecommerce.api.entity.Product;
import com.ecommerce.api.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //Pagination
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RESOURCE_NOT_FOUND"));
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    // Mise à jour partielle : price & stock uniquement
    public Product updatePriceAndStock(Long id, Double price, Integer stock) {
        Product product = findById(id);
        if (price != null) product.setPrice(price);
        if (stock != null) product.setStock(stock);
        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
