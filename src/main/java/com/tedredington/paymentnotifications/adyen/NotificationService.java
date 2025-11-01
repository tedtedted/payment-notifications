package com.tedredington.paymentnotifications.adyen;

import com.adyen.model.notification.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.util.List;

@Service
public class NotificationService {

    Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationItemRepository;

    public NotificationService(NotificationRepository notificationItemRepository) {
        this.notificationItemRepository = notificationItemRepository;
    }

    public void save(NotificationRequest notificationRequest) throws SignatureException {

     notificationItemRepository.save(notificationRequest);
    }
}
