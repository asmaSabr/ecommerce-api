package com.ecommerce.api.dto;

import com.ecommerce.api.entity.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class OrderCreateDTO {
    @NotBlank
    private String customerName;

    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemDTO> items;

    @NotNull
    private Address shippingAddress;
}