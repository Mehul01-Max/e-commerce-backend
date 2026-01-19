package com.mehulagawal.e_commerce.repository;

import com.mehulagawal.e_commerce.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {
}
