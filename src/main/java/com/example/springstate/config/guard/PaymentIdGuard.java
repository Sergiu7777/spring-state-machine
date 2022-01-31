package com.example.springstate.config.guard;

import com.example.springstate.domain.PaymentEvent;
import com.example.springstate.domain.PaymentState;
import com.example.springstate.service.PaymentServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

@Configuration
public class PaymentIdGuard implements Guard<PaymentState, PaymentEvent> {

    @Override
    public boolean evaluate(StateContext<PaymentState, PaymentEvent> stateContext) {
        return stateContext.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER) != null;
    }
}
