package com.example.springstate.service;

import com.example.springstate.domain.Payment;
import com.example.springstate.domain.PaymentEvent;
import com.example.springstate.domain.PaymentState;
import org.springframework.statemachine.StateMachine;

public interface PaymentService {

    Payment newPayment(Payment payment);

    StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId) throws Exception;

    StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId) throws Exception;
}
