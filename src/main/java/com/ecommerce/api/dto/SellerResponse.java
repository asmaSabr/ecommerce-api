package com.ecommerce.api.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import java.time.LocalDateTime;

@Getter
@Setter
public class SellerResponse extends RepresentationModel<SellerResponse> {
    private Long id;
    private String storeName;
    private String email;
    private String phone;
    private Double rating;
    private LocalDateTime createdAt;


}