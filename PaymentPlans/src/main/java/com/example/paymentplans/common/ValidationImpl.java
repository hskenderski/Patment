package com.example.paymentplans.common;

import com.example.paymentplans.dao.person.PersonDao;
import com.example.paymentplans.exceptions.InvalidException;
import org.springframework.stereotype.Component;

import static com.example.paymentplans.common.ExceptionMessages.*;

@Component
public class ValidationImpl implements Validation
{

  private final PersonDao personDao;

  public ValidationImpl(PersonDao personDao)
  {
    this.personDao = personDao;
  }

  @Override
  public void passwordValidation(String password, String repeatPassword)
  {
    if (!(password.equals(repeatPassword))) {
      throw new InvalidException(PASSWORD_DONT_MATCH);
    }
    boolean containsUpperCaseLetter = false;
    boolean containsLowerCaseLetter = false;
    boolean containsSpecialSymbol = false;
    boolean containsDigit = false;
    boolean containsWhiteSpace = false;
    for (char c : password.toCharArray()) {
      if (Character.isUpperCase(c)) {
        containsUpperCaseLetter = true;
      }
      if (Character.isLowerCase(c)) {
        containsLowerCaseLetter = true;
      }
      if (!Character.isAlphabetic(c) && (!Character.isDigit(c)) && (!Character.isWhitespace(c))) {
        containsSpecialSymbol = true;
      }
      if (Character.isDigit(c)) {
        containsDigit = true;
      }
      if (Character.isWhitespace(c)) {
        containsWhiteSpace = true;
      }
    }
    if (!containsSpecialSymbol) {
      throw new InvalidException(PASSWORD_SHOULD_HAVE_SPECIAL_SYMBOL);
    }
    else if (!containsDigit) {
      throw new InvalidException(PASSWORD_SHOULD_HAVE_DIGIT);
    }
    else if (!containsUpperCaseLetter) {
      throw new InvalidException(PASSWORD_SHOULD_HAVE_UPPERCASE_LETTER);
    }
    else if (!containsLowerCaseLetter) {
      throw new InvalidException(PASSWORD_SHOULD_HAVE_LOWERCASE_LETTER);
    }
    else if (containsWhiteSpace) {
      throw new InvalidException(PASSWORD_CANT_HAVE_WHITESPACE);
    }
    else if (password.length() < 7) {
      throw new InvalidException(PASSWORD_SHOULD_BE_MIN_7_SYMBOLS);
    }
  }


  @Override
  public void checkIfUsernameExists(String username)
  {
    if (personDao.findPersonByUsername(username).isPresent()) {
      throw new InvalidException(USERNAME_ALREADY_EXIST);
    }
  }

  @Override
  public void changeUsernameWithTheSameValidation(String newUsername, String oldUsername)
  {

    if (newUsername.equals(oldUsername)) {
      throw new InvalidException(CHANGE_USERNAME_WITH_THE_SAME_ONE);
    }
  }

  @Override
  public void sortCriteriaInputValidation(String sortCriteria)
  {
    if (!(sortCriteria.equalsIgnoreCase("REMAINING_TO_PAID_MONEY") || sortCriteria.equalsIgnoreCase("PLAN_ID")
        || sortCriteria.equalsIgnoreCase("PERSON_ID") || sortCriteria.equalsIgnoreCase("USERNAME") || sortCriteria.equalsIgnoreCase("NAME"))) {
      throw new InvalidException(INVALID_INPUT);
    }
  }

}
