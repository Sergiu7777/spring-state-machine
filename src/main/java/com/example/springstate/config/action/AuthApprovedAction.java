package com.example.springstate.config.action;

import com.example.springstate.domain.PaymentEvent;
import com.example.springstate.domain.PaymentState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

@Slf4j
@Configuration
public class AuthApprovedAction implements Action<PaymentState, PaymentEvent> {

    @Override
    public void execute(StateContext<PaymentState, PaymentEvent> stateContext) {
        log.info("Pre Authorization was approved!!!");

    }
}
