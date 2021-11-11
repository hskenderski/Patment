package com.example.paymentplans.controllers;

import com.example.paymentplans.entities.Payment;
import com.example.paymentplans.services.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

import static com.example.paymentplans.common.ExceptionMessages.INVALID_MONEY;
import static com.example.paymentplans.common.ExceptionMessages.INVALID_UUID;

@RestController
@Validated
@RequestMapping("/api/v1/payment/")
public class PaymentController
{
  private final PaymentService paymentService;

  @Autowired
  public PaymentController(PaymentService paymentService)
  {
    this.paymentService = paymentService;
  }

  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  @PostMapping("installment/")
  @ResponseStatus(HttpStatus.OK)
  public String insertInstallment(@RequestParam @NotEmpty @Size(min = 32, max = 32, message = INVALID_UUID) String planId,
                                  @RequestParam @DecimalMin(value = "0.0", inclusive = false, message = INVALID_MONEY)
                                  @Digits(integer = 6, fraction = 2, message = INVALID_MONEY) BigDecimal amount)
  {
    return paymentService.createPayment(planId, amount);
  }

  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  @GetMapping("show/")
  @ResponseStatus(HttpStatus.OK)
  public List<Payment> showPaymentsByPaymentPlan(@RequestParam(defaultValue = "0") @Min(value = 0) Integer pageNumber,
                                                 @Min(value = 1) @Max(value = 100) @RequestParam(defaultValue = "100") Integer pageCapacity,
                                                 @RequestParam @NotEmpty @Size(min = 32, max = 32, message = INVALID_UUID) String planId)
  {
    return paymentService.showPayments(PageRequest.of(pageNumber, pageCapacity), planId);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("change/amount")
  @ResponseStatus(HttpStatus.OK)
  public String changePaymentAmount(@RequestParam @NotEmpty @Size(min = 32, max = 32, message = INVALID_UUID) String paymentId,
                                    @RequestParam @DecimalMin(value = "0.0", message = INVALID_MONEY)
                                    @Digits(integer = 6, fraction = 2, message = INVALID_MONEY) BigDecimal amount)
  {
    return paymentService.changePaymentAmount(amount, paymentId);
  }

}
