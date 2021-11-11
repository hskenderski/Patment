package com.example.paymentplans.services.paymentPlan;

import com.example.paymentplans.common.Validation;
import com.example.paymentplans.controllers.PaymentController;
import com.example.paymentplans.dao.paymentPlan.PaymentPlanDao;
import com.example.paymentplans.dto.PaymentPlanRequest;
import com.example.paymentplans.dto.PaymentPlanResponse;
import com.example.paymentplans.entities.Person;
import com.example.paymentplans.exceptions.InvalidException;
import com.example.paymentplans.services.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

import static com.example.paymentplans.common.ExceptionMessages.INVALID_PAYMENT_PLAN_ID;
import static com.example.paymentplans.common.OutputMessages.PAYMENT_PLAN_CREATE_SUCCESSFUL;
import static com.example.paymentplans.common.OutputMessages.SUCCESSFUL_CHANGED_PAYMENT_PLAN_AMOUNT;

@Transactional
@Service
public class PaymentPlanServiceImpl implements PaymentPlanService
{
  private final PaymentPlanDao paymentPlanDao;
  private final PersonService  personService;
  private final Validation validation;

  @Autowired
  public PaymentPlanServiceImpl(PaymentPlanDao paymentPlanDao, PersonService personService,Validation validation)
  {

    this.paymentPlanDao = paymentPlanDao;
    this.personService = personService;
    this.validation = validation;

  }

  public String createPaymentPlan(BigDecimal amount)
  {

    Person person = personService.getCurrentLoggedUser();
    paymentPlanDao.insertPaymentPlan(amount, person.getPersonId(),person.getUsername());
    return String.format(PAYMENT_PLAN_CREATE_SUCCESSFUL, person.getUsername(), amount);

  }

  public List<PaymentPlanResponse> showAllPaymentPlans(Pageable page, PaymentPlanRequest paymentPlanRequest)
  {

    validation.sortCriteriaInputValidation(paymentPlanRequest.getSortCriteria());

    List<PaymentPlanResponse> paymentPlans = paymentPlanDao.showPaymentPlans(page,paymentPlanRequest);

    for (PaymentPlanResponse paymentPlan : paymentPlans) {
      paymentPlan.setUrl(MvcUriComponentsBuilder.fromMethodName
              (PaymentController.class, "showPaymentsByPaymentPlan"
                  , page.getPageNumber()
                  , page.getPageSize()
                  , paymentPlan.getPlanId())
          .build()
          .toString());
    }
    return paymentPlans;

  }

  public String changePaymentPlanAmount(BigDecimal amount,String planId){

    paymentPlanDao.checkIfPaymentPlanExist(planId).
        orElseThrow(() -> new InvalidException(INVALID_PAYMENT_PLAN_ID));

    paymentPlanDao.updatePaymentPlanAmount(amount,planId);

    return SUCCESSFUL_CHANGED_PAYMENT_PLAN_AMOUNT;

  }

}
