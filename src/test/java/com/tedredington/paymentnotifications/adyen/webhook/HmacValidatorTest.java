package com.tedredington.paymentnotifications.adyen.webhook;

import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HmacValidatorTest {

    private static final String HMAC_KEY = Base64.getEncoder().encodeToString("testHmacKey12345".getBytes(StandardCharsets.UTF_8));

    @Test
    public void testValidHmacSignature() throws Exception {
        NotificationRequestItem item = buildItem(
                "PSP_001", null, "TestMerchant", "ref-001",
                1000L, "EUR", "AUTHORISATION", true
        );
        String expectedHmac = computeHmac(item, HMAC_KEY);
        item = withHmacSignature(item, expectedHmac);

        assertThat(HmacValidator.isHmacValid(item, HMAC_KEY)).isTrue();
    }

    @Test
    public void testInvalidHmacSignature() {
        NotificationRequestItem item = buildItem(
                "PSP_001", null, "TestMerchant", "ref-001",
                1000L, "EUR", "AUTHORISATION", true
        );
        item = withHmacSignature(item, "invalidHmacValue");

        assertThat(HmacValidator.isHmacValid(item, HMAC_KEY)).isFalse();
    }

    @Test
    public void testMissingHmacSignature() {
        NotificationRequestItem item = buildItem(
                "PSP_001", null, "TestMerchant", "ref-001",
                1000L, "EUR", "AUTHORISATION", true
        );

        assertThat(HmacValidator.isHmacValid(item, HMAC_KEY)).isFalse();
    }

    @Test
    public void testNullAdditionalData() {
        NotificationRequestItem item = new NotificationRequestItem(
                "AUTHORISATION", "TestMerchant", "ref-001", null,
                "PSP_001", null, true, new Amount("EUR", 1000L),
                null, null, null, null
        );

        assertThat(HmacValidator.isHmacValid(item, HMAC_KEY)).isFalse();
    }

    @Test
    public void testWithOriginalReference() throws Exception {
        NotificationRequestItem item = buildItem(
                "CAPTURE_PSP_001", "PSP_001", "TestMerchant", "ref-001",
                1000L, "EUR", "CAPTURE", true
        );
        String expectedHmac = computeHmac(item, HMAC_KEY);
        item = withHmacSignature(item, expectedHmac);

        assertThat(HmacValidator.isHmacValid(item, HMAC_KEY)).isTrue();
    }

    @Test
    public void testWithNullAmount() throws Exception {
        NotificationRequestItem item = new NotificationRequestItem(
                "AUTHORISATION", "TestMerchant", "ref-001", null,
                "PSP_001", null, true, null,
                null, null, null, new HashMap<>()
        );
        String expectedHmac = computeHmac(item, HMAC_KEY);
        item = withHmacSignature(item, expectedHmac);

        assertThat(HmacValidator.isHmacValid(item, HMAC_KEY)).isTrue();
    }

    @Test
    public void testSpecialCharactersInFields() throws Exception {
        NotificationRequestItem item = buildItem(
                "PSP:002", null, "Merchant:Test", "ref\\001",
                2000L, "EUR", "AUTHORISATION", true
        );
        String expectedHmac = computeHmac(item, HMAC_KEY);
        item = withHmacSignature(item, expectedHmac);

        assertThat(HmacValidator.isHmacValid(item, HMAC_KEY)).isTrue();
    }

    @Test
    public void testSuccessFalse() throws Exception {
        NotificationRequestItem item = buildItem(
                "PSP_003", null, "TestMerchant", "ref-003",
                5000L, "EUR", "AUTHORISATION", false
        );
        String expectedHmac = computeHmac(item, HMAC_KEY);
        item = withHmacSignature(item, expectedHmac);

        assertThat(HmacValidator.isHmacValid(item, HMAC_KEY)).isTrue();
    }

    private NotificationRequestItem buildItem(String pspReference, String originalReference,
                                               String merchantAccountCode, String merchantReference,
                                               Long amountValue, String currency,
                                               String eventCode, boolean success) {
        return new NotificationRequestItem(
                eventCode, merchantAccountCode, merchantReference, originalReference,
                pspReference, null, success, new Amount(currency, amountValue),
                null, null, null, new HashMap<>()
        );
    }

    private NotificationRequestItem withHmacSignature(NotificationRequestItem item, String hmac) {
        Map<String, String> data = new HashMap<>(item.additionalData() != null ? item.additionalData() : Map.of());
        data.put("hmacSignature", hmac);
        return new NotificationRequestItem(
                item.eventCode(), item.merchantAccountCode(), item.merchantReference(),
                item.originalReference(), item.pspReference(), item.reason(), item.success(),
                item.amount(), item.eventDate(), item.paymentMethod(), item.operations(), data
        );
    }

    private String computeHmac(NotificationRequestItem item, String hmacKey) throws Exception {
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
        return Base64.getEncoder().encodeToString(rawHmac);
    }

    private static String safe(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace(":", "\\:");
    }
}
