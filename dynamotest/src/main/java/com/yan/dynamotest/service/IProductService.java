package com.yan.dynamotest.service;

import org.springframework.stereotype.Service;

import com.yan.dynamotest.entity.Product;
import com.yan.dynamotest.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class IProductService implements ProductService {

    private final ProductRepository productRepository;

    public IProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Flux<Product> getProductAll() {
        return productRepository.findAll();
    }

    @Override
    public Mono<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Mono<Product> createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Flux<Product> getNameAndStockThen(String name, int stock) {
        return productRepository.findByNameAndStock(name, stock);
    }

    @Override
    public Flux<Product> joinWithUserRepository() {
        return null;
    }

    @Override
    public Mono<String> delProductById(String id) {
        return productRepository.delete(id);
    }

    @Override
    public Flux<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public Mono<Product> updateProduct(String id, Product product) {
        return productRepository.update(id, product);
    }

}
