package com.example.paymentplans.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.example.paymentplans.common.OutputMessages.LOGIN_SUCCESSFUL;

@RestController
@RequestMapping("/api/v1/")
public class LoginController
{

  @PostMapping("login")
  @ResponseStatus(HttpStatus.OK)
  public String login()
  {
    return LOGIN_SUCCESSFUL;
  }

}
