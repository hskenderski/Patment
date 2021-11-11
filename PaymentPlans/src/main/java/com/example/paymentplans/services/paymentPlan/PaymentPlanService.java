package com.example.paymentplans.services.paymentPlan;

import com.example.paymentplans.dto.PaymentPlanRequest;
import com.example.paymentplans.dto.PaymentPlanResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentPlanService
{
  String createPaymentPlan(BigDecimal amount);

  List<PaymentPlanResponse> showAllPaymentPlans(Pageable page, PaymentPlanRequest paymentPlanRequest);

  String changePaymentPlanAmount(BigDecimal amount, String planId);
}
