package com.mehulagawal.e_commerce.controller;

import com.mehulagawal.e_commerce.model.Order;
import com.mehulagawal.e_commerce.model.Payment;
import com.mehulagawal.e_commerce.repository.OrderRepository;
import com.mehulagawal.e_commerce.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/payment")
    public ResponseEntity<Payment> makePayment(
            @RequestParam String orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(order.getTotalAmount());
        payment.setStatus("SUCCESS");
        payment.setCreatedAt(new Date());
        payment.setPaymentId("pay_" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        paymentRepository.save(payment);
        order.setStatus("PAID");
        orderRepository.save(order);

        return ResponseEntity.ok(payment);
    }
}

