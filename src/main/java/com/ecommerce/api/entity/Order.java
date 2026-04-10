package com.ecommerce.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    //  OneToMany vers OrderItem au lieu de ManyToMany vers Product
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Embedded
    @NotNull(message = "Shipping address is required")
    private Address shippingAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @CreationTimestamp//  géré par Hibernate, pas à la construction
    @Column(name = "created_at", updatable = false)  // ← ajouter updatable = false
    private LocalDateTime createdAt;

    // calculé dynamiquement, jamais désynchronisé
    public Double getTotalAmount() {
        return items.stream()
                .mapToDouble(item -> item.getPriceAtOrder() * item.getQuantity())
                .sum();
    }
}