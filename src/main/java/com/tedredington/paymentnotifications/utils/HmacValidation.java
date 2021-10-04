package com.tedredington.paymentnotifications.utils;

import com.adyen.model.notification.NotificationRequest;
import com.adyen.model.notification.NotificationRequestItem;
import com.adyen.util.HMACValidator;
import com.tedredington.paymentnotifications.domain.HmacStatus;

import java.security.SignatureException;
import java.util.List;

public class HmacValidation {

    public static HmacStatus validateHmac(NotificationRequest notificationRequest) {

        List<NotificationRequestItem> notificationRequestItems = notificationRequest.getNotificationItems();

        // Iterate through NotificationItems array ( there's not expected to be multiple )
        for (NotificationRequestItem notificationRequestItem : notificationRequestItems) {

            String hmacSignature;

            try {
                 hmacSignature = notificationRequestItem.getAdditionalData().get("hmacSignature");
            } catch (NullPointerException e){
                 hmacSignature = null;
            }

            System.out.println("HMAC SIGNATURE " + hmacSignature);

            if (hmacSignature != null ) {
                if (isHmacValid(notificationRequestItem, hmacSignature)) {
                    return HmacStatus.VALID;
                } else {
                    return HmacStatus.NOT_VALID;
                }
            }
        }
        return HmacStatus.MISSING;
    }

    public static boolean isHmacValid(NotificationRequestItem notificationRequestItem, String hmacSignature) {

        // Fake HMAC from Adyen docs
        String hmacKey = "b0ea55c2fe60d4d1d605e9c385e0e7";

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
