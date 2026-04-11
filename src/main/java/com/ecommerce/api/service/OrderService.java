package com.ecommerce.api.service;

import com.ecommerce.api.entity.Order;
import com.ecommerce.api.entity.OrderStatus;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RESOURCE_NOT_FOUND"));
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    // Mise à jour du statut uniquement
    public Order updateStatus(Long id, OrderStatus status) {
        Order order = findById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    // Filtrer par client
    public List<Order> findByCustomer(String customerName) {
        return orderRepository.findByCustomerName(customerName);
    }

    // Filtrer par produit
    public List<Order> findByProduct(Long productId) {
        return orderRepository.findByItems_Product_Id(productId);
    }
    // Pagination
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
}
