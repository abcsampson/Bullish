package com.bullish.store;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiscountController {

    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;

    DiscountController(
            DiscountRepository discountRepository,
            ProductRepository productRepository
    ) {
        this.discountRepository = discountRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/discounts")
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    @GetMapping("/discounts/{id}")
    public Discount getDiscountById(@PathVariable Long id) {
        return discountRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    // curl -X POST http://localhost:8080/discounts -H "Content-type:application/json" -d "{\"productId\": 1, \"quantity\": 2, \"percentage\": 70}"
    @PostMapping("/discounts")
    public Discount addDiscount(@RequestBody Discount newDiscount) {
        validateDiscount(newDiscount);

        return discountRepository.save(newDiscount);
    }

    @PutMapping("/discounts/{id}")
    public Discount updateDiscountById(@PathVariable Long id, @RequestBody Discount newDiscount) {
        validateDiscount(newDiscount);

        return discountRepository.findById(id)
                .map(discount -> {
                    discount.setProductId(newDiscount.getProductId());
                    discount.setQuantity(newDiscount.getQuantity());
                    discount.setPercentage(newDiscount.getPercentage());
                    return discountRepository.save(discount);
                })
                .orElseGet(() -> {
                    newDiscount.setId(id);
                    return discountRepository.save(newDiscount);
                });
    }

    @DeleteMapping("/discounts/{id}")
    public void deleteDiscountById(@PathVariable Long id) {
        if (!discountRepository.existsById(id)) {
            throw new RuntimeException();
        }

        discountRepository.deleteById(id);
    }

    private void validateDiscount(Discount discount) {
        if (!productRepository.existsById(discount.getProductId())) {
            throw new RuntimeException("Product does not exist");
        }
        if (discount.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive integer");
        }
        if (discount.getPercentage() <= 0 || discount.getPercentage() > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
    }
}