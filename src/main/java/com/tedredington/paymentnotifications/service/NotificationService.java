package com.tedredington.paymentnotifications.service;

import com.adyen.model.notification.NotificationRequest;
import com.tedredington.paymentnotifications.domain.HmacStatus;
import com.tedredington.paymentnotifications.repo.NotificationRepository;
import com.tedredington.paymentnotifications.utils.HmacValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SignatureException;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationItemRepo;

    Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public void save(NotificationRequest notificationRequest) throws SignatureException {

        HmacStatus status = HmacValidation.validateHmac(notificationRequest);

        Logger logger = LoggerFactory.getLogger(NotificationService.class);

        // Fake HMAC from the Adyen docs
        String hmacKey = "b0ea55c2fe60d4d1d605e9c385e0e7";

        if (status.equals(HmacStatus.NOT_VALID)) {
            logger.info("HMAC Status: " + status + ". Not Moving Forward with Sending");
        } else {
            logger.info("HMAC Status: " + status + ". Moving Forward with Sending");
            notificationItemRepo.save(notificationRequest);

        }
    }
}
