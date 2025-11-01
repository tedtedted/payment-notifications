package com.tedredington.paymentnotifications.adyen.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
public class NotificationWrapperDeserializationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testDeserializeNotificationWrapper() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "additionalData": {
                      "tokenization.shopperReference": "YOUR_SHOPPER_REFERENCE",
                      "tokenization.storedPaymentMethodId": "M5N7TQ4TG5PFWR50",
                      "tokenization.store.operationType": "created"
                    },
                    "amount": {
                      "currency": "EUR",
                      "value": 1000
                    },
                    "eventCode": "AUTHORISATION",
                    "eventDate": "2021-01-01T01:00:00+01:00",
                    "merchantAccountCode": "YOUR_MERCHANT_ACCOUNT",
                    "merchantReference": "YOUR_MERCHANT_REFERENCE",
                    "paymentMethod": "ach",
                    "operations": [
                      "CANCEL",
                      "CAPTURE",
                      "REFUND"
                    ],
                    "pspReference": "QFQTPCQ8HXSKGK82",
                    "reason": "null",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItems().get(0).NotificationRequestItem();

        assertThat(item.eventCode()).isEqualTo("AUTHORISATION");
        assertThat(item.amount().currency()).isEqualTo("EUR");
        assertThat(item.amount().value()).isEqualTo(1000L);
        assertThat(item.success()).isEqualTo("true");
    }
}