package com.bullish.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BasketControllerTests {

    @Autowired
    private BasketController basketController;
    
    @Autowired
    private DiscountController discountController;

    @Autowired
    private ProductController productController;

    @BeforeEach
    void setup() {
        Product product1 = new Product("testname", 16f);
        Product product2 = new Product("testname2", 15f);
        Product product3 = new Product("testname3", 10f);
        productController.addProduct(product1);
        productController.addProduct(product2);
        productController.addProduct(product3);

        Discount discount1 = new Discount(1L, 2L, 70f);
        Discount discount2 = new Discount(2L, 3L, 80f);
        discountController.addDiscount(discount1);
        discountController.addDiscount(discount2);
    }

    @Test
    void addBasketItem() {
        assertThat(basketController.getAllBasketItems()).isEmpty();

        buildItemList(2L, 1L, 1L);

        assertThat(basketController.getAllBasketItems().size()).isEqualTo(3);
        assertThat(basketController.getBasketItemById(1L).getProductId()).isEqualTo(2L);
        assertThat(basketController.getBasketItemById(2L).getProductId()).isEqualTo(1L);
        assertThat(basketController.getBasketItemById(3L).getProductId()).isEqualTo(1L);
    }

    @Test
    void addBasketItem_productDoesNotExist_throws() {
        BasketItem item1 = new BasketItem(4L);
        assertThatThrownBy(() -> basketController.addBasketItem(item1)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void updateBasketItem() {
        assertThat(basketController.getAllBasketItems()).isEmpty();

        BasketItem item1 = new BasketItem(3L);
        basketController.addBasketItem(item1);

        assertThat(basketController.getBasketItemById(1L).getProductId()).isEqualTo(3L);

        BasketItem item2 = new BasketItem(2L);
        basketController.updateBasketItemById(1L, item2);

        assertThat(basketController.getBasketItemById(1L).getProductId()).isEqualTo(2L);
    }

    @Test
    void deleteBasketItem() {
        buildItemList(2L, 1L);

        assertThat(basketController.getAllBasketItems().size()).isEqualTo(2);

        basketController.deleteBasketItemById(1L);

        assertThat(basketController.getAllBasketItems().size()).isEqualTo(1);
        assertThatThrownBy(() -> basketController.getBasketItemById(1L)).isInstanceOf(RuntimeException.class);

        assertThat(basketController.getBasketItemById(2L)).isInstanceOf(BasketItem.class);
    }

    @Test
    void checkout_noItems() {


        assertThat(basketController.checkoutBasket()).isEqualTo(0f);
    }

    @Test
    void checkout_noDiscount() {
        buildItemList(3L, 3L, 3L);

        assertThat(basketController.checkoutBasket()).isEqualTo(30f);
    }

    @Test
    void checkout_discountedItemInOneGroup() {
        buildItemList(3L, 3L, 3L, 1L, 1L);

        assertThat(basketController.checkoutBasket()).isEqualTo(52.4f);
    }

    @Test
    void checkout_discountedItemWithRemainders() {
        buildItemList(2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L); // 8 items

        // 6 items get the discount.
        // 15 * 6 * 80% + 15 * 2
        assertThat(basketController.checkoutBasket()).isEqualTo(102f);
    }

    private void buildItemList(Long ...ids) {
        for (Long id: ids) {
            BasketItem item = new BasketItem(id);
            basketController.addBasketItem(item);
        }
    }
}
