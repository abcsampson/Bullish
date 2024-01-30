package com.bullish.store;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findDiscountByProductId(Long productId);
}