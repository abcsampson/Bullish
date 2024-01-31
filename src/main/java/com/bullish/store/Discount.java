package com.bullish.store;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
class Discount {

    private @Id @GeneratedValue Long id;

    // TODO: For simplicity I assume that productIds are unique. I haven't found a way to ensure this in the framework.
    private Long productId;

    /**
     * The {percentage} is a number in (0..100].
     * We group every {quantity} items into groups and only items in the groups enjoy the discount.
     * For example, if {quantity} = 3 and {percentage} = 70, the customer buys 8 of the item,
     * then 6 of the item (in 2 groups of 3) have the price multiplied by 70%,
     * while the remaining 2 items costs the full price.
     */
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