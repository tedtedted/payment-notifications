package com.tedredington.paymentnotifications.service;

import com.adyen.model.notification.NotificationRequest;
import com.adyen.model.notification.NotificationRequestItem;
import com.adyen.util.HMACValidator;
import com.tedredington.paymentnotifications.domain.EventTypes;
import com.tedredington.paymentnotifications.domain.HmacStatus;
import com.tedredington.paymentnotifications.utils.HmacValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.util.List;

@Service
public class NotificationService {

    Logger logger = LoggerFactory.getLogger(NotificationService.class);

    // Fake HMAC from the Adyen docs
    String hmacKey = "b0ea55c2fe60d4d1d605e9c385e0e7";

    public void save(NotificationRequest notificationRequest) throws SignatureException {

        HmacStatus status = HmacValidation.validateHmac(notificationRequest);

        if (status.equals(HmacStatus.NOT_VALID)) {
            logger.info("HMAC Status: " + status +  ". Not Moving Forward with Sending");
        } else {
            logger.info("HMAC Status: " + status + ". Moving Forward with Sending to Queue");
        }
    }
}
