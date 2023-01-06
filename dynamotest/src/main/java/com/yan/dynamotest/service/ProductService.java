package com.yan.dynamotest.service;

import com.yan.dynamotest.entity.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
	Flux<Product> getProductAll();
	
	Flux<Product> getProductByName(String name);

	Mono<Product> getProductById(String id);

	Mono<Product> createProduct(Product product);

	Flux<Product> getNameAndStockThen(String name,int stock);

	Flux<Product> joinWithUserRepository();
	
	Mono<String> delProductById(String id);
	
	Mono<Product> updateProduct(String id , Product product);
}
