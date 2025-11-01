package com.tedredington.paymentnotifications.adyen.webhook;

import java.util.List;

public record NotificationWrapper(List<NotificationContainer> notificationItems) {
}

