package com.example.paymentplans.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonRequestTest
{

  @Test
  public void test_get_password_confirmation()
  {
    PersonRequest personRequest = new PersonRequest();
    personRequest.setPasswordConfirmation("test");
    assertEquals("test", personRequest.getPasswordConfirmation());
  }

}