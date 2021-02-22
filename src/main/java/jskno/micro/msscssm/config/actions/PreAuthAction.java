package jskno.micro.msscssm.config.actions;

import jskno.micro.msscssm.domain.PaymentEvent;
import jskno.micro.msscssm.domain.PaymentState;
import jskno.micro.msscssm.services.PaymentServiceImpl;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PreAuthAction implements Action<PaymentState, PaymentEvent> {

    @Override
    public void execute(StateContext<PaymentState, PaymentEvent> stateContext) {
        System.out.println("PreAuth was called!!!");
        if (new Random().nextInt(10) < 8) {
            System.out.println("Aproved");
            stateContext.getStateMachine()
                    .sendEvent(MessageBuilder.withPayload(PaymentEvent.PRE_AUTH_APPROVED)
                            .setHeader(PaymentServiceImpl.PAYMENT_ID,
                                    stateContext.getMessageHeader(PaymentServiceImpl.PAYMENT_ID))
                            .build());

        } else {
            System.out.println("Declined no credit!!!");
            stateContext.getStateMachine()
                    .sendEvent(MessageBuilder.withPayload(PaymentEvent.PRE_AUTH_DECLINED)
                            .setHeader(PaymentServiceImpl.PAYMENT_ID,
                                    stateContext.getMessageHeader(PaymentServiceImpl.PAYMENT_ID))
                            .build());
        }
    }
}
