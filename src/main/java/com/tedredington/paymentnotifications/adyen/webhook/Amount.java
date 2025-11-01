package com.tedredington.paymentnotifications.adyen.webhook;

public record Amount(
        String currency,
        Long value
) {}
