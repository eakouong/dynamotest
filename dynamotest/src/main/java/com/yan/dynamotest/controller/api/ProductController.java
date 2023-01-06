package com.yan.dynamotest.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yan.dynamotest.entity.Product;
import com.yan.dynamotest.service.ProductService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("api/v1/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Flux<Product> getProductAll() {
        return productService.getProductAll();
    }

    @PostMapping
    public Mono<Product> saveProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @GetMapping("/{userUid}")
    public Mono<Product> getProductById(@PathVariable("userUid") String userUid) {
        return productService.getProductById(userUid);
    }

    @GetMapping("/search/{name}")
    public Flux<Product> getProductByName(@PathVariable("name") String name) {
        return productService.getProductByName(name);
    }

    @PutMapping("/{userUid}")
    public Mono<Product> updateProduct(@PathVariable("userUid") String userUid, @RequestBody Product product) {
        return productService.updateProduct(userUid, product);
    }

    @GetMapping(path = "/search", params = {"name", "stock"})
    public Mono<ResponseEntity<Flux<Product>>> searchProductByStockThen(@RequestParam String name,
                                                                        @RequestParam int stock) {
        Flux<Product> products = productService.getNameAndStockThen(name, stock);
        return products.hasElements().map(hasElements -> Boolean.TRUE.equals(hasElements) ? ResponseEntity.ok(products)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{userUid}")
    public Mono<String> delProductById(@PathVariable("userUid") String userUid) {
        return productService.delProductById(userUid);
    }

}
