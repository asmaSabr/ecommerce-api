package com.ecommerce.api.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProductResponse {
    private Long id;
    private String name;
    private Double price;
    private String currency;
    private Integer stock;
    private String category;
    private Long sellerId; // on expose seulement l’ID du vendeur
    private LocalDateTime createdAt;
}
