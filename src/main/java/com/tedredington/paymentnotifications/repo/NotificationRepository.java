package com.tedredington.paymentnotifications.repo;

import com.adyen.model.notification.NotificationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<NotificationRequest, String> {
}