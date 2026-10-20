package com.budiluhur.product_catalog.service;

import org.springframework.stereotype.Service;

@Service
public class DiscountService {

    public double calculateDiscountedPrice(
            double originalPrice,
            double discountPercentage) {

        if (originalPrice < 0) {
            throw new IllegalArgumentException(
                "Harga awal tidak boleh negatif!"
            );
        }

        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException(
                "Diskon harus antara 0% sampai 100%!"
            );
        }

        return originalPrice
                - (originalPrice * discountPercentage / 100);
    }
}