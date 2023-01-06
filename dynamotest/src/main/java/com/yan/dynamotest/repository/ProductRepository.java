package com.yan.dynamotest.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.yan.dynamotest.entity.Product;
import com.yan.dynamotest.exception.ProductNotFoundException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class ProductRepository {
    private static final Logger log = LoggerFactory.getLogger(ProductRepository.class);
    private final DynamoDBMapper dynamoDBMapper;

    public ProductRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public Flux<Product> findByNameAndStock(String name, int stock) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":name", new AttributeValue().withS(name));
        expressionAttributeValues.put(":stock", new AttributeValue().withN(String.valueOf(stock)));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("contains(#name, :name) AND stock > :stock")
                .withExpressionAttributeNames(Collections.singletonMap("#name", "name"))
                .withExpressionAttributeValues(expressionAttributeValues);

        List<Product> results = dynamoDBMapper.scan(Product.class, scanExpression);
        if (results.isEmpty()) {
            throw new ProductNotFoundException(name + "is Name Not Found. And stock not > " + stock + ".");
        } else {
            return Flux.fromIterable(results);
        }
    }

    public Flux<Product> findAll() {
        return Flux.fromIterable(dynamoDBMapper.scan(Product.class, new DynamoDBScanExpression()));
    }

    public Flux<Product> findByName(String name) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":name", new AttributeValue().withS(name));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("contains(#name, :name)")
                .withExpressionAttributeNames(Collections.singletonMap("#name", "name"))
                .withExpressionAttributeValues(expressionAttributeValues);
        List<Product> results = dynamoDBMapper.scan(Product.class, scanExpression);
        if (results.isEmpty()) {
            throw new ProductNotFoundException(name + "is Name Not Found.");
        } else {
            return Flux.fromIterable(results);
        }
    }

    public Mono<Product> findById(String id) {
        return Mono.fromCallable(() -> dynamoDBMapper.load(Product.class, id));
    }

    public Mono<Product> save(Product product) {
        return Mono.fromCallable(() -> {
            Product p = new Product();
            p.setName(product.getName()).setPrice(product.getPrice()).setStock(product.getStock()).setAddress(product.getAddress());
            dynamoDBMapper.save(p);
            return p;
        });
    }

    public Mono<String> delete(String id) {
        return Mono.fromCallable(() -> {
            Product existingProduct = dynamoDBMapper.load(Product.class, id);
            if (existingProduct != null) {
                dynamoDBMapper.delete(existingProduct);
                return "Product Deleted.";
            }
            throw new ProductNotFoundException(id + " Product is NotFound");
        });
    }

    public Mono<Product> update(String id, Product product) {
        return Mono.fromCallable(() -> {
            Product existingProduct = dynamoDBMapper.load(Product.class, id);
            if (existingProduct != null) {
                existingProduct.setName(product.getName());
                existingProduct.setPrice(product.getPrice());
                existingProduct.setStock(product.getStock());
                dynamoDBMapper.save(existingProduct);
                return existingProduct;
            }
            throw new ProductNotFoundException(id + " Product is NotFound");
        });
    }
}
