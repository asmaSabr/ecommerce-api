package com.ecommerce.api.repository;

import com.ecommerce.api.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    // Recherche par nom de boutique
    Seller findByStoreName(String storeName);

    // Recherche par email
    Seller findByEmail(String email);
}
