package com.tedredington.paymentnotifications.adyen.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NotificationContainer(
        @JsonProperty("NotificationRequestItem") NotificationRequestItem notificationItem
) {}
