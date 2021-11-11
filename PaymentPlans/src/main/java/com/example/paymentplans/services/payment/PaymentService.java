package com.example.paymentplans.services.payment;

import com.example.paymentplans.entities.Payment;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService
{
  String createPayment(String planId, BigDecimal amount);

  List<Payment> showPayments(Pageable page, String planId);

  String changePaymentAmount(BigDecimal amount, String paymentId);

}
