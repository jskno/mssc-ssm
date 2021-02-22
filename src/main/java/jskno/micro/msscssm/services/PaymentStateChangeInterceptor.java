package jskno.micro.msscssm.services;

import jskno.micro.msscssm.domain.Payment;
import jskno.micro.msscssm.domain.PaymentEvent;
import jskno.micro.msscssm.domain.PaymentState;
import jskno.micro.msscssm.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentStateChangeInterceptor extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent> {

    private final PaymentRepository paymentRepository;

    @Override
    public void preStateChange(State<PaymentState, PaymentEvent> state, Message<PaymentEvent> message,
            Transition<PaymentState, PaymentEvent> transition, StateMachine<PaymentState,
            PaymentEvent> stateMachine, StateMachine<PaymentState, PaymentEvent> rootStateMachine) {

        Optional.ofNullable(message).ifPresent(msg -> {
            Optional.ofNullable(Long.class.cast(
                    msg.getHeaders().getOrDefault(PaymentServiceImpl.PAYMENT_ID, -1)))
                    .ifPresent(paymentId -> {
                        Payment payment = paymentRepository.getOne(paymentId);
                        payment.setPaymentState(state.getId());
                        paymentRepository.save(payment);
                    });
        });

    }
}
