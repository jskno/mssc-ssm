package jskno.micro.msscssm.config.guards;

import jskno.micro.msscssm.domain.PaymentEvent;
import jskno.micro.msscssm.domain.PaymentState;
import jskno.micro.msscssm.services.PaymentServiceImpl;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
public class PaymentIdGuard implements Guard<PaymentState, PaymentEvent> {

    @Override
    public boolean evaluate(StateContext<PaymentState, PaymentEvent> stateContext) {
        return stateContext.getMessageHeader(PaymentServiceImpl.PAYMENT_ID) != null;
    }
}
