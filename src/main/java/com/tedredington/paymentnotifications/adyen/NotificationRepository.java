package com.tedredington.paymentnotifications.adyen;

import com.tedredington.paymentnotifications.adyen.webhook.NotificationWrapper;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<NotificationWrapper, String> {
}
