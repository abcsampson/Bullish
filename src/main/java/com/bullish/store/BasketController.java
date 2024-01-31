package com.bullish.store;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasketController {

    private final BasketItemRepository basketItemRepository;
    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;

    BasketController(
            BasketItemRepository basketItemRepository,
            DiscountRepository discountRepository,
            ProductRepository productRepository
    ) {
        this.basketItemRepository = basketItemRepository;
        this.discountRepository = discountRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/basket")
    public List<BasketItem> getAllBasketItems() {
        return basketItemRepository.findAll();
    }

    @GetMapping("/basket/{id}")
    public BasketItem getBasketItemById(@PathVariable Long id) {
        return basketItemRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    // curl -X POST http://localhost:8080/basket -H "Content-type:application/json" -d "{\"productId\": 1}"
    @PostMapping("/basket")
    public BasketItem addBasketItem(@RequestBody BasketItem newBasketItem) {
        validateProductExists(newBasketItem);

        return basketItemRepository.save(newBasketItem);
    }

    @PutMapping("/basket/{id}")
    public BasketItem updateBasketItemById(@PathVariable Long id, @RequestBody BasketItem newBasketItem) {
        validateProductExists(newBasketItem);

        return basketItemRepository.findById(id)
                .map(basketItem -> {
                    basketItem.setProductId(newBasketItem.getProductId());
                    return basketItemRepository.save(basketItem);
                })
                .orElseGet(() -> {
                    newBasketItem.setId(id);
                    return basketItemRepository.save(newBasketItem);
                });
    }

    @DeleteMapping("/basket/{id}")
    public void deleteBasketItemById(@PathVariable Long id) {
        if (!basketItemRepository.existsById(id)) {
            throw new RuntimeException();
        }

        basketItemRepository.deleteById(id);
    }

    @DeleteMapping("/clearbasket")
    public void deleteAllBasketItems() {
        basketItemRepository.deleteAll();
    }

    @GetMapping("checkout")
    public float checkoutBasket() {
        // TODO: Potential race condition.
        // Basket content / discount / product might be modified during calculation.
        Map<Long, Long> productIdToCount = basketItemRepository.findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(BasketItem::getProductId, Collectors.counting())
                );

        float totalPrice = 0f;
        for (Map.Entry<Long, Long> entry: productIdToCount.entrySet()) {
            Long productId = entry.getKey();
            Long quantity = entry.getValue();

            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException());

            Optional<Discount> discountOptional = discountRepository.findDiscountByProductId(productId);
            if (discountOptional.isPresent()) {
                Long discountQuantity = discountOptional.get().getQuantity();
                Long remainder = quantity % discountQuantity;
                totalPrice +=
                        product.getPrice() * remainder +
                                product.getPrice() * (quantity - remainder) * discountOptional.get().getPercentage() / 100;
            } else {
                totalPrice += product.getPrice() * quantity;
            }
        }

        return totalPrice;
    }

    private void validateProductExists(BasketItem item) {
        if (!productRepository.existsById(item.getProductId())) {
            throw new RuntimeException("Product does not exist");
        }
    }
}