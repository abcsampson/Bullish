package com.bullish.store;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
class Discount {

    private @Id @GeneratedValue Long id;

    // TODO: For simplicity I assume that productIds are unique. I haven't found a way to ensure this in the framework.
    private Long productId;
    private Long quantity;
    private float percentage;

    Discount() {}

    Discount(Long productId, Long quantity, float percentage) {
        this.productId = productId;
        this.quantity = quantity;
        this.percentage = percentage;
    }

    public Long getId() {
        return this.id;
    }

    public Long getProductId() {
        return this.productId;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public float getPercentage() {
        return this.percentage;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
}