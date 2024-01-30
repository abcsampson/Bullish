package com.bullish.store;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
class BasketItem {

    private @Id @GeneratedValue Long id;
    private Long productId;

    BasketItem() {}

    BasketItem(Long productId) {
        this.productId = productId;
    }

    public Long getId() {
        return this.id;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}