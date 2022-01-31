package com.example.springstate.config;

import com.example.springstate.config.action.*;
import com.example.springstate.config.guard.PaymentIdGuard;
import com.example.springstate.domain.PaymentEvent;
import com.example.springstate.domain.PaymentState;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Slf4j
@EnableStateMachineFactory
@AllArgsConstructor
@Configuration
public class PaymentStateConfig extends StateMachineConfigurerAdapter<PaymentState, PaymentEvent> {

    private final StateContext<PaymentState, PaymentEvent> stateContext;
    private final AuthorizeAction authorizeAction;
    private final PreAuthAction preAuthAction;
    private final PreAuthApprovedAction preAuthApprovedAction;
    private final PreAuthDeclinedAction preAuthDeclinedAction;
    private final AuthApprovedAction authApprovedAction;
    private final AuthDeclinedAction authDeclinedAction;

    private final PaymentIdGuard paymentIdGuard;

    @Override
    public void configure(StateMachineStateConfigurer<PaymentState, PaymentEvent> states) throws Exception {
        states.withStates()
                .initial(PaymentState.NEW)
                .states(EnumSet.allOf(PaymentState.class))
                .end(PaymentState.AUTHORIZE)
                .end(PaymentState.PRE_AUTH_ERROR)
                .end(PaymentState.AUTH_ERROR);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PaymentState, PaymentEvent> transitions) throws Exception {
        transitions
                .withExternal().source(PaymentState.NEW).target(PaymentState.NEW).event(PaymentEvent.PRE_AUTHORIZE)
                    .action(preAuthAction).guard(paymentIdGuard)
                .and()
                .withExternal().source(PaymentState.NEW).target(PaymentState.PRE_AUTHORIZE).event(PaymentEvent.PRE_AUTH_APPROVED)
                    .action(preAuthApprovedAction)
                .and()
                .withExternal().source(PaymentState.NEW).target(PaymentState.PRE_AUTH_ERROR).event(PaymentEvent.PRE_AUTH_DECLINED)
                    .action(preAuthDeclinedAction)
                .and()
                .withExternal().source(PaymentState.PRE_AUTHORIZE).target(PaymentState.PRE_AUTHORIZE).event(PaymentEvent.AUTH_APPROVED)
                    .action(authApprovedAction)
                .and()
                .withExternal().source(PaymentState.PRE_AUTHORIZE).target(PaymentState.AUTHORIZE).event(PaymentEvent.AUTH_APPROVED)
                    .action(authorizeAction)
                .and()
                .withExternal().source(PaymentState.PRE_AUTHORIZE).target(PaymentState.AUTH_ERROR).event(PaymentEvent.AUTH_DECLINED)
                    .action(authDeclinedAction);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<PaymentState, PaymentEvent> config) throws Exception {
        StateMachineListenerAdapter<PaymentState, PaymentEvent> adapter = new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<PaymentState, PaymentEvent> from, State<PaymentState, PaymentEvent> to) {
                log.info("State changed from: {}, to: {}", from, to);
            }
        };

        config.withConfiguration().listener(adapter);
    }


}
