package com.tedredington.paymentnotifications.adyen;

import com.tedredington.paymentnotifications.adyen.webhook.NotificationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SignatureException;

@Service
public class NotificationService {

    Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationItemRepository;

    public NotificationService(NotificationRepository notificationItemRepository) {
        this.notificationItemRepository = notificationItemRepository;
    }

    public void save(NotificationWrapper notificationWrapper) throws SignatureException {

     notificationItemRepository.save(notificationWrapper);
    }
}
