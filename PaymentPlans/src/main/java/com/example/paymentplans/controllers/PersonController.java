package com.example.paymentplans.controllers;

import com.example.paymentplans.dto.PasswordRequest;
import com.example.paymentplans.dto.PersonRequest;
import com.example.paymentplans.dto.PersonResponse;
import com.example.paymentplans.services.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.example.paymentplans.common.ExceptionMessages.*;

@RestController
@Validated
@RequestMapping("/api/v1/person/")
public class PersonController
{

  private final PersonService personService;

  @Autowired
  public PersonController(PersonService personService)
  {
    this.personService = personService;
  }

  @PostMapping("register")
  @ResponseStatus(HttpStatus.OK)
  public PersonResponse register(@RequestBody @Valid PersonRequest personRequest)
  {
    return personService.createUser(personRequest);
  }

  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  @PatchMapping("change/username")
  @ResponseStatus(HttpStatus.OK)
  public String changeUsername(@RequestParam @Pattern(regexp = "^[A-Za-z0-9.\\-]+$", message = INVALID_USERNAME)
                               @NotBlank
                               @Size(min = 5, message = USERNAME_SHOULD_BE_MIN_5_SYMBOLS)
                               @Size(max = 50, message = USERNAME_SHOULD_BE_MAX_50_SYMBOLS)
                                   String username)
  {
    return personService.changeUsername(username);
  }

  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  @GetMapping("my/profile")
  @ResponseStatus(HttpStatus.OK)
  public PersonResponse showProfile()
  {
    return personService.getMyProfile();
  }

  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  @PatchMapping("change/password")
  @ResponseStatus(HttpStatus.OK)
  public String changePassword(@Valid PasswordRequest passwordRequest)
  {
    return personService.changePassword(passwordRequest.getOldPassword(),
        passwordRequest.getNewPassword(),
        passwordRequest.getNewPasswordConfirmation());
  }

}
