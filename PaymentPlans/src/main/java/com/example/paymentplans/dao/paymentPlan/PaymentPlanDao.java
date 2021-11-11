package com.example.paymentplans.dao.paymentPlan;

import com.example.paymentplans.dto.PaymentPlanRequest;
import com.example.paymentplans.dto.PaymentPlanResponse;
import com.example.paymentplans.entities.PaymentPlan;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PaymentPlanDao
{
  void insertPaymentPlan(BigDecimal amount, String personId, String username);

  List<PaymentPlanResponse> showPaymentPlans(Pageable page, PaymentPlanRequest paymentPlanRequest);

  Optional<PaymentPlan> showPaymentPlanMoneyByPlanIdAndPersonId(String planId, String personId);

  Optional<String> checkIfPaymentPlanExist(String personId, String planId);

  void updatePaymentPlanAmount(BigDecimal amount, String planId);

  Optional<String> checkIfPaymentPlanExist(String planId);

}
