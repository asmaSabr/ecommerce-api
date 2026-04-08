package com.ecommerce.api.repository;

import com.ecommerce.api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Recherche par catégorie
    List<Product> findByCategory(String category);

    // Recherche par prix min/max
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    // Recherche par nom (contient)
    List<Product> findByNameContainingIgnoreCase(String name);
}


