package com.tedredington.AdyenNotifications.service;

import com.adyen.model.Amount;
import com.adyen.model.notification.NotificationRequest;
import com.adyen.model.notification.NotificationRequestItem;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Async
@Service
public class NotificationService {

    public NotificationService() {}

    public void save(NotificationRequest notification){

        String adyen_environment = notification.getLive();

        List<NotificationRequestItem> notificationItems = notification.getNotificationItems();

        notificationItems.forEach(item-> {

            // Data fields to match Impl Ninja
            String merchantAccount = item.getMerchantAccountCode();
            Date eventDate = item.getEventDate();
            String eventCode = item.getEventCode();
            String pspReference = item.getPspReference();
            String origPspReference = item.getOriginalReference();
            String merchantRef = item.getMerchantReference();
            Boolean success = item.isSuccess();

            Amount amount = item.getAmount();
            Long value = amount.getValue();
            String currency = amount.getCurrency();

            String reason = item.getReason();
            String paymentMethod = item.getPaymentMethod();

            Map<String, String> additionalData = item.getAdditionalData();
            String avsResult = additionalData.get("avsResult");
            String cvcResult = additionalData.get("cvcResult");
            String acquirerAccountCode = additionalData.get("acquirerAccountCode");
            String acquirerCode = additionalData.get("acquirerCode");
            String authCode = additionalData.get("authCode");
            String issuerCountry = additionalData.get("issuerCountry");
            String cardPaymentMethod = additionalData.get("cardPaymentMethod");
            String fundingSource = additionalData.get("fundingSource");
            String shopperInteraction = additionalData.get("shopperInteraction");
            String alias = additionalData.get("alias");
            String cardSummary = additionalData.get("cardSummary");
            String cardBin = additionalData.get("cardBin");
            String totalFraudScore = additionalData.get("totalFraudScore");

            if (success.equals("true")) {
                String operations = item.getOperations().toString();
            }

            System.out.println(item);

        });
    }
}