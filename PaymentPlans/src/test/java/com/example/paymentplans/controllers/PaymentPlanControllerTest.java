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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@Sql(scripts = {"/QueriesForTest.sql"})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class PaymentPlanControllerTest
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
  public void create_payment_plan_successfully() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/plan/create/")
            .queryParam("amount", "2000"))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("Ivan1")
  public void create_payment_with_negative_value() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/plan/create/")
            .queryParam("amount", "-2000"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan1")
  public void create_payment_with_zero_value() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/plan/create/")
            .queryParam("amount", "0"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan1")
  public void create_payment_with_invalid_big_decimal_integer() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/plan/create/")
            .queryParam("amount", "6666666"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan1")
  public void create_payment_with_invalid_big_decimal_fraction() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/plan/create/")
            .queryParam("amount", "6666.666"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan1")
  public void create_payment_without_amount_parameter() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/plan/create/"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan1")
  public void show_all_payment_plans_successfully_amount_from_to() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/plan/show/all/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "    \"amountFrom\": 0   ,\n" +
                "    \"amountTo\":   4000   ,\n" +
                "    \"sortCriteria\":  \"USERNAME\",\n" +
                "    \"orderCriteria\":  \"ASC\",\n" +
                "    \"pageNumber\":    0,\n" +
                "    \"pageSize\":      5\n" +
                "}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].username").value("Ivan1"))
        .andExpect(jsonPath("$.[0].planId").value("18752F46D5244A40BFFAC2D01B5431B0"))
        .andExpect(jsonPath("$.[0].remainingMoney").value(2000))
        .andExpect(jsonPath("$.[0].url").value("http://localhost:8080/api/v1/payment/show/?pageNumber=0&pageCapacity=5&planId=18752F46D5244A40BFFAC2D01B5431B0"))
        .andExpect(jsonPath("$.[0].paymentPlanMoney").value(2000))
        .andExpect(jsonPath("$.[1].username").value("Ivan2"))
        .andExpect(jsonPath("$.[1].planId").value("18752F46D5244A40BFFAC2D09B5431B0"))
        .andExpect(jsonPath("$.[1].remainingMoney").value(3000))
        .andExpect(jsonPath("$.[1].url").value("http://localhost:8080/api/v1/payment/show/?pageNumber=0&pageCapacity=5&planId=18752F46D5244A40BFFAC2D09B5431B0"))
        .andExpect(jsonPath("$.[1].paymentPlanMoney").value(3000))
        .andExpect(jsonPath("$.[2].username").value("Ivan3"))
        .andExpect(jsonPath("$.[2].remainingMoney").value(0))
        .andExpect(jsonPath("$.[2].paymentPlanMoney").value(4000));
  }

  @Test
  @WithUserDetails("Ivan1")
  public void show_all_payment_plans_successfully_amount_from_to_with_plan_id() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/plan/show/all/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "    \"amountFrom\": 0   ,\n" +
                "    \"amountTo\":   4000   ,\n" +
                "    \"sortCriteria\":  \"USERNAME\",\n" +
                "    \"orderCriteria\":  \"ASC\",\n" +
                "    \"planId\": \"11752F46D5244A40BFFAC2D09B5431B0\",\n" +
                "    \"pageNumber\":    0,\n" +
                "    \"pageSize\":      5\n" +
                "}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].username").value("Ivan3"))
        .andExpect(jsonPath("$.[0].remainingMoney").value(0))
        .andExpect(jsonPath("$.[0].paymentPlanMoney").value(4000))
        .andExpect(jsonPath("$.[0].planId").value("11752F46D5244A40BFFAC2D09B5431B0"));
  }

  @Test
  @WithUserDetails("Ivan1")
  public void show_all_payment_plans_successfully_with_person_id() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/plan/show/all/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "    \"sortCriteria\":  \"USERNAME\",\n" +
                "    \"orderCriteria\":  \"ASC\",\n" +
                "    \"personId\": \"18752F46D5244A40BFFAC2D01B5431P0\",\n" +
                "    \"pageNumber\":    0,\n" +
                "    \"pageSize\":      5\n" +
                "}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].username").value("Ivan3"))
        .andExpect(jsonPath("$.[0].remainingMoney").value(0))
        .andExpect(jsonPath("$.[0].paymentPlanMoney").value(4000))
        .andExpect(jsonPath("$.[0].planId").value("11752F46D5244A40BFFAC2D09B5431B0"));
  }

  @Test
  @WithUserDetails("Ivan1")
  public void show_all_payment_plans_successfully_with_username() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/plan/show/all/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "    \"sortCriteria\":  \"USERNAME\",\n" +
                "    \"orderCriteria\":  \"ASC\",\n" +
                "    \"username\": \"Ivan3\",\n" +
                "    \"pageNumber\":    0,\n" +
                "    \"pageSize\":      5\n" +
                "}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].username").value("Ivan3"))
        .andExpect(jsonPath("$.[0].remainingMoney").value(0))
        .andExpect(jsonPath("$.[0].paymentPlanMoney").value(4000))
        .andExpect(jsonPath("$.[0].planId").value("11752F46D5244A40BFFAC2D09B5431B0"));
  }

  @Test
  @WithUserDetails("Ivan1")
  public void show_all_payment_plans_successfully_with_name() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/plan/show/all/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "    \"sortCriteria\":  \"USERNAME\",\n" +
                "    \"orderCriteria\":  \"ASC\",\n" +
                "    \"name\": \"Ivan Ivailov Ivanov3\",\n" +
                "    \"pageNumber\":    0,\n" +
                "    \"pageSize\":      5\n" +
                "}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].username").value("Ivan3"))
        .andExpect(jsonPath("$.[0].remainingMoney").value(0))
        .andExpect(jsonPath("$.[0].paymentPlanMoney").value(4000))
        .andExpect(jsonPath("$.[0].planId").value("11752F46D5244A40BFFAC2D09B5431B0"));
  }

  @Test
  @WithUserDetails("Ivan1")
  public void show_all_payment_plans_successfully_amount_from_to_sort_by_remaining_to_paid_money_desc() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/plan/show/all/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "    \"amountFrom\": 3000   ,\n" +
                "    \"amountTo\":   4000   ,\n" +
                "    \"sortCriteria\":  \"REMAINING_TO_PAID_MONEY\",\n" +
                "    \"orderCriteria\":  \"DESC\",\n" +
                "    \"pageNumber\":    0,\n" +
                "    \"pageSize\":      5\n" +
                "}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].username").value("Ivan2"))
        .andExpect(jsonPath("$.[0].planId").value("18752F46D5244A40BFFAC2D09B5431B0"))
        .andExpect(jsonPath("$.[0].remainingMoney").value(3000))
        .andExpect(jsonPath("$.[0].url").value("http://localhost:8080/api/v1/payment/show/?pageNumber=0&pageCapacity=5&planId=18752F46D5244A40BFFAC2D09B5431B0"))
        .andExpect(jsonPath("$.[0].paymentPlanMoney").value(3000))
        .andExpect(jsonPath("$.[1].username").value("Ivan3"))
        .andExpect(jsonPath("$.[1].remainingMoney").value(0))
        .andExpect(jsonPath("$.[1].paymentPlanMoney").value(4000));
  }

  @Test
  @WithUserDetails("Ivan1")
  public void show_all_payment_plans_wrong_sort_criteria() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/plan/show/all/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "    \"amountFrom\": 3000   ,\n" +
                "    \"amountTo\":   4000   ,\n" +
                "    \"sortCriteria\":  \"REMAINING_TO_PAID_MON\",\n" +
                "    \"orderCriteria\":  \"DESC\",\n" +
                "    \"pageNumber\":    0,\n" +
                "    \"pageSize\":      5\n" +
                "}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("IvanAdmin")
  public void update_payment_plan_amount_successfully() throws Exception
  {
    mockMvc.perform(patch("/api/v1/payment/plan/change/amount")
            .queryParam("amount", "5000")
            .queryParam("planId", "66752F46D5244A40BFFAC2D09B5431B0"))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("IvanAdmin")
  public void update_payment_plan_amount_with_wrong_id() throws Exception
  {
    mockMvc.perform(patch("/api/v1/payment/plan/change/amount")
            .queryParam("amount", "5000")
            .queryParam("planId", "66752F46D5244A40BFFAC2D04B5431B0"))
        .andExpect(status().isBadRequest());
  }

}