package com.example.paymentplans.services.person;

import com.example.paymentplans.dto.PersonRequest;
import com.example.paymentplans.dto.PersonResponse;
import com.example.paymentplans.entities.Person;

public interface PersonService
{
  PersonResponse createUser(PersonRequest PersonRequest);

  String changeUsername(String newUsername);

  PersonResponse getMyProfile();

  Person getCurrentLoggedUser();

  String changePassword(String oldPassword, String newPassword, String repeatNewPassword);
}
