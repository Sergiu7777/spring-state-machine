package com.example.springstate.domain;

public enum PaymentState {
    NEW,
    PRE_AUTHORIZE,
    PRE_AUTH_ERROR,
    AUTHORIZE,
    AUTH_ERROR
}
