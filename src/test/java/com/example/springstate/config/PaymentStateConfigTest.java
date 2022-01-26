package com.example.springstate.config;

import com.example.springstate.domain.PaymentEvent;
import com.example.springstate.domain.PaymentState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentStateConfigTest {
    @Autowired
    StateMachineFactory<PaymentState, PaymentEvent> factory;

    @Test
    void testNewStateMachine() {
        StateMachine<PaymentState, PaymentEvent> machine = factory.getStateMachine(UUID.randomUUID());
        machine.start();

        System.out.println(machine.getState().toString());

        machine.sendEvent(PaymentEvent.PRE_AUTHORIZE);
        System.out.println(machine.getState().toString());
        assertEquals(machine.getState().getId(), PaymentState.NEW);

        machine.sendEvent(PaymentEvent.PRE_AUTH_APPROVED);
        System.out.println(machine.getState().toString());
        assertEquals(machine.getState().getId(), PaymentState.PRE_AUTH);
    }
}