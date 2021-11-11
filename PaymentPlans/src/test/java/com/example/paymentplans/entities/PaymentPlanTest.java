package com.example.paymentplans.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentPlanTest
{
  @Test
  public void TestConstructor()
  {
    PaymentPlan paymentPlan = new PaymentPlan();
    paymentPlan.setPlanId("1");
    paymentPlan.setPersonId("1");
    assertNotNull(paymentPlan);
    assertEquals("1", paymentPlan.getPlanId());
    assertEquals("1", paymentPlan.getPersonId());
  }

}