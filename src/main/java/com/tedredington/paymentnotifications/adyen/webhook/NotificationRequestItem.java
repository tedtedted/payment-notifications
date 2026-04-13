package com.tedredington.paymentnotifications.adyen.webhook;

import java.util.Date;
import java.util.List;
import java.util.Map;

public record NotificationRequestItem(
        String eventCode,
        String merchantAccountCode,
        String merchantReference,
        String originalReference,
        String pspReference,
        String reason,
        boolean success,
        Amount amount,
        Date eventDate,
        String paymentMethod,
        List<String> operations,
        Map<String, String> additionalData
) {}
