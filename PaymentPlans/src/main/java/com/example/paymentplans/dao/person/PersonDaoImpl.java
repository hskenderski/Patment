package com.example.paymentplans.dao.person;

import com.example.paymentplans.dto.PersonRequest;
import com.example.paymentplans.entities.Person;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PersonDaoImpl implements PersonDao
{
  private final NamedParameterJdbcOperations namedParameterJdbcOperations;

  public PersonDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations){
    this.namedParameterJdbcOperations = namedParameterJdbcOperations;
  }

  @Override
  public Optional<Person> findPersonByUsername(String username)
  {
    String sql =
        "SELECT P.PERSON_ID,            "
       +"       P.USERNAME,             "
       +"       P.PASSWORD,             "
       +"       P.NAME,                 "
       +"       P.ADDRESS,              "
       +"       P.ROLE                  "
       +"  FROM PERSON P                "
       +" WHERE P.USERNAME = :username  ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("username", username);

    try{
      return namedParameterJdbcOperations.queryForObject(sql, parameters,(rs, rowNum) ->
          Optional.of(
              new Person(
              rs.getString("PERSON_ID"),
              rs.getString("USERNAME"),
              rs.getString("PASSWORD"),
              rs.getString("NAME"),
              rs.getString("ADDRESS"),
              rs.getObject("ROLE",Person.RoleName.class))));
    }catch (EmptyResultDataAccessException ex){
      return Optional.empty();
    }
  }

  @Override
  public void insertPerson(PersonRequest personRequest){
    String sql =
        "INSERT INTO PERSON                        "
       +"        (PERSON_ID,                       "
       +"         USERNAME,                        "
       +"         PASSWORD,                        "
       +"         NAME,                            "
       +"         ADDRESS,                         "
       +"         ROLE)                            "
       +"      VALUES                              "
       +"        ((select SYS_GUID() from dual),   "
       +"         :username,                       "
       +"         :password,                       "
       +"         :name,                           "
       +"         :address,                        "
       +"         :role)                           ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("username", personRequest.getUsername())
        .addValue("password", personRequest.getPassword())
        .addValue("name", personRequest.getName())
        .addValue("address",personRequest.getAddress())
        .addValue("role", Person.RoleName.USER.name());

    namedParameterJdbcOperations.update(sql,parameters);
  }

  @Override
  public void updateUsername(String newUsername,String oldUsername){
    String sql =
        "UPDATE PERSON P                  "
       +"SET P.USERNAME = :newUsername    "
       +"WHERE P.USERNAME = :oldUsername  ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("newUsername", newUsername)
        .addValue("oldUsername", oldUsername);

    namedParameterJdbcOperations.update(sql,parameters);
  }

  @Override
  public void updatePassword(String newPassword,String username){
    String sql =
        "UPDATE PERSON P               "
       +"SET P.PASSWORD = :newPassword "
       +"WHERE P.USERNAME = :username  ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("newPassword",newPassword)
        .addValue("username",username);

    namedParameterJdbcOperations.update(sql,parameters);
  }





}
