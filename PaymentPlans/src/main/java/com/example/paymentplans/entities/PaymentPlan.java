package com.example.paymentplans.entities;

import java.math.BigDecimal;

public class PaymentPlan
{
  private String     planId;
  private String     personId;
  private BigDecimal amount;

  public PaymentPlan()
  {
  }

  public PaymentPlan(BigDecimal amount)
  {
    setAmount(amount);
  }

  public String getPlanId()
  {
    return planId;
  }

  public void setPlanId(String planId)
  {
    this.planId = planId;
  }

  public String getPersonId()
  {
    return personId;
  }

  public void setPersonId(String personId)
  {
    this.personId = personId;
  }

  public BigDecimal getAmount()
  {
    return amount;
  }

  public void setAmount(BigDecimal amount)
  {
    this.amount = amount;
  }
}
