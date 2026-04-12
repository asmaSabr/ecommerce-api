package com.ecommerce.api.controller;

import com.ecommerce.api.dto.SellerResponse;
import com.ecommerce.api.entity.Seller;
import com.ecommerce.api.service.SellerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/sellers")
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @GetMapping
    public ResponseEntity<?> getAllSellers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "storeName") String sortBy,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        String version = (String) request.getAttribute("apiVersion");

        // ─── Version 2 — sans HATEOAS ────────────────────────
        if ("v2".equals(version)) {
            Page<SellerResponse> sellers = sellerService.findAll(pageable)
                    .map(this::toDto);
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
                    .body(sellers);
        }

        // ─── Version 1 — avec HATEOAS ────────────────────────
        Page<SellerResponse> sellers = sellerService.findAll(pageable)
                .map(seller -> {
                    SellerResponse dto = toDto(seller);
                    dto.add(linkTo(methodOn(SellerController.class)
                            .getSellerById(seller.getId()))
                            .withSelfRel());
                    return dto;
                });

        CollectionModel<SellerResponse> result = CollectionModel.of(sellers.getContent());
        result.add(Link.of("/api/v1/sellers?page=" + page + "&size=" + size).withSelfRel());

        if (sellers.hasNext())
            result.add(Link.of("/api/v1/sellers?page=" + (page + 1) + "&size=" + size).withRel("next"));
        if (sellers.hasPrevious())
            result.add(Link.of("/api/v1/sellers?page=" + (page - 1) + "&size=" + size).withRel("prev"));

        result.add(Link.of("/api/v1/sellers?page=0&size=" + size).withRel("first"));
        result.add(Link.of("/api/v1/sellers?page=" + (sellers.getTotalPages() - 1) + "&size=" + size).withRel("last"));

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
                .body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerResponse> getSellerById(@PathVariable Long id) {
        Seller seller = sellerService.findById(id);
        SellerResponse dto = toDto(seller);

        dto.add(linkTo(methodOn(SellerController.class)
                .getSellerById(id))
                .withSelfRel());
        dto.add(linkTo(methodOn(SellerController.class)
                .getAllSellers(0, 10, "storeName", null))
                .withRel("sellers"));
        dto.add(linkTo(methodOn(SellerController.class)
                .updateSeller(id, null))
                .withRel("update"));
        dto.add(linkTo(methodOn(SellerController.class)
                .deleteSeller(id))
                .withRel("delete"));

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES).cachePublic())
                .body(dto);
    }

    @PostMapping
    public ResponseEntity<SellerResponse> createSeller(@RequestBody Seller seller) {
        Seller saved = sellerService.save(seller);
        SellerResponse dto = toDto(saved);

        dto.add(linkTo(methodOn(SellerController.class)
                .getSellerById(saved.getId()))
                .withSelfRel());

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SellerResponse> updateSeller(
            @PathVariable Long id, @RequestBody Seller updated) {

        Seller seller = sellerService.updateSeller(id, updated);
        SellerResponse dto = toDto(seller);

        dto.add(linkTo(methodOn(SellerController.class)
                .getSellerById(id))
                .withSelfRel());

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        sellerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Helper ──────────────────────────────────────────
    private SellerResponse toDto(Seller seller) {
        SellerResponse dto = new SellerResponse();
        dto.setId(seller.getId());
        dto.setStoreName(seller.getStoreName());
        dto.setEmail(seller.getEmail());
        dto.setPhone(seller.getPhone());
        dto.setRating(seller.getRating());
        dto.setCreatedAt(seller.getCreatedAt());
        return dto;
    }
}