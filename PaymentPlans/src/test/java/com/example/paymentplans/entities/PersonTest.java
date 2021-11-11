package com.example.paymentplans.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonTest
{

  @Test
  public void test_get_authority()
  {
    Person person = new Person();
    person.setRole(Person.RoleName.USER);
    assertEquals("ROLE_" + person.getRole(), person.getAuthority());
  }

}