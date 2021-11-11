package com.example.paymentplans.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.example.paymentplans.common.ExceptionMessages.*;

public class PersonRequest
{

  @Pattern(regexp = "^[A-Za-z0-9.\\-]+$", message = INVALID_USERNAME)
  @Size(min = 5, message = USERNAME_SHOULD_BE_MIN_5_SYMBOLS)
  @Size(max = 50, message = USERNAME_SHOULD_BE_MAX_50_SYMBOLS)
  @NotBlank
  private String username;

  @NotBlank
  private String password;

  @NotBlank
  private String passwordConfirmation;

  @NotBlank
  @Size(min = 5, message = NAME_SHOULD_BE_MIN_5_LETTERS)
  @Size(max = 100, message = NAME_SHOULD_BE_MAX_100_LETTERS)
  private String name;

  @NotBlank
  private String address;

  public String getUsername()
  {
    return username;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public String getPasswordConfirmation()
  {
    return passwordConfirmation;
  }

  public void setPasswordConfirmation(String passwordConfirmation)
  {
    this.passwordConfirmation = passwordConfirmation;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getAddress()
  {
    return address;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }
}
