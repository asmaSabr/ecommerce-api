package com.ecommerce.api.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Embeddable
public class Address {
    @NotNull
    private String street;

    @NotNull
    private String city;

    @NotNull
    private String country;

    @NotNull
    @Pattern(regexp = "^[0-9]{4,10}$", message = "Zip code must be numeric")
    private String zipCode;
}
