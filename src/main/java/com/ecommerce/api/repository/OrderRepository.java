package com.ecommerce.api.repository;

import com.ecommerce.api.entity.Order;
import com.ecommerce.api.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Trouver les commandes par nom du customer
    List<Order> findByCustomerName(String customerName);

    // Trouver les commandes qui contiennent un produit spécifique
    // Spring lit : Order → items → product → id
    List<Order> findByItems_Product_Id(Long productId);
}
