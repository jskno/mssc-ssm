package jskno.micro.msscssm.services;

import jskno.micro.msscssm.domain.Payment;
import jskno.micro.msscssm.domain.PaymentEvent;
import jskno.micro.msscssm.domain.PaymentState;
import org.springframework.statemachine.StateMachine;

public interface PaymentService {

    Payment newPayment(Payment payment);

    StateMachine<PaymentState, PaymentEvent> preAuthPayment(Long paymentId);

    StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId);
}
