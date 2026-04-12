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
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    @NotNull
    @Size(min = 10, max = 2000)
    private String description;

    @NotNull
    @DecimalMin(value = "0.1", message = "Price must be greater than 0")
    private Double price;

    @NotNull
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be ISO code (e.g. USD, EUR)")
    private String currency;

    @Min(value = 0, message = "Stock must be non-negative")
    private Integer stock;

    @ElementCollection
    private List<String> images;

    @NotNull
    @Pattern(regexp = "^(http|https)://.*$", message = "Thumbnail must be a valid URL")
    private String thumbnail;

    @NotNull
    private String category;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @NotNull(message = "Seller is required")
    private Seller seller;

}
