package com.example.springstate.service;

import com.example.springstate.domain.Payment;
import com.example.springstate.domain.PaymentEvent;
import com.example.springstate.domain.PaymentState;
import com.example.springstate.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class PaymentStateChangeInterceptor extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent> {
    private final PaymentRepository paymentRepository;

    @Override
    public void preStateChange(State<PaymentState, PaymentEvent> state,
                               Message<PaymentEvent> message,
                               Transition<PaymentState, PaymentEvent> transition,
                               StateMachine<PaymentState, PaymentEvent> stateMachine) {

        Optional.ofNullable(message)
                .flatMap(msg -> Optional.ofNullable((Long) msg.getHeaders()
                        .getOrDefault(PaymentServiceImpl.PAYMENT_ID_HEADER, -1L)))
                .ifPresent(paymentId -> {
                    Payment payment = paymentRepository.findById(paymentId).orElseThrow();
                    payment.setState(state.getId());
                    paymentRepository.save(payment);
                });
    }
}
