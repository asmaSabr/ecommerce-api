package com.ecommerce.api.service;

import com.ecommerce.api.entity.Seller;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.repository.SellerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }
    //Pagination
    public Page<Seller> findAll(Pageable pageable) {
        return sellerRepository.findAll(pageable);
    }

    public Seller findById(String  id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RESOURCE_NOT_FOUND"));
    }

    public Seller save(Seller seller) {
        return sellerRepository.save(seller);
    }

    // Mise à jour partielle : email interdit
    public Seller updateSeller(String id, Seller updated) {
        Seller seller = findById(id);
        if (updated.getStoreName() != null) seller.setStoreName(updated.getStoreName());
        if (updated.getPhone() != null) seller.setPhone(updated.getPhone());
        if (updated.getAddress() != null) seller.setAddress(updated.getAddress());
        if (updated.getRating() != null) seller.setRating(updated.getRating());
        // Email ignoré volontairement
        return sellerRepository.save(seller);
    }

    public void delete(String id) {
        sellerRepository.deleteById(id);
    }
}
