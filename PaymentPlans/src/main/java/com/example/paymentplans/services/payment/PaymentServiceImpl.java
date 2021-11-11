package com.example.paymentplans.services.payment;

import com.example.paymentplans.dao.payment.PaymentDao;
import com.example.paymentplans.dao.paymentPlan.PaymentPlanDao;
import com.example.paymentplans.entities.Payment;
import com.example.paymentplans.entities.PaymentPlan;
import com.example.paymentplans.exceptions.InvalidException;
import com.example.paymentplans.services.person.PersonService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.example.paymentplans.common.ExceptionMessages.*;
import static com.example.paymentplans.common.OutputMessages.PAYMENT_CREATE_SUCCESSFUL;
import static com.example.paymentplans.common.OutputMessages.SUCCESSFUL_CHANGED_PAYMENT_AMOUNT;

@Transactional
@Service
public class PaymentServiceImpl implements PaymentService
{
  private final PaymentDao     paymentDao;
  private final PaymentPlanDao paymentPlanDao;
  private final PersonService  personService;

  public PaymentServiceImpl(PaymentDao paymentDao, PaymentPlanDao paymentPlanDao, PersonService personService)
  {

    this.paymentDao = paymentDao;
    this.paymentPlanDao = paymentPlanDao;
    this.personService = personService;

  }

  public String createPayment(String planId, BigDecimal amount)
  {
    PaymentPlan paymentPlan = paymentPlanDao.showPaymentPlanMoneyByPlanIdAndPersonId(planId,
            personService.getCurrentLoggedUser().getPersonId()).
        orElseThrow(() -> new InvalidException(INVALID_PAYMENT_PLAN_ID));

    if (paymentPlan.getAmount().compareTo(BigDecimal.ZERO) == 0) {
      throw new InvalidException(PAYMENT_PLAN_ALREADY_PAID);
    }

    if (paymentPlan.getAmount().compareTo(amount) < 0) {
      throw new InvalidException(String.format(INVALID_MONEY_MORE_THAN_THE_PAYMENT_PLAN_AMOUNT, paymentPlan.getAmount()));
    }

    paymentDao.insertPayment(planId, amount);
    return String.format(PAYMENT_CREATE_SUCCESSFUL, planId, amount);

  }

  public List<Payment> showPayments(Pageable page, String planId)
  {

    String personId = personService.getCurrentLoggedUser().getPersonId();

    paymentPlanDao.checkIfPaymentPlanExist(personId, planId).
        orElseThrow(() -> new InvalidException(INVALID_PAYMENT_PLAN_ID));

    return paymentDao.getPayments(page, planId);

  }

  public String changePaymentAmount(BigDecimal amount, String paymentId)
  {
    paymentDao.checkIfPaymentExist(paymentId)
        .orElseThrow(() -> new InvalidException(INVALID_PAYMENT_ID));
    paymentDao.updatePaymentAmount(amount, paymentId);
    return SUCCESSFUL_CHANGED_PAYMENT_AMOUNT;
  }

}
