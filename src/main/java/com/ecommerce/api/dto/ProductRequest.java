package com.ecommerce.api.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProductRequest {
    private String name;
    private String description;
    private Double price;
    private String currency;
    private Integer stock;
    private List<String> images;
    private String thumbnail;
    private String category;
    private String  sellerId; // juste l’ID du vendeur
}
