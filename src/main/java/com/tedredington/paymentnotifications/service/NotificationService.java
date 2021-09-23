package com.tedredington.paymentnotifications.service;

import com.adyen.model.notification.NotificationRequest;
import com.adyen.model.notification.NotificationRequestItem;
import com.adyen.util.HMACValidator;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.util.List;

@Service
public class NotificationService {


    String hmacKey = "b0ea55c2fe60d4d1d605e9c385e0e7";


    public void save(NotificationRequest notificationRequest) throws SignatureException {

        List<NotificationRequestItem> notificationRequestItems = notificationRequest.getNotificationItems();

        // Iterate through NotificationItems array ( there's not expected to be multiple )
        for (NotificationRequestItem notificationRequestItem : notificationRequestItems) {

            String hmacSignature = notificationRequestItem.getAdditionalData().get("hmacSignature");

            if (hmacSignature != null) {
                if (isHmacValid(notificationRequestItem, hmacSignature)) {
                    System.out.println("Hmac is valid!");
                } else {
                    System.out.println("Hmac is not valid :(");
                }
            } else {
                System.out.println("No Hmac, moving on anyway");
            }

        }
    }


    public boolean isHmacValid(NotificationRequestItem notificationRequestItem, String hmacSignature) {

        HMACValidator hmacValidator = new HMACValidator();

        try {
            if (hmacValidator.validateHMAC(notificationRequestItem, hmacKey)) {
                return true;
            }
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }
}
