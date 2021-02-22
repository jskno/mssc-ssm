package jskno.micro.msscssm.services;

import jskno.micro.msscssm.domain.Payment;
import jskno.micro.msscssm.domain.PaymentEvent;
import jskno.micro.msscssm.domain.PaymentState;
import jskno.micro.msscssm.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder().amount(new BigDecimal("12.99")).build();
    }

    @Transactional
    @Test
    void preAuthPayment() {
        Payment savedPayment = paymentService.newPayment(payment);
        System.out.println("Should be NEW");
        System.out.println(savedPayment.getPaymentState());

        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuthPayment(savedPayment.getId());
        Payment preAuthedPayment = paymentRepository.getOne(savedPayment.getId());
        System.out.println("Should be PRE_AUTH or PRE_AUTH_ERROR");
        System.out.println(sm.getState().getId());
        System.out.println(preAuthedPayment);
    }

    @Transactional
    @RepeatedTest(10)
    void authorizePayment() {
        Payment savedPayment = paymentService.newPayment(payment);
        System.out.println("Should be NEW");
        System.out.println(savedPayment.getPaymentState());

        StateMachine<PaymentState, PaymentEvent> preAuthSM =
                paymentService.preAuthPayment(savedPayment.getId());
        if (preAuthSM.getState().getId() == PaymentState.PRE_AUTH) {
            System.out.println("Payment is Pre Authorized");
            StateMachine<PaymentState, PaymentEvent> authSM = paymentService.authorizePayment(savedPayment.getId());
            System.out.println("Result of Auth: " + authSM.getState().getId());
        } else {
            System.out.println("Payment failed pre-auth...");
        }
    }
}
