package com.tedredington.paymentnotifications.adyen.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "notificationRequest")
public record NotificationWrapper(
        String live,
        @JsonProperty("notificationItems") List<NotificationContainer> notificationItemContainers
) {}
