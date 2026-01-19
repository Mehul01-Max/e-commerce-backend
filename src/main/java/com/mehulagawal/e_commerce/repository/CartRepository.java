package com.mehulagawal.e_commerce.repository;

import com.mehulagawal.e_commerce.model.CART_ITEM;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CartRepository extends MongoRepository<CART_ITEM, String> {
    public List<CART_ITEM> findByUserId(String userId);
    void deleteByUserId(String userId);
}
