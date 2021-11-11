package com.example.paymentplans.security;

import com.example.paymentplans.dao.person.PersonDao;
import com.example.paymentplans.entities.Person;
import com.example.paymentplans.exceptions.InvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.paymentplans.common.ExceptionMessages.NO_SUCH_USER;

@Service
public class UserDetailServiceImpl implements UserDetailsService
{

  private final PersonDao personDao;

  @Autowired
  public UserDetailServiceImpl(PersonDao personDao)
  {
    this.personDao = personDao;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
  {
    Person person = personDao.findPersonByUsername(username).orElseThrow(() -> new InvalidException(NO_SUCH_USER));

    return org.springframework.security.core.userdetails.User
        .withUsername(person.getUsername())
        .password(person.getPassword())
        .roles(person.getRole().name())
        .build();
  }
}
