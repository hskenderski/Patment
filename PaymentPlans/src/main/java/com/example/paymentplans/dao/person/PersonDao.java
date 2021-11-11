package com.example.paymentplans.dao.person;

import com.example.paymentplans.dto.PersonRequest;
import com.example.paymentplans.entities.Person;

import java.util.Optional;

public interface PersonDao
{
  Optional<Person> findPersonByUsername(String username);

  void insertPerson(PersonRequest personRequest);

  void updateUsername(String newUsername, String oldUsername);

  void updatePassword(String newPassword, String username);

}
