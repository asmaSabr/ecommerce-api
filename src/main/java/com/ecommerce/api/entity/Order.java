package com.ecommerce.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "orders") // éviter conflit avec mot réservé SQL
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String customerName;

    @ManyToMany
    @NotEmpty(message = "Order must contain at least one product")
    private List<Product> products;

    @NotNull
    @DecimalMin(value = "0.1", message = "Total amount must be greater than 0")
    private Double totalAmount;

    @Embedded
    @NotNull(message = "Shipping address is required")
    private Address shippingAddress;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private OrderStatus status = OrderStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();


}
