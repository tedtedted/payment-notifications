package com.tedredington.paymentnotifications.adyen.webhook;

import java.util.Map;

public record NotificationRequestItem(
        String eventCode,
        String merchantAccountCode,
        String merchantReference,
        String originalReference,
        String pspReference,
        String success,
        Amount amount,
        Map<String, String> additionalData
) {}

