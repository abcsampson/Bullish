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
public class ProductController {

    private final ProductRepository productRepository;

    ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    // curl -X POST http://localhost:8080/products -H "Content-type:application/json" -d "{\"name\": \"Toothpaste West\", \"price\": 15.05}"
    @PostMapping("/products")
    public Product addProduct(@RequestBody Product newProduct) {
        return productRepository.save(newProduct);
    }

    @PutMapping("/products/{id}")
    public Product updateProductById(@RequestBody Product newProduct, @PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(newProduct.getName());
                    product.setPrice(newProduct.getPrice());
                    return productRepository.save(product);
                })
                .orElseGet(() -> {
                    newProduct.setId(id);
                    return productRepository.save(newProduct);
                });
    }

    @DeleteMapping("/products/{id}")
    public void deleteProductById(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException();
        }

        // TODO: Potential race condition.
        // It is possible that another API call has deleted the product before reaching this line.
        productRepository.deleteById(id);
    }
}