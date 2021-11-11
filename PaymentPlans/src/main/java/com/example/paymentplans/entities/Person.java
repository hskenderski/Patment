package com.example.paymentplans.entities;

import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;

public class Person implements GrantedAuthority
{
  private String   personId;
  private String   username;
  private String   password;
  private String   name;
  private String   address;
  private RoleName role;

  public Person()
  {
  }

  public Person(String personId, String username, String password, String name, String address, RoleName role)
  {
    setPersonId(personId);
    setUsername(username);
    setPassword(password);
    setName(name);
    setAddress(address);
    setRole(role);
  }

  public String getPersonId()
  {
    return personId;
  }

  public void setPersonId(String personId)
  {
    this.personId = personId;
  }

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

  public RoleName getRole()
  {
    return role;
  }

  public void setRole(RoleName role)
  {
    this.role = role;
  }

  public enum RoleName
  {
    USER,
    ADMIN
  }

  @Transient
  @Override
  public String getAuthority()
  {
    return "ROLE_" + role.toString();
  }
}
