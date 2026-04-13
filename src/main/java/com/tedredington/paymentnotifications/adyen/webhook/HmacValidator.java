package com.tedredington.paymentnotifications.adyen.webhook;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HmacValidator {

    public static boolean isHmacValid(NotificationRequestItem item, String hmacKey) {
        try {
            String dataToSign = String.join(":",
                    safe(item.pspReference()),
                    safe(item.originalReference()),
                    safe(item.merchantAccountCode()),
                    safe(item.merchantReference()),
                    safe(item.amount() != null ? item.amount().value().toString() : ""),
                    safe(item.amount() != null ? item.amount().currency() : ""),
                    safe(item.eventCode()),
                    safe(String.valueOf(item.success()))
            );

            SecretKeySpec signingKey = new SecretKeySpec(Base64.getDecoder().decode(hmacKey), "HmacSHA256");

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(dataToSign.getBytes(StandardCharsets.UTF_8));

            String calculatedHmac = Base64.getEncoder().encodeToString(rawHmac);
            return calculatedHmac.equals(item.additionalData().get("hmacSignature"));
        } catch (Exception e) {
            return false;
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace(":", "\\:");
    }
}
