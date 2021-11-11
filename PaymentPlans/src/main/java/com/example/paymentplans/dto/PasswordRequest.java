package com.example.paymentplans.dto;


import javax.validation.constraints.NotBlank;

public class PasswordRequest
{
  @NotBlank
  private String oldPassword;

  @NotBlank
  private String newPassword;

  @NotBlank
  private String newPasswordConfirmation;

  public String getOldPassword()
  {
    return oldPassword;
  }

  public void setOldPassword(String oldPassword)
  {
    this.oldPassword = oldPassword;
  }

  public String getNewPassword()
  {
    return newPassword;
  }

  public void setNewPassword(String newPassword)
  {
    this.newPassword = newPassword;
  }

  public String getNewPasswordConfirmation()
  {
    return newPasswordConfirmation;
  }

  public void setNewPasswordConfirmation(String newPasswordConfirmation)
  {
    this.newPasswordConfirmation = newPasswordConfirmation;
  }
}
