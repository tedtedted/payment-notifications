package com.tedredington.paymentnotifications.adyen;

import com.adyen.model.notification.NotificationRequest;
import com.adyen.model.notification.NotificationRequestItem;
import com.adyen.util.HMACValidator;
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

        HmacStatus status = HmacValidation.validateHmac(notificationRequest);

        // Fake HMAC from the Adyen docs
        String hmacKey = "b0ea55c2fe60d4d1d605e9c385e0e7";

        if (status.equals(HmacStatus.NOT_VALID)) {
            logger.info("HMAC Status: " + status + ". Not Moving Forward with Sending");
        } else {
            logger.info("HMAC Status: " + status + ". Moving Forward with Sending");
            notificationItemRepository.save(notificationRequest);
        }
    }

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

                if (hmacSignature != null) {
                    if (isHmacValid(notificationRequestItem, hmacSignature)) {
                        return HmacStatus.VALID;
                    } else {
                        return HmacStatus.NOT_VALID;
                    }
                }

                if (hmacSignature == null || hmacSignature.isBlank()) {
                    return HmacStatus.MISSING;
                }
            }
            return HmacStatus.UNKNOWN;
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

    public enum EventTypes {
        EVENTS, REPORTS, ISSUING
    }

    public enum HmacStatus {
        VALID,NOT_VALID, MISSING, UNKNOWN;
    }
}
