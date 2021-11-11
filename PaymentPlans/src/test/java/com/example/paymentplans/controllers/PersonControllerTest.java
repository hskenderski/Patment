package com.example.paymentplans.controllers;


import capital.scalable.restdocs.AutoDocumentation;
import capital.scalable.restdocs.jackson.JacksonResultHandlers;
import capital.scalable.restdocs.response.ResponseModifyingPreprocessors;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.cli.CliDocumentation;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@Sql(scripts = {"/QueriesForTest.sql"})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class PersonControllerTest
{

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;


  @BeforeEach
  public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation)
  {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(webApplicationContext)
        .apply(documentationConfiguration(restDocumentation))
        .alwaysDo(JacksonResultHandlers.prepareJackson(objectMapper))
        .alwaysDo(MockMvcRestDocumentation.document("{method-name}",
            Preprocessors.preprocessRequest(),
            Preprocessors.preprocessResponse(
                ResponseModifyingPreprocessors.replaceBinaryContent(),
                ResponseModifyingPreprocessors.limitJsonArrayLength(objectMapper),
                Preprocessors.prettyPrint())))
        .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
            .uris()
            .withScheme("http")
            .withHost("localhost")
            .withPort(8080)
            .and().snippets()
            .withDefaults(CliDocumentation.curlRequest(),
                HttpDocumentation.httpRequest(),
                HttpDocumentation.httpResponse(),
                AutoDocumentation.requestFields(),
                AutoDocumentation.responseFields(),
                AutoDocumentation.pathParameters(),
                AutoDocumentation.requestParameters(),
                AutoDocumentation.description(),
                AutoDocumentation.methodAndPath(),
                AutoDocumentation.section()))
        .build();
  }


  @Test
  @WithUserDetails("Ivan1")
  public void change_username_successful() throws Exception
  {
    mockMvc.perform(patch("/api/v1/person/change/username")
            .queryParam("username", "Ivann"))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("Ivan2")
  public void change_username_with_the_same_one() throws Exception
  {
    mockMvc.perform(patch("/api/v1/person/change/username")
            .queryParam("username", "Ivan2"))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan2")
  public void change_username_with_already_used() throws Exception
  {
    mockMvc.perform(patch("/api/v1/person/change/username")
            .queryParam("username", "Ivan1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan2")
  public void change_username_with_wrong_min_size() throws Exception
  {
    mockMvc.perform(patch("/api/v1/person/change/username")
            .queryParam("username", "Ivan"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan2")
  public void change_username_with_wrong_max_size() throws Exception
  {
    mockMvc.perform(patch("/api/v1/person/change/username?")
            .queryParam("username", "Ivan1Ivan1Ivan1Ivan1Ivan1Ivan1Ivan1Ivan1Ivan1Ivan1Ivan1Ivan1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan2")
  public void change_username_with_wrong_username() throws Exception
  {
    mockMvc.perform(patch("/api/v1/person/change/username")
            .queryParam("username", "Ivan$#"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan2")
  public void change_username_without_parameter() throws Exception
  {
    mockMvc.perform(patch("/api/v1/person/change/username"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan2")
  public void show_profile_successful() throws Exception
  {
    mockMvc.perform(get("/api/v1/person/my/profile"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("Ivan2"))
        .andExpect(jsonPath("$.name").value("Ivan Ivailov Ivanov2"))
        .andExpect(jsonPath("$.address").value("Sofia"));
  }

  @Test
  @WithUserDetails("Ivan2")
  public void show_profile_wrong_url() throws Exception
  {
    mockMvc.perform(get("/api/v1/person/my/profil"))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithUserDetails("Ivan2")
  public void change_password_successful() throws Exception
  {
    mockMvc.perform(patch("/api/v1/person/change/password")
            .queryParam("oldPassword", "Ivan1ha!")
            .queryParam("newPassword", "Ivan1ha!!")
            .queryParam("newPasswordConfirmation", "Ivan1ha!!"))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("Ivan3")
  public void change_password_password_dont_match() throws Exception
  {
    mockMvc.perform(patch("/api/v1/person/change/password")
            .queryParam("oldPassword", "Ivan1ha!")
            .queryParam("newPassword", "Ivan1ha!!")
            .queryParam("newPasswordConfirmation", "Ivan1ha!!!"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan3")
  public void change_password_password_must_contain_upper_case_letter() throws Exception
  {
    mockMvc.perform(patch("/api/v1/person/change/password")
            .queryParam("oldPassword", "Ivan1ha!")
            .queryParam("newPassword", "ivan1ha!!")
            .queryParam("newPasswordConfirmation", "ivan1ha!!"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan3")
  public void change_password_password_must_contain_lower_case_letter() throws Exception
  {
    mockMvc.perform(patch("/api/v1/person/change/password")
            .queryParam("oldPassword", "Ivan1ha!")
            .queryParam("newPassword", "IVAN1HA!!")
            .queryParam("newPasswordConfirmation", "IVAN1HA!!"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan3")
  public void change_password_password_must_contain_special_symbol() throws Exception
  {
    mockMvc.perform(patch("/api/v1/person/change/password")
            .queryParam("oldPassword", "Ivan1ha!")
            .queryParam("newPassword", "Ivan1ha")
            .queryParam("newPasswordConfirmation", "Ivan1ha"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan3")
  public void change_password_password_must_contain_digit() throws Exception
  {
    mockMvc.perform(patch("/api/v1/person/change/password")
            .queryParam("oldPassword", "Ivan1ha!")
            .queryParam("newPassword", "Ivanha!")
            .queryParam("newPasswordConfirmation", "Ivanha!"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan3")
  public void change_password_password_is_not_allowed_to_have_whiteSpace() throws Exception
  {
    mockMvc.perform(patch("/api/v1/person/change/password")
            .queryParam("oldPassword", "Ivan1ha!")
            .queryParam("newPassword", "Ivan1ha !")
            .queryParam("newPasswordConfirmation", "Ivan1ha !"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan3")
  public void change_password_password_should_have_min_7_symbols() throws Exception
  {
    mockMvc.perform(patch("/api/v1/person/change/password")
            .queryParam("oldPassword", "Ivan1ha!")
            .queryParam("newPassword", "Iv1ha!")
            .queryParam("newPasswordConfirmation", "Iv1ha!"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan3")
  public void change_password_wrong_old_passowrd() throws Exception
  {
    mockMvc.perform(patch("/api/v1/person/change/password")
            .queryParam("oldPassword", "Ivan1ha")
            .queryParam("newPassword", "Iva1ha!")
            .queryParam("newPasswordConfirmation", "Iva1ha!"))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_successful() throws Exception
  {
    mockMvc.perform(post("/api/v1/person/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "    \"username\": \"Ivan4\",\n" +
                "    \"password\": \"Ivan1ha!\",\n" +
                "    \"passwordConfirmation\": \"Ivan1ha!\",\n" +
                "    \"name\": \"Hristiyan Ivanov Skenderski\",\n" +
                "    \"address\": \"Sofia\"\n" +
                "}")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("Ivan4"))
        .andExpect(jsonPath("$.name").value("Hristiyan Ivanov Skenderski"))
        .andExpect(jsonPath("$.address").value("Sofia"));
  }

  @Test
  public void register_username_already_exist() throws Exception
  {
    mockMvc.perform(post("/api/v1/person/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "    \"username\": \"Ivan2\",\n" +
                "    \"password\": \"Ivan1ha!\",\n" +
                "    \"passwordConfirmation\": \"Ivan1ha!\",\n" +
                "    \"name\": \"Hristiyan Ivanov Skenderski\",\n" +
                "    \"address\": \"Sofia\"\n" +
                "}")
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_password_cant_be_blank() throws Exception
  {
    mockMvc.perform(post("/api/v1/person/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "    \"username\": \"Ivan2\",\n" +
                "    \"passwordConfirmation\": \"Ivan1ha!\",\n" +
                "    \"name\": \"Hristiyan Ivanov Skenderski\",\n" +
                "    \"address\": \"Sofia\"\n" +
                "}")
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_password_confirmation_cant_be_blank() throws Exception
  {
    mockMvc.perform(post("/api/v1/person/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "    \"username\": \"Ivan2\",\n" +
                "    \"password\": \"Ivan1ha!\",\n" +
                "    \"name\": \"Hristiyan Ivanov Skenderski\",\n" +
                "    \"address\": \"Sofia\"\n" +
                "}")
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_names_must_be_min_5_symbols() throws Exception
  {
    mockMvc.perform(post("/api/v1/person/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "    \"username\": \"Ivan2\",\n" +
                "    \"password\": \"Ivan1ha!\",\n" +
                "    \"passwordConfirmation\": \"Ivan1ha!\",\n" +
                "    \"name\": \"Hris\",\n" +
                "    \"address\": \"Sofia\"\n" +
                "}")
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_names_must_be_max_100_symbols() throws Exception
  {
    mockMvc.perform(post("/api/v1/person/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "    \"username\": \"Ivan2\",\n" +
                "    \"password\": \"Ivan1ha!\",\n" +
                "    \"passwordConfirmation\": \"Ivan1ha!\",\n" +
                "    \"name\": \"Hristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov SkenderskiHristiyan Ivanov Skenderski\",\n" +
                "    \"address\": \"Sofia\"\n" +
                "}")
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_names_cant_be_blank() throws Exception
  {
    mockMvc.perform(post("/api/v1/person/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "    \"username\": \"Ivan2\",\n" +
                "    \"password\": \"Ivan1ha!\",\n" +
                "    \"passwordConfirmation\": \"Ivan1ha!\",\n" +
                "    \"address\": \"Sofia\"\n" +
                "}")
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_address_cant_be_blank() throws Exception
  {
    mockMvc.perform(post("/api/v1/person/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "    \"username\": \"Ivan2\",\n" +
                "    \"password\": \"Ivan1ha!\",\n" +
                "    \"passwordConfirmation\": \"Ivan1ha!\",\n" +
                "    \"name\": \"Hristiyan Ivanov Skenderski\",\n" +
                "    \"address\": \"Sofia\"\n" +
                "}")
        )
        .andExpect(status().isBadRequest());
  }


}