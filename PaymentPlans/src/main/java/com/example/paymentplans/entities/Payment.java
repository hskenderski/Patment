package com.example.paymentplans.entities;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class Payment
{
  private String        paymentId;
  private String        planId;
  private BigDecimal    amount;
  private ZonedDateTime paymentDateDt;

  public Payment()
  {
  }

  public Payment(String paymentId, String planId, BigDecimal amount, ZonedDateTime paymentDateDt)
  {
    setPaymentId(paymentId);
    setPlanId(planId);
    setAmount(amount);
    setPaymentDateDt(paymentDateDt);
  }

  public String getPaymentId()
  {
    return paymentId;
  }

  public void setPaymentId(String paymentId)
  {
    this.paymentId = paymentId;
  }

  public String getPlanId()
  {
    return planId;
  }

  public void setPlanId(String planId)
  {
    this.planId = planId;
  }

  public BigDecimal getAmount()
  {
    return amount;
  }

  public void setAmount(BigDecimal amount)
  {
    this.amount = amount;
  }

  public ZonedDateTime getPaymentDateDt()
  {
    return paymentDateDt;
  }

  public void setPaymentDateDt(ZonedDateTime paymentDateDt)
  {
    this.paymentDateDt = paymentDateDt;
  }
}
