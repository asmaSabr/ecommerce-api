package com.ecommerce.api.controller;

import com.ecommerce.api.dto.*;
import com.ecommerce.api.entity.*;
import com.ecommerce.api.repository.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderRepository orderRepository,
                           ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    // ─────────────────────────────────────────
    // GET /api/v1/orders → liste toutes les commandes
    // ─────────────────────────────────────────
    //Pgination
    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<OrderResponseDTO> orders = orderRepository.findAll(pageable)
                .map(this::toResponseDTO);
        return ResponseEntity.ok(orders);
    }


    // ─────────────────────────────────────────
    // GET /api/v1/orders/{id} → commande par id
    // ─────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(order -> ResponseEntity.ok(toResponseDTO(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ─────────────────────────────────────────
    // GET /api/v1/orders?customerId=X → par customer
    // GET /api/v1/orders?productId=X  → par product
    // ─────────────────────────────────────────
    @GetMapping(params = "customerId")
    public ResponseEntity<List<OrderResponseDTO>> getByCustomer(
            @RequestParam String customerId) {
        List<OrderResponseDTO> orders = orderRepository
                .findByCustomerName(customerId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping(params = "productId")
    public ResponseEntity<List<OrderResponseDTO>> getByProduct(
            @RequestParam Long productId) {
        List<OrderResponseDTO> orders = orderRepository
                .findByItems_Product_Id(productId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    //Search using customerName
    @GetMapping(params = "search")
    public ResponseEntity<List<OrderResponseDTO>> searchOrders(@RequestParam String search) {
        List<OrderResponseDTO> orders = orderRepository
                .findByCustomerNameContainingIgnoreCase(search) // 🔍 recherche par nom client
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }




    // ─────────────────────────────────────────
    // POST /api/v1/orders → créer une commande
    // ─────────────────────────────────────────
    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(
            @Valid @RequestBody OrderCreateDTO dto) {

        Order order = new Order();
        order.setCustomerName(dto.getCustomerName());
        order.setShippingAddress(dto.getShippingAddress());

        // Pour chaque item du DTO → créer un OrderItem
        for (OrderItemDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "Product not found: " + itemDTO.getProductId()));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.setPriceAtOrder(product.getPrice()); // ✅ snapshot du prix
            order.getItems().add(item);
        }

        Order saved = orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponseDTO(saved));
    }

    // ─────────────────────────────────────────
    // PATCH /api/v1/orders/{id}/status → maj statut
    // ─────────────────────────────────────────
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusDTO dto) {

        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(dto.getStatus());
                    return ResponseEntity.ok(toResponseDTO(orderRepository.save(order)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ─────────────────────────────────────────
    // DELETE /api/v1/orders/{id} → supprimer
    // ─────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        orderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ─────────────────────────────────────────
    // Convertisseur Entité → DTO réponse
    // ─────────────────────────────────────────
    private OrderResponseDTO toResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setCustomerName(order.getCustomerName());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setStatus(order.getStatus().name());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setTotalAmount(order.getTotalAmount()); // ✅ calculé dynamiquement

        List<OrderItemResponseDTO> itemDTOs = order.getItems().stream()
                .map(item -> {
                    OrderItemResponseDTO i = new OrderItemResponseDTO();
                    i.setProductId(item.getProduct().getId());
                    i.setProductName(item.getProduct().getName());
                    i.setQuantity(item.getQuantity());
                    i.setPriceAtOrder(item.getPriceAtOrder());
                    return i;
                }).collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }
}

