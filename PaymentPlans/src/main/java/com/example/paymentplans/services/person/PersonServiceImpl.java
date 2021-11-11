package com.example.paymentplans.services.person;

import com.example.paymentplans.common.Validation;
import com.example.paymentplans.dao.person.PersonDao;
import com.example.paymentplans.dto.PersonRequest;
import com.example.paymentplans.dto.PersonResponse;
import com.example.paymentplans.entities.Person;
import com.example.paymentplans.exceptions.InvalidException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.paymentplans.common.ExceptionMessages.NO_SUCH_USER;
import static com.example.paymentplans.common.ExceptionMessages.WRONG_PASSWORD;
import static com.example.paymentplans.common.OutputMessages.PASSWORD_CHANGE_SUCCESSFUL;
import static com.example.paymentplans.common.OutputMessages.USERNAME_CHANGE_SUCCESSFUL;

@Transactional
@Service
public class PersonServiceImpl implements PersonService
{

  private final PersonDao       personDao;
  private final ModelMapper     modelMapper;
  private final PasswordEncoder passwordEncoder;
  private final Validation      validation;

  @Autowired
  public PersonServiceImpl(PersonDao personDao, Validation validation)
  {
    this.personDao = personDao;
    this.modelMapper = new ModelMapper();
    this.passwordEncoder = new BCryptPasswordEncoder();
    this.validation = validation;
  }


  @Override
  public PersonResponse createUser(PersonRequest personRequest)
  {

    validation.checkIfUsernameExists(personRequest.getUsername());
    validation.passwordValidation(personRequest.getPassword(), personRequest.getPasswordConfirmation());
    personRequest.setPassword(passwordEncoder.encode(personRequest.getPassword()));
    personDao.insertPerson(personRequest);
    return modelMapper.map(personRequest, PersonResponse.class);

  }

  public String changeUsername(String newUsername)
  {

    String oldUsername = getCurrentLoggedUser().getUsername();
    validation.changeUsernameWithTheSameValidation(newUsername, oldUsername);
    validation.checkIfUsernameExists(newUsername);
    personDao.updateUsername(newUsername, oldUsername);
    return String.format(USERNAME_CHANGE_SUCCESSFUL, oldUsername, newUsername);

  }

  public String changePassword(String oldPassword, String newPassword, String repeatNewPassword)
  {

    Person person = getCurrentLoggedUser();
    String username = person.getUsername();
    String password = person.getPassword();

    boolean isMather = passwordEncoder.matches(oldPassword, password);
    if (!isMather) {
      throw new InvalidException(WRONG_PASSWORD);
    }

    validation.passwordValidation(newPassword, repeatNewPassword);

    String encodedNewPassword = passwordEncoder.encode(newPassword);

    personDao.updatePassword(encodedNewPassword, username);

    return PASSWORD_CHANGE_SUCCESSFUL;

  }

  public PersonResponse getMyProfile()
  {

    return modelMapper.map(
        personDao.findPersonByUsername(getCurrentLoggedUser().getUsername()).
            orElseThrow(() -> new InvalidException(NO_SUCH_USER)), PersonResponse.class);

  }

  public Person getCurrentLoggedUser()
  {

    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String principalUsername = "";
    if (principal instanceof UserDetails) {
      principalUsername = ((UserDetails) principal).getUsername();
    }
    return personDao.findPersonByUsername(principalUsername).orElseThrow();

  }

}
