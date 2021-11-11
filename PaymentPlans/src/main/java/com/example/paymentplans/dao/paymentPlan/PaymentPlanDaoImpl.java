package com.example.paymentplans.dao.paymentPlan;

import com.example.paymentplans.dto.PaymentPlanRequest;
import com.example.paymentplans.dto.PaymentPlanResponse;
import com.example.paymentplans.entities.PaymentPlan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class PaymentPlanDaoImpl implements PaymentPlanDao
{
  private final NamedParameterJdbcOperations namedParameterJdbcOperations;

  public PaymentPlanDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations){
    this.namedParameterJdbcOperations = namedParameterJdbcOperations;
  }

  public void insertPaymentPlan(BigDecimal amount,String personId,String username){
    String sql =
        "INSERT INTO PAYMENT_PLANS                   "
       +"       (PLAN_ID,                            "
       +"        PERSON_ID,                          "
       +"        AMOUNT)                             "
       +"     VALUES                                 "
       +"      ((SELECT SYS_GUID() FROM dual),       "
       +"       :personId,                           "
       +"       :amount)                             ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("username", username)
        .addValue("personId", personId)
        .addValue("amount", amount);

    namedParameterJdbcOperations.update(sql,parameters);
  }

  public List<PaymentPlanResponse> showPaymentPlans (Pageable page,PaymentPlanRequest paymentPlanRequest){

    final String sort = String.format("ORDER BY %s %s ",paymentPlanRequest.getSortCriteria(),paymentPlanRequest.getOrderCriteria());

    String sql =
        "SELECT   P.USERNAME,                      "
       +"         PP.PLAN_ID,                      "
       +"         PP.AMOUNT,                       "
       +" CASE WHEN                                "
       +"   PP.AMOUNT-SUM(P2.AMOUNT) IS NULL       "
       +"   THEN  PP.AMOUNT                        "
       +" ELSE    PP.AMOUNT-SUM(P2.AMOUNT)         "
       +" END REMAINING_TO_PAID_MONEY              "
       +" FROM PERSON P                            "
       +" JOIN PAYMENT_PLANS PP                    "
       +"  ON P.PERSON_ID = PP.PERSON_ID           "
       +" LEFT JOIN PAYMENTS P2                    "
       +"  ON PP.PLAN_ID = P2.PLAN_ID              "
       +"WHERE                                     "
       +"  PP.AMOUNT BETWEEN NVL(:minAmount,0)     "
       +"  AND NVL(:maxAmount,999999)              "
       +"AND PP.PLAN_ID LIKE NVL(:planId,'%')      "
       +"AND PP.PERSON_ID LIKE NVL(:personId,'%')  "
       +"AND P.USERNAME LIKE NVL(:username,'%')    "
       +"AND P.NAME LIKE NVL(:names,'%')           "
       +"GROUP BY PP.PLAN_ID,                      "
       +"         P.USERNAME,                      "
       +"         PP.AMOUNT                        "
       +sort
       +"OFFSET           :start                   "
       +"ROWS FETCH NEXT  :end                     "
       +"ROWS ONLY                                 ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("minAmount",paymentPlanRequest.getAmountFrom())
        .addValue("maxAmount",paymentPlanRequest.getAmountTo())
        .addValue("planId",paymentPlanRequest.getPlanId())
        .addValue("personId",paymentPlanRequest.getPersonId())
        .addValue("username",paymentPlanRequest.getUsername())
        .addValue("names",paymentPlanRequest.getName())
        .addValue("start",page.getPageNumber() * page.getPageSize())
        .addValue("end", page.getPageSize());

    return namedParameterJdbcOperations.query(sql,parameters,(rs, rowNum) ->
        new PaymentPlanResponse(
            rs.getString("USERNAME"),
            rs.getString("PLAN_ID"),
            rs.getBigDecimal("REMAINING_TO_PAID_MONEY"),
            rs.getBigDecimal("AMOUNT")));
  }

  public Optional<PaymentPlan> showPaymentPlanMoneyByPlanIdAndPersonId(String planId, String personId){
      String sql =
              " SELECT  CASE WHEN                         "
             +"         PP.AMOUNT-SUM(P2.AMOUNT) IS NULL  "
             +"         THEN  PP.AMOUNT                   "
             +"         ELSE    PP.AMOUNT-SUM(P2.AMOUNT)  "
             +"         END REMAINING_TO_PAID_MONEY       "
             +"  FROM PERSON P                            "
             +"  JOIN PAYMENT_PLANS PP                    "
             +"    ON P.PERSON_ID = PP.PERSON_ID          "
             +"  LEFT JOIN PAYMENTS P2                    "
             +"    ON PP.PLAN_ID = P2.PLAN_ID             "
             +"  GROUP BY PP.PLAN_ID,                     "
             +"           PP.AMOUNT,                      "
             +"           PP.PERSON_ID                    "
             +"  HAVING   PP.PLAN_ID = :planId            "
             +"       AND PP.PERSON_ID = :personId        ";

      MapSqlParameterSource parameters = new MapSqlParameterSource()
          .addValue("planId",planId)
          .addValue("personId",personId);

      try {
        return Optional.ofNullable(namedParameterJdbcOperations.queryForObject(sql, parameters, (rs, rowNum) ->
            new PaymentPlan(
                rs.getBigDecimal("REMAINING_TO_PAID_MONEY")
            )));
      }catch (EmptyResultDataAccessException ex){
        return Optional.empty();
      }
  }

  public Optional<String> checkIfPaymentPlanExist(String personId,String planId){
    String sql =
         "SELECT PLAN_ID              "
        +"FROM PAYMENT_PLANS          "
        +"WHERE PERSON_ID = :personId AND PLAN_ID = :planId";

    MapSqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("personId",personId)
        .addValue("planId",planId);

    try {
      return Optional.ofNullable(namedParameterJdbcOperations.queryForObject(sql, parameters,String.class));
    }catch (EmptyResultDataAccessException ex){
      return Optional.empty();
    }
  }

  public void updatePaymentPlanAmount(BigDecimal amount,String planId){
    String sql =
        "UPDATE PAYMENT_PLANS PP     "
       +"SET PP.AMOUNT = :amount     "
       +"WHERE PP.PLAN_ID = :planId  ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("amount", amount)
        .addValue("planId", planId);

    namedParameterJdbcOperations.update(sql,parameters);
  }

  public Optional<String> checkIfPaymentPlanExist(String planId){
    String sql =
             "SELECT PLAN_ID              "
            +"FROM PAYMENT_PLANS          "
            +"WHERE PLAN_ID = :planId     ";

    MapSqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("planId",planId);

    try {
      return Optional.ofNullable(namedParameterJdbcOperations.queryForObject(sql, parameters,String.class));
    }catch (EmptyResultDataAccessException ex){
      return Optional.empty();
    }
  }

}
