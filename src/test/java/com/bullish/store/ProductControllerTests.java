package com.bullish.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductControllerTests {

	@Autowired
	private ProductController productController;

	@Test
	void addProduct() {
		assertThat(productController.getAllProducts()).isEmpty();

		Product product1 = new Product("testname", 1.5f);
		Product product2 = new Product("testname2", 1.0f);
		productController.addProduct(product1);
		productController.addProduct(product2);

		assertThat(productController.getAllProducts().size()).isEqualTo(2);
		assertThat(productController.getProductById(1L).getName()).isEqualTo("testname");
		assertThat(productController.getProductById(1L).getPrice()).isEqualTo(1.5f);
		assertThat(productController.getProductById(2L).getName()).isEqualTo("testname2");
		assertThat(productController.getProductById(2L).getPrice()).isEqualTo(1.0f);
	}

	@Test
	void addProduct_invalidPrice_throwsError() {
		Product product1 = new Product("testname", -1f);
		assertThatThrownBy(() -> productController.addProduct(product1)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void updateProduct() {
		assertThat(productController.getAllProducts()).isEmpty();

		Product product1 = new Product("testname", 1.5f);
		productController.addProduct(product1);

		assertThat(productController.getProductById(1L).getName()).isEqualTo("testname");
		assertThat(productController.getProductById(1L).getPrice()).isEqualTo(1.5f);

		Product product2 = new Product("testname2", 1.0f);
		productController.updateProductById(1L, product2);

		assertThat(productController.getProductById(1L).getName()).isEqualTo("testname2");
		assertThat(productController.getProductById(1L).getPrice()).isEqualTo(1.0f);
	}

	@Test
	void deleteProduct() {
		Product product1 = new Product("testname", 1.5f);
		Product product2 = new Product("testname2", 1.0f);
		productController.addProduct(product1);
		productController.addProduct(product2);

		assertThat(productController.getAllProducts().size()).isEqualTo(2);

		productController.deleteProductById(1L);

		assertThat(productController.getAllProducts().size()).isEqualTo(1);
		assertThatThrownBy(() -> productController.getProductById(1L)).isInstanceOf(RuntimeException.class);

		assertThat(productController.getProductById(2L)).isInstanceOf(Product.class);
	}
}
