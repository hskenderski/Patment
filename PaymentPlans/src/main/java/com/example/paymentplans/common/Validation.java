package com.example.paymentplans.common;

public interface Validation
{

  void passwordValidation(String password, String repeatPassword);

  void checkIfUsernameExists(String username);

  void changeUsernameWithTheSameValidation(String newUsername, String oldUsername);

  void sortCriteriaInputValidation(String orderCriteria);

}
