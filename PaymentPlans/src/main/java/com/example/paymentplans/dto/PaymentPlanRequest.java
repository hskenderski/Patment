package com.example.paymentplans.dto;

import javax.validation.constraints.*;
import java.math.BigDecimal;

import static com.example.paymentplans.common.ExceptionMessages.*;

public class PaymentPlanRequest
{
  @DecimalMin(value = "0.0", message = INVALID_MONEY)
  @Digits(integer = 6, fraction = 2, message = INVALID_MONEY)
  private BigDecimal amountFrom;

  @DecimalMin(value = "0.0", inclusive = false, message = INVALID_MONEY)
  @Digits(integer = 6, fraction = 2, message = INVALID_MONEY)
  private BigDecimal amountTo;

  @Size(min = 32, max = 32, message = INVALID_UUID)
  private String planId;

  @Size(min = 32, max = 32, message = INVALID_UUID)
  private String personId;

  @Pattern(regexp = "^[A-Za-z0-9.\\-]+$", message = INVALID_USERNAME)
  @Size(min = 5, message = USERNAME_SHOULD_BE_MIN_5_SYMBOLS)
  @Size(max = 50, message = USERNAME_SHOULD_BE_MAX_50_SYMBOLS)
  private String username;

  @Size(min = 5, message = NAME_SHOULD_BE_MIN_5_LETTERS)
  @Size(max = 100, message = NAME_SHOULD_BE_MAX_100_LETTERS)
  private String name;

  @Pattern(regexp = "^[A-Za-z_]+$", message = INVALID_INPUT)
  private String sortCriteria = "REMAINING_TO_PAID_MONEY";

  @Pattern(regexp = "^[A-Za-z]+$", message = INVALID_INPUT)
  @Size(min = 3, max = 4)
  private String orderCriteria = "ASC";

  @Min(value = 0)
  private Integer pageNumber = 0;

  @Min(value = 1)
  @Max(value = 100)
  private Integer pageSize = 100;

  public BigDecimal getAmountFrom()
  {
    return amountFrom;
  }

  public void setAmountFrom(BigDecimal amountFrom)
  {
    this.amountFrom = amountFrom;
  }

  public BigDecimal getAmountTo()
  {
    return amountTo;
  }

  public void setAmountTo(BigDecimal amountTo)
  {
    this.amountTo = amountTo;
  }

  public String getPlanId()
  {
    return planId;
  }

  public void setPlanId(String planId)
  {
    this.planId = planId;
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

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getSortCriteria()
  {
    return sortCriteria;
  }

  public void setSortCriteria(String sortCriteria)
  {
    this.sortCriteria = sortCriteria;
  }

  public Integer getPageNumber()
  {
    return pageNumber;
  }

  public void setPageNumber(Integer pageNumber)
  {
    this.pageNumber = pageNumber;
  }

  public Integer getPageSize()
  {
    return pageSize;
  }

  public void setPageSize(Integer pageSize)
  {
    this.pageSize = pageSize;
  }

  public String getOrderCriteria()
  {
    return orderCriteria;
  }

  public void setOrderCriteria(String orderCriteria)
  {
    this.orderCriteria = orderCriteria;
  }
}
