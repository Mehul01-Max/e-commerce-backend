package com.mehulagawal.e_commerce.repository;

import com.mehulagawal.e_commerce.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
