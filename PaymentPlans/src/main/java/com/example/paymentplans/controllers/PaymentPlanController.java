package com.example.paymentplans.controllers;

import com.example.paymentplans.dto.PaymentPlanRequest;
import com.example.paymentplans.dto.PaymentPlanResponse;
import com.example.paymentplans.services.paymentPlan.PaymentPlanService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

import static com.example.paymentplans.common.ExceptionMessages.INVALID_MONEY;
import static com.example.paymentplans.common.ExceptionMessages.INVALID_UUID;

@RestController
@Validated
@RequestMapping("/api/v1/payment/plan/")
public class PaymentPlanController
{
  private final PaymentPlanService paymentPlanService;

  public PaymentPlanController(PaymentPlanService paymentPlanService)
  {
    this.paymentPlanService = paymentPlanService;
  }

  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  @PostMapping("create/")
  @ResponseStatus(HttpStatus.OK)
  public String createPaymentPlan(@RequestParam @DecimalMin(value = "0.0", inclusive = false, message = INVALID_MONEY)
                                  @Digits(integer = 6, fraction = 2, message = INVALID_MONEY) BigDecimal amount)
  {
    return paymentPlanService.createPaymentPlan(amount);
  }


  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("show/all/")
  @ResponseStatus(HttpStatus.OK)
  public List<PaymentPlanResponse> showPaymentPlansByCriteria(@RequestBody @Valid PaymentPlanRequest paymentPlanRequest)
  {
    return paymentPlanService.showAllPaymentPlans(PageRequest.of(paymentPlanRequest.getPageNumber(), paymentPlanRequest.getPageSize()), paymentPlanRequest);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("change/amount")
  @ResponseStatus(HttpStatus.OK)
  public String changePaymentPlanAmount(@RequestParam @NotEmpty @Size(min = 32, max = 32, message = INVALID_UUID)
                                            String planId,
                                        @RequestParam @DecimalMin(value = "0.0", message = INVALID_MONEY)
                                        @Digits(integer = 6, fraction = 2, message = INVALID_MONEY)
                                            BigDecimal amount)
  {
    return paymentPlanService.changePaymentPlanAmount(amount, planId);
  }

}
