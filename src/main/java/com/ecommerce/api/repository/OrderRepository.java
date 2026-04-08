package com.ecommerce.api.repository;

import com.ecommerce.api.entity.Order;
import com.ecommerce.api.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Recherche par nom du client
    List<Order> findByCustomerName(String customerName);

    // Recherche par statut
    List<Order> findByStatus(OrderStatus status);

    // Recherche par produit (via relation ManyToMany)
    List<Order> findByProducts_Id(Long productId);
}
