package com.example.paymentplans.dao.payment;

import com.example.paymentplans.entities.Payment;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PaymentDao
{
  void insertPayment(String planId, BigDecimal amount);

  List<Payment> getPayments(Pageable page, String planId);

  void updatePaymentAmount(BigDecimal amount, String paymentId);

  Optional<String> checkIfPaymentExist(String paymentId);

}
