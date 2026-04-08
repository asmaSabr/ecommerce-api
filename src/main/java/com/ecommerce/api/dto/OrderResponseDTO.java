package com.ecommerce.api.dto;

import com.ecommerce.api.entity.Address;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter

public class OrderResponseDTO {
    private Long id;
    private String customerName;
    private List<OrderItemResponseDTO> items;
    private Double totalAmount;
    private Address shippingAddress;
    private String status;
    private LocalDateTime createdAt;
}
