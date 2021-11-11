package com.example.paymentplans.dao.payment;

import com.example.paymentplans.entities.Payment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PaymentDaoImpl implements PaymentDao
{
  private final NamedParameterJdbcOperations namedParameterJdbcOperations;

  public PaymentDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations){
    this.namedParameterJdbcOperations=namedParameterJdbcOperations;
  }

  public void insertPayment(String planId, BigDecimal amount){
    String sql=
        "INSERT INTO PAYMENTS                 "
       +"     (PAYMENT_ID,                    "
       +"      PLAN_ID,                       "
       +"      AMOUNT,                        "
       +"      PAYMENT_DATE_DT)               "
       +"   VALUES                            "
       +"     ((SELECT SYS_GUID() FROM dual), "
       +"     :planId,                        "
       +"     :amount,                        "
       +"     :date)                          ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("planId", planId)
        .addValue("amount", amount)
        .addValue("date",ZonedDateTime.now());

    namedParameterJdbcOperations.update(sql,parameters);
  }

  public List<Payment> getPayments(Pageable page,String planId){

    String sql =
        "SELECT   P.PAYMENT_ID,           "
       +"         P.PLAN_ID,              "
       +"         P.AMOUNT,               "
       +"         P.PAYMENT_DATE_DT       "
       +" FROM PAYMENTS P                 "
       +"WHERE    P.PLAN_ID = :planId     "
       +"ORDER BY P.PAYMENT_DATE_DT DESC  "
       +"OFFSET               :start      "
       +"ROWS FETCH NEXT      :end        "
       +"ROWS ONLY                        ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("planId", planId)
        .addValue("start",page.getPageNumber() * page.getPageSize())
        .addValue("end",page.getPageSize());

    return namedParameterJdbcOperations.query(sql,parameters,(rs, rowNum) ->
        new Payment(
            rs.getString("PAYMENT_ID"),
            rs.getString("PLAN_ID"),
            rs.getBigDecimal("AMOUNT"),
            rs.getObject("PAYMENT_DATE_DT", ZonedDateTime.class)));
  }

  public void updatePaymentAmount(BigDecimal amount,String paymentId){
    String sql =
        "UPDATE PAYMENTS P                   "
       +"SET   P.AMOUNT  = :amount           "
       +"WHERE P.PAYMENT_ID = :paymentId     ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("amount", amount)
        .addValue("paymentId", paymentId);

    namedParameterJdbcOperations.update(sql,parameters);
  }

  public Optional<String> checkIfPaymentExist(String paymentId){
    String sql =
             "SELECT PAYMENT_ID              "
            +"FROM PAYMENTS                  "
            +"WHERE PAYMENT_ID = :paymentId  ";

    MapSqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("paymentId",paymentId);

    try {
      return Optional.ofNullable(namedParameterJdbcOperations.queryForObject(sql, parameters,String.class));
    }catch (EmptyResultDataAccessException ex){
      return Optional.empty();
    }
  }

}
