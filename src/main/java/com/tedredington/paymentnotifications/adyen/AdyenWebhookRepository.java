package com.tedredington.paymentnotifications.adyen;

import com.adyen.model.notification.NotificationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdyenWebhookRepository extends MongoRepository<NotificationRequest, String> {
}
