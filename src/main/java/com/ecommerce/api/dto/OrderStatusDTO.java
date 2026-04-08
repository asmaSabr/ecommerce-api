package com.ecommerce.api.dto;

import com.ecommerce.api.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class OrderStatusDTO {
    @NotNull(message = "Status is required")
    private OrderStatus status;
}
