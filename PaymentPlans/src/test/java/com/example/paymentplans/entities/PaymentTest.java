package com.example.paymentplans.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentTest
{

  @Test
  public void TestConstructor()
  {
    Payment payment = new Payment();
    assertNotNull(payment);
  }
}