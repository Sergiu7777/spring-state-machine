package com.example.springstate.service;

import com.example.springstate.domain.Payment;
import com.example.springstate.domain.PaymentEvent;
import com.example.springstate.domain.PaymentState;
import com.example.springstate.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
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
        payment = Payment.builder()
                .amount(new BigDecimal("17.99"))
                .build();
    }

    @Transactional
    @Test
    void preAuth() throws Exception {
        Payment savedPayment = paymentService.newPayment(payment);
        paymentService.preAuth(savedPayment.getId());

        Payment preAuthedPayment = paymentRepository.getById(savedPayment.getId());

        System.out.println(preAuthedPayment);
        assertNotNull(preAuthedPayment);
        assertEquals(preAuthedPayment.getId(), savedPayment.getId());
//        assertEquals(preAuthedPayment.getState(), PaymentState.PRE_AUTHORIZE);
    }

    @Transactional
    @Test
    void authorize() throws Exception {
        Payment savedPayment = paymentService.newPayment(payment);
        StateMachine<PaymentState, PaymentEvent> preAuthSM = paymentService.preAuth(savedPayment.getId());

//        assertEquals (preAuthSM.getState().getId(), PaymentState.PRE_AUTHORIZE);
        System.out.println("PreAuthorize state machine: " + preAuthSM.getState().getId());

        if (preAuthSM.getState().getId() == PaymentState.PRE_AUTHORIZE) {
            System.out.println("PreAuth passed!!!");
            StateMachine<PaymentState, PaymentEvent> authSM = paymentService.authorizePayment(savedPayment.getId());
            System.out.println("Auth state: " + authSM.getState().getId()
            );
        } else {
            System.out.println("PreAuth failed!!!");
        }

//        assertEquals(authSM.getState().getId(), PaymentState.PRE_AUTHORIZE);
//        assertEquals(authorizedPayment.getState(), PaymentState.AUTHORIZE);
    }
}