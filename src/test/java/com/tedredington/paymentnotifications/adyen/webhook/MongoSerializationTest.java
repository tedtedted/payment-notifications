package com.tedredington.paymentnotifications.adyen.webhook;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class MongoSerializationTest {

    @Autowired
    private MongoConverter mongoConverter;

    @Test
    public void testSerializationFieldNamesMatchExistingDocumentStructure() {
        NotificationRequestItem item = new NotificationRequestItem(
                "AUTHORISATION", "TedRedingtonECOM", "testMerchantRef1", null,
                "N5TS7M2X4W3W9575", "1234:7777:12/2012", true,
                new Amount("EUR", 10100L),
                new Date(), "visa", List.of("CANCEL", "CAPTURE", "REFUND"),
                Map.of("hmacSignature", "testHmac", "cardSummary", "7777")
        );
        NotificationContainer container = new NotificationContainer(item);
        NotificationWrapper wrapper = new NotificationWrapper("false", List.of(container));

        Document document = new Document();
        mongoConverter.write(wrapper, document);

        // Top-level fields
        assertThat(document.getString("live")).isEqualTo("false");
        assertThat(document.containsKey("notificationItemContainers")).isTrue();
        assertThat(document.getString("_class"))
                .isEqualTo("com.tedredington.paymentnotifications.adyen.webhook.NotificationWrapper");

        // Container level
        List<Document> containers = document.getList("notificationItemContainers", Document.class);
        assertThat(containers).hasSize(1);
        Document containerDoc = containers.get(0);
        assertThat(containerDoc.containsKey("notificationItem")).isTrue();

        // Item level
        Document itemDoc = containerDoc.get("notificationItem", Document.class);
        assertThat(itemDoc.getString("eventCode")).isEqualTo("AUTHORISATION");
        assertThat(itemDoc.getString("merchantAccountCode")).isEqualTo("TedRedingtonECOM");
        assertThat(itemDoc.getString("merchantReference")).isEqualTo("testMerchantRef1");
        assertThat(itemDoc.getString("pspReference")).isEqualTo("N5TS7M2X4W3W9575");
        assertThat(itemDoc.getString("reason")).isEqualTo("1234:7777:12/2012");
        assertThat(itemDoc.getBoolean("success")).isTrue();
        assertThat(itemDoc.getString("paymentMethod")).isEqualTo("visa");
        assertThat(itemDoc.getList("operations", String.class))
                .containsExactly("CANCEL", "CAPTURE", "REFUND");

        // Amount
        Document amountDoc = itemDoc.get("amount", Document.class);
        assertThat(amountDoc.getLong("value")).isEqualTo(10100L);
        assertThat(amountDoc.getString("currency")).isEqualTo("EUR");

        // Additional data
        Document additionalDataDoc = itemDoc.get("additionalData", Document.class);
        assertThat(additionalDataDoc.getString("hmacSignature")).isEqualTo("testHmac");
    }
}
