package com.example.springstate.service;

import com.example.springstate.domain.Payment;
import com.example.springstate.domain.PaymentEvent;
import com.example.springstate.domain.PaymentState;
import com.example.springstate.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    public static final String PAYMENT_ID_HEADER = "payment_id";
    private final PaymentRepository paymentRepository;
    private final StateMachineFactory<PaymentState, PaymentEvent> factory;
    private final PaymentStateChangeInterceptor interceptor;

    @Override
    public Payment newPayment(Payment payment) {
        payment.setState(PaymentState.NEW);

        return paymentRepository.save(payment);
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId) throws Exception {
        StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
        sendEvent(paymentId, sm, PaymentEvent.AUTHORIZE);

        return null;
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId) throws Exception {
        StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
        sendEvent(paymentId, sm, PaymentEvent.AUTH_APPROVED);

        return null;
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId) throws Exception {
        StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
        sendEvent(paymentId, sm, PaymentEvent.AUTH_DECLINED);

        return null;
    }

    private void sendEvent(Long paymentId, StateMachine<PaymentState, PaymentEvent> sm, PaymentEvent event) {
        Message<PaymentEvent> message = MessageBuilder.withPayload(event)
                .setHeader(PAYMENT_ID_HEADER, paymentId)
                .build();

        sm.sendEvent(message);
    }

    private StateMachine<PaymentState, PaymentEvent> build(Long paymentId) throws Exception {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(Exception::new);

        StateMachine<PaymentState, PaymentEvent> sm = factory.getStateMachine(Long.toString(paymentId));
        sm.stop();


        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(interceptor);
                    sma.resetStateMachine(new DefaultStateMachineContext<>(payment.getState(), null, null, null));
                });

        sm.start();

        return sm;
    }
}
