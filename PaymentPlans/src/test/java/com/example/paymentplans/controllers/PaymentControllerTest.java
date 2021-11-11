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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@Sql(scripts = {"/QueriesForTest.sql"})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class PaymentControllerTest
{

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;


  @BeforeEach
  public void setup(WebApplicationContext webApplicationContext,RestDocumentationContextProvider restDocumentation)
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
  public void create_instalment_successfully() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/installment/")
            .queryParam("planId","18752F46D5244A40BFFAC2D01B5431B0")
            .queryParam("amount","200"))
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("Ivan1")
  public void create_instalment_with_payment_plan_id_length_less_than_32() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/installment/")
        .queryParam("planId","18752F46D524A40BFFAC2D01B5431B0")
        .queryParam("amount","200"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan1")
  public void create_instalment_with_payment_plan_id_length_more_than_32() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/installment/")
            .queryParam("planId","18752F46D524A40BFFAC2D01B5F431B0")
            .queryParam("amount","200"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan1")
  public void create_instalment_with_incorrect_payment_plan_id() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/installment/")
            .queryParam("planId","18752F46D523A40BFFAC2D01B5F431B0")
            .queryParam("amount","200"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan1")
  public void create_instalment_with_negative_amount() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/installment/")
            .queryParam("planId","18752F46D5244A40BFFAC2D01B5431B0")
            .queryParam("amount","-200"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan1")
  public void create_instalment_with_zero_amount() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/installment/?planId=18752F46D5244A40BFFAC2D01B5431B0&amount=0")
            .queryParam("planId","18752F46D5244A40BFFAC2D01B5431B0")
            .queryParam("amount","0"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan1")
  public void create_instalment_with_incorrect_Decimal_integer_bigger_than_6_digits() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/installment/")
            .queryParam("planId","18752F46D5244A40BFFAC2D01B5431B0")
            .queryParam("amount","2000000"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan1")
  public void create_instalment_with_incorrect_Decimal_fraction() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/installment/")
            .queryParam("planId","18752F46D5244A40BFFAC2D01B5431B0")
            .queryParam("amount","222.222"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan2")
  public void create_instalment_with_more_money_than_payment_plan_amount() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/installment/")
            .queryParam("planId","18752F46D5244A40BFFAC2D09B5431B0")
            .queryParam("amount","10000"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan3")
  public void create_instalment_for_already_payed_payment_plan() throws Exception
  {
    mockMvc.perform(post("/api/v1/payment/installment/")
            .queryParam("planId","11752F46D5244A40BFFAC2D09B5431B0")
            .queryParam("amount","10000"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan3")
  public void show_payments_by_payment_plan_id_successfully() throws Exception
  {
    mockMvc.perform(get("/api/v1/payment/show/")
            .queryParam("pageNumber","0")
            .queryParam("pageCapacity","5")
            .queryParam("planId","11752F46D5244A40BFFAC2D09B5431B0"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].paymentId").value("2F3C983122344D4C95E34A22CE576B67"))
        .andExpect(jsonPath("$.[0].planId").value("11752F46D5244A40BFFAC2D09B5431B0"))
        .andExpect(jsonPath("$.[0].amount").value(1000))
        .andExpect(jsonPath("$.[0].paymentDateDt").isEmpty())
        .andExpect(jsonPath("$.[1].paymentId").value("2F3C983122344D4C95E34A22CE576B68"))
        .andExpect(jsonPath("$.[1].planId").value("11752F46D5244A40BFFAC2D09B5431B0"))
        .andExpect(jsonPath("$.[1].amount").value(1000))
        .andExpect(jsonPath("$.[1].paymentDateDt").isEmpty())
        .andExpect(jsonPath("$.[2].paymentId").value("2F3C983122344D4C95E34A22CE576B69"))
        .andExpect(jsonPath("$.[2].planId").value("11752F46D5244A40BFFAC2D09B5431B0"))
        .andExpect(jsonPath("$.[2].amount").value(2000))
        .andExpect(jsonPath("$.[2].paymentDateDt").isEmpty());
  }

  @Test
  @WithUserDetails("Ivan3")
  public void show_payments_by_payment_plan_id_with_empty_payment_plan_id() throws Exception
  {
    mockMvc.perform(get("/api/v1/payment/show/")
            .queryParam("pageNumber","0")
            .queryParam("pageCapacity","5"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan3")
  public void show_payments_by_payment_plan_id_with_invalid_payment_plan_id() throws Exception
  {
    mockMvc.perform(get("/api/v1/payment/show/")
            .queryParam("pageNumber","0")
            .queryParam("pageCapacity","5")
            .queryParam("planId","11752F46D5444A40BFFAC2D09B5431B0"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan3")
  public void show_payments_by_payment_plan_id_with_payment_plan_id_size_less_than_32() throws Exception
  {
    mockMvc.perform(get("/api/v1/payment/show/")
            .queryParam("pageNumber","0")
            .queryParam("pageCapacity","5")
            .queryParam("planId","11752F46D5444A40BFFC2D09B5431B0"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("Ivan3")
  public void show_payments_by_payment_plan_id_with_payment_plan_id_size_more_than_32() throws Exception
  {
    mockMvc.perform(get("/api/v1/payment/show/")
            .queryParam("pageNumber","0")
            .queryParam("pageCapacity","5")
            .queryParam("planId","11752F46D5444A40BFFC2D09B54311752F46D5444A40BFFC2D09B5431B01B0"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("IvanAdmin")
  public void update_payment_amount_successfully() throws Exception
  {
    mockMvc.perform(patch("/api/v1/payment/change/amount")
            .queryParam("amount","200")
            .queryParam("paymentId","2F3C983122344D0C95E34A22CE576B69"))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("IvanAdmin")
  public void update_payment_amount_with_wrong_id() throws Exception
  {
    mockMvc.perform(patch("/api/v1/payment/change/amount")
            .queryParam("amount","200")
            .queryParam("paymentId","2F3C983122346D0C95E34A22CE576B69"))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }




}