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
class DiscountControllerTests {

    @Autowired
    private DiscountController discountController;

    @Autowired
    private ProductController productController;

    @BeforeEach
    void setup() {
        Product product1 = new Product("testname", 1.5f);
        Product product2 = new Product("testname2", 1.0f);
        productController.addProduct(product1);
        productController.addProduct(product2);
    }

    @Test
    void addDiscount() {
        assertThat(discountController.getAllDiscounts()).isEmpty();

        Discount discount1 = new Discount(1L, 3L, 70f);
        Discount discount2 = new Discount(2L, 2L, 50f);
        discountController.addDiscount(discount1);
        discountController.addDiscount(discount2);

        assertThat(discountController.getAllDiscounts().size()).isEqualTo(2);
        assertThat(discountController.getDiscountById(1L).getProductId()).isEqualTo(1L);
        assertThat(discountController.getDiscountById(1L).getQuantity()).isEqualTo(3L);
        assertThat(discountController.getDiscountById(1L).getPercentage()).isEqualTo(70f);
        assertThat(discountController.getDiscountById(2L).getProductId()).isEqualTo(2L);
        assertThat(discountController.getDiscountById(2L).getQuantity()).isEqualTo(2L);
        assertThat(discountController.getDiscountById(2L).getPercentage()).isEqualTo(50f);
    }

    @Test
    void addDiscount_productDoesNotExist_throws() {
        Discount discount1 = new Discount(3L, 3L, 70f);
        assertThatThrownBy(() -> discountController.addDiscount(discount1)).isInstanceOf(RuntimeException.class);
    }
    @Test
    void addProduct_invalidQuantity_throwsError() {
        Discount discount1 = new Discount(2L, -1L, 70f);
        assertThatThrownBy(() -> discountController.addDiscount(discount1)).isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    void addProduct_invalidPercentage_throwsError() {
        Discount discount1 = new Discount(2L, 3L, -70f);
        assertThatThrownBy(() -> discountController.addDiscount(discount1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateDiscount() {
        assertThat(discountController.getAllDiscounts()).isEmpty();

        Discount discount1 = new Discount(1L, 3L, 70f);
        discountController.addDiscount(discount1);

        assertThat(discountController.getDiscountById(1L).getProductId()).isEqualTo(1L);
        assertThat(discountController.getDiscountById(1L).getQuantity()).isEqualTo(3L);
        assertThat(discountController.getDiscountById(1L).getPercentage()).isEqualTo(70f);

        Discount discount2 = new Discount(2L, 2L, 50f);
        discountController.updateDiscountById(1L, discount2);

        assertThat(discountController.getDiscountById(1L).getProductId()).isEqualTo(2L);
        assertThat(discountController.getDiscountById(1L).getQuantity()).isEqualTo(2L);
        assertThat(discountController.getDiscountById(1L).getPercentage()).isEqualTo(50f);
    }

    @Test
    void update_productDoesNotExist_throws() {
        Discount discount1 = new Discount(1L, 3L, 70f);
        discountController.addDiscount(discount1);

        Discount discount2 = new Discount(3L, 2L, 50f);
        assertThatThrownBy(() -> discountController.updateDiscountById(1L, discount2)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void deleteDiscount() {
        Discount discount1 = new Discount(1L, 3L, 70f);
        Discount discount2 = new Discount(2L, 2L, 50f);
        discountController.addDiscount(discount1);
        discountController.addDiscount(discount2);

        assertThat(discountController.getAllDiscounts().size()).isEqualTo(2);

        discountController.deleteDiscountById(1L);

        assertThat(discountController.getAllDiscounts().size()).isEqualTo(1);
        assertThatThrownBy(() -> discountController.getDiscountById(1L)).isInstanceOf(RuntimeException.class);

        assertThat(discountController.getDiscountById(2L)).isInstanceOf(Discount.class);
    }

    // @Test
    // void deleteProduct_deleteAssociatedDiscounts() {}
    // There should be some clever way in JpaRepository to do that but I haven't figured out.
}
