package com.example.paymentplans.dto;

import java.math.BigDecimal;

public class PaymentPlanResponse
{
  private String     username;
  private String     planId;
  private BigDecimal remainingMoney;
  private BigDecimal paymentPlanMoney;
  private String     Url;

  public PaymentPlanResponse(String username, String planId, BigDecimal remainingMoney, BigDecimal paymentPlanMoney)
  {
    setUsername(username);
    setPlanId(planId);
    setRemainingMoney(remainingMoney);
    setPaymentPlanMoney(paymentPlanMoney);
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }

  public String getPlanId()
  {
    return planId;
  }

  public void setPlanId(String planId)
  {
    this.planId = planId;
  }

  public BigDecimal getRemainingMoney()
  {
    return remainingMoney;
  }

  public void setRemainingMoney(BigDecimal remainingMoney)
  {
    this.remainingMoney = remainingMoney;
  }

  public String getUrl()
  {
    return Url;
  }

  public void setUrl(String url)
  {
    Url = url;
  }

  public BigDecimal getPaymentPlanMoney()
  {
    return paymentPlanMoney;
  }

  public void setPaymentPlanMoney(BigDecimal paymentPlanMoney)
  {
    this.paymentPlanMoney = paymentPlanMoney;
  }
}
