package com.tedredington.paymentnotifications.adyen.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class NotificationWrapperDeserializationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAuthorisationWithFullPayload() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "additionalData": {
                      "hmacSignature": "C8hPB6UcY3kS4dirYQ+ud7rnqGwkrmqtQ9B5BDjcFwY=",
                      "cardSummary": "7777",
                      "shopperIP": "127.0.0.1",
                      "paymentMethodVariant": "visa"
                    },
                    "amount": {
                      "currency": "EUR",
                      "value": 10100
                    },
                    "eventCode": "AUTHORISATION",
                    "eventDate": "2026-02-27T22:40:32+01:00",
                    "merchantAccountCode": "TedRedingtonECOM",
                    "merchantReference": "testMerchantRef1",
                    "pspReference": "N5TS7M2X4W3W9575",
                    "reason": "1234:7777:12/2012",
                    "success": "true",
                    "paymentMethod": "visa",
                    "operations": ["CANCEL", "CAPTURE", "REFUND"]
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);

        assertThat(wrapper.live()).isEqualTo("false");
        assertThat(wrapper.notificationItemContainers()).hasSize(1);

        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();
        assertThat(item.eventCode()).isEqualTo("AUTHORISATION");
        assertThat(item.success()).isTrue();
        assertThat(item.amount().currency()).isEqualTo("EUR");
        assertThat(item.amount().value()).isEqualTo(10100L);
        assertThat(item.merchantAccountCode()).isEqualTo("TedRedingtonECOM");
        assertThat(item.merchantReference()).isEqualTo("testMerchantRef1");
        assertThat(item.pspReference()).isEqualTo("N5TS7M2X4W3W9575");
        assertThat(item.reason()).isEqualTo("1234:7777:12/2012");
        assertThat(item.paymentMethod()).isEqualTo("visa");
        assertThat(item.operations()).containsExactly("CANCEL", "CAPTURE", "REFUND");
        assertThat(item.additionalData()).containsEntry("hmacSignature", "C8hPB6UcY3kS4dirYQ+ud7rnqGwkrmqtQ9B5BDjcFwY=");
        assertThat(item.additionalData()).containsEntry("cardSummary", "7777");
        assertThat(item.eventDate()).isNotNull();
    }

    @Test
    public void testCapture() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 5000 },
                    "eventCode": "CAPTURE",
                    "eventDate": "2026-03-01T10:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "order-123",
                    "originalReference": "N5TS7M2X4W3W9575",
                    "pspReference": "CAPTURE_PSP_001",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.eventCode()).isEqualTo("CAPTURE");
        assertThat(item.success()).isTrue();
        assertThat(item.originalReference()).isEqualTo("N5TS7M2X4W3W9575");
        assertThat(item.amount().value()).isEqualTo(5000L);
    }

    @Test
    public void testRefund() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "USD", "value": 2500 },
                    "eventCode": "REFUND",
                    "eventDate": "2026-03-02T14:30:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "order-456",
                    "originalReference": "ORIG_PSP_002",
                    "pspReference": "REFUND_PSP_001",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.eventCode()).isEqualTo("REFUND");
        assertThat(item.success()).isTrue();
        assertThat(item.originalReference()).isEqualTo("ORIG_PSP_002");
        assertThat(item.amount().currency()).isEqualTo("USD");
    }

    @Test
    public void testRefundFailed() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 1500 },
                    "eventCode": "REFUND_FAILED",
                    "eventDate": "2026-03-03T09:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "order-789",
                    "originalReference": "ORIG_PSP_003",
                    "pspReference": "REFUND_FAIL_001",
                    "reason": "Insufficient balance",
                    "success": "false"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.eventCode()).isEqualTo("REFUND_FAILED");
        assertThat(item.success()).isFalse();
        assertThat(item.reason()).isEqualTo("Insufficient balance");
    }

    @Test
    public void testCancellation() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 7500 },
                    "eventCode": "CANCELLATION",
                    "eventDate": "2026-03-04T11:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "order-cancel-1",
                    "originalReference": "ORIG_PSP_004",
                    "pspReference": "CANCEL_PSP_001",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.eventCode()).isEqualTo("CANCELLATION");
        assertThat(item.success()).isTrue();
        assertThat(item.originalReference()).isEqualTo("ORIG_PSP_004");
    }

    @Test
    public void testCancelOrRefund() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "additionalData": { "modification.action": "refund" },
                    "amount": { "currency": "EUR", "value": 3000 },
                    "eventCode": "CANCEL_OR_REFUND",
                    "eventDate": "2026-03-05T12:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "order-cor-1",
                    "originalReference": "ORIG_PSP_005",
                    "pspReference": "COR_PSP_001",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.eventCode()).isEqualTo("CANCEL_OR_REFUND");
        assertThat(item.additionalData()).containsEntry("modification.action", "refund");
    }

    @Test
    public void testChargebackReversed() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "GBP", "value": 9900 },
                    "eventCode": "CHARGEBACK_REVERSED",
                    "eventDate": "2026-03-06T15:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "order-cb-1",
                    "originalReference": "ORIG_PSP_006",
                    "pspReference": "CBR_PSP_001",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.eventCode()).isEqualTo("CHARGEBACK_REVERSED");
        assertThat(item.amount().currency()).isEqualTo("GBP");
    }

    @Test
    public void testReportAvailable() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 0 },
                    "eventCode": "REPORT_AVAILABLE",
                    "eventDate": "2026-02-25T23:01:03+01:00",
                    "merchantAccountCode": "JPMora",
                    "merchantReference": "",
                    "pspReference": "settlement_report_aggregate.csv",
                    "reason": "https://ca-test.adyen.com/reports/download/report.csv",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.eventCode()).isEqualTo("REPORT_AVAILABLE");
        assertThat(item.amount().value()).isEqualTo(0L);
        assertThat(item.paymentMethod()).isNull();
        assertThat(item.operations()).isNull();
    }

    @Test
    public void testRecurringContract() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "additionalData": {
                      "shopperReference": "SHOPPER_001",
                      "recurringDetailReference": "REC_DETAIL_001"
                    },
                    "amount": { "currency": "EUR", "value": 0 },
                    "eventCode": "RECURRING_CONTRACT",
                    "eventDate": "2026-03-07T10:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "recurring-setup-1",
                    "pspReference": "REC_PSP_001",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.eventCode()).isEqualTo("RECURRING_CONTRACT");
        assertThat(item.additionalData()).containsEntry("shopperReference", "SHOPPER_001");
    }

    @Test
    public void testNotificationOfFraud() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 20000 },
                    "eventCode": "NOTIFICATION_OF_FRAUD",
                    "eventDate": "2026-03-08T08:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "fraud-order-1",
                    "originalReference": "ORIG_PSP_007",
                    "pspReference": "FRAUD_PSP_001",
                    "reason": "Fraudulent transaction reported",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.eventCode()).isEqualTo("NOTIFICATION_OF_FRAUD");
        assertThat(item.reason()).isEqualTo("Fraudulent transaction reported");
    }

    @Test
    public void testPending() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "BRL", "value": 4500 },
                    "eventCode": "PENDING",
                    "eventDate": "2026-03-09T16:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "pending-order-1",
                    "pspReference": "PENDING_PSP_001",
                    "paymentMethod": "boleto",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.eventCode()).isEqualTo("PENDING");
        assertThat(item.paymentMethod()).isEqualTo("boleto");
        assertThat(item.amount().currency()).isEqualTo("BRL");
    }

    @Test
    public void testAuthorisationAdjustment() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 15000 },
                    "eventCode": "AUTHORISATION_ADJUSTMENT",
                    "eventDate": "2026-03-10T13:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "adjust-order-1",
                    "originalReference": "ORIG_PSP_008",
                    "pspReference": "ADJUST_PSP_001",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.eventCode()).isEqualTo("AUTHORISATION_ADJUSTMENT");
        assertThat(item.originalReference()).isEqualTo("ORIG_PSP_008");
        assertThat(item.amount().value()).isEqualTo(15000L);
    }

    @Test
    public void testManualReviewReject() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 8800 },
                    "eventCode": "MANUAL_REVIEW_REJECT",
                    "eventDate": "2026-03-11T09:30:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "review-order-1",
                    "originalReference": "ORIG_PSP_009",
                    "pspReference": "REVIEW_PSP_001",
                    "reason": "Rejected by manual review",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.eventCode()).isEqualTo("MANUAL_REVIEW_REJECT");
        assertThat(item.reason()).isEqualTo("Rejected by manual review");
    }

    @Test
    public void testAuthentication() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 5500 },
                    "eventCode": "AUTHENTICATION",
                    "eventDate": "2026-03-12T11:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "auth-3ds-1",
                    "pspReference": "AUTH3DS_PSP_001",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.eventCode()).isEqualTo("AUTHENTICATION");
    }

    @Test
    public void testPayoutThirdparty() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 30000 },
                    "eventCode": "PAYOUT_THIRDPARTY",
                    "eventDate": "2026-03-13T14:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "payout-1",
                    "pspReference": "PAYOUT_PSP_001",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.eventCode()).isEqualTo("PAYOUT_THIRDPARTY");
        assertThat(item.amount().value()).isEqualTo(30000L);
    }

    @Test
    public void testOrderOpenedAndClosed() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 12000 },
                    "eventCode": "ORDER_OPENED",
                    "eventDate": "2026-03-14T10:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "order-open-1",
                    "pspReference": "ORDER_OPEN_PSP_001",
                    "success": "true"
                  }
                },
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 12000 },
                    "eventCode": "ORDER_CLOSED",
                    "eventDate": "2026-03-14T11:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "order-open-1",
                    "originalReference": "ORDER_OPEN_PSP_001",
                    "pspReference": "ORDER_CLOSE_PSP_001",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);

        assertThat(wrapper.notificationItemContainers()).hasSize(2);
        assertThat(wrapper.notificationItemContainers().get(0).notificationItem().eventCode()).isEqualTo("ORDER_OPENED");
        assertThat(wrapper.notificationItemContainers().get(1).notificationItem().eventCode()).isEqualTo("ORDER_CLOSED");
    }

    @Test
    public void testRefundedReversed() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 2000 },
                    "eventCode": "REFUNDED_REVERSED",
                    "eventDate": "2026-03-15T09:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "refrev-1",
                    "originalReference": "REFUND_PSP_002",
                    "pspReference": "REFREV_PSP_001",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.eventCode()).isEqualTo("REFUNDED_REVERSED");
    }

    @Test
    public void testRefundWithData() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "additionalData": { "acquirerReference": "ACQ_REF_001" },
                    "amount": { "currency": "EUR", "value": 4400 },
                    "eventCode": "REFUND_WITH_DATA",
                    "eventDate": "2026-03-16T10:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "refdata-1",
                    "originalReference": "ORIG_PSP_010",
                    "pspReference": "REFDATA_PSP_001",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.eventCode()).isEqualTo("REFUND_WITH_DATA");
        assertThat(item.additionalData()).containsEntry("acquirerReference", "ACQ_REF_001");
    }

    @Test
    public void testOfferClosed() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 6000 },
                    "eventCode": "OFFER_CLOSED",
                    "eventDate": "2026-03-17T12:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "offer-1",
                    "pspReference": "OFFER_PSP_001",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.eventCode()).isEqualTo("OFFER_CLOSED");
    }

    @Test
    public void testPaidoutReversed() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 25000 },
                    "eventCode": "PAIDOUT_REVERSED",
                    "eventDate": "2026-03-18T08:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "paidrev-1",
                    "originalReference": "PAYOUT_PSP_002",
                    "pspReference": "PAIDREV_PSP_001",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.eventCode()).isEqualTo("PAIDOUT_REVERSED");
    }

    // --- Edge cases ---

    @Test
    public void testSuccessFalseDeserializesToBooleanFalse() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 1000 },
                    "eventCode": "AUTHORISATION",
                    "eventDate": "2026-01-01T00:00:00+01:00",
                    "merchantAccountCode": "Test",
                    "merchantReference": "ref",
                    "pspReference": "PSP_001",
                    "success": "false",
                    "reason": "Refused"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.success()).isFalse();
        assertThat(item.reason()).isEqualTo("Refused");
    }

    @Test
    public void testMissingOptionalFields() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 0 },
                    "eventCode": "REPORT_AVAILABLE",
                    "eventDate": "2026-01-01T00:00:00+01:00",
                    "merchantAccountCode": "Test",
                    "merchantReference": "",
                    "pspReference": "PSP_002",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);
        NotificationRequestItem item = wrapper.notificationItemContainers().get(0).notificationItem();

        assertThat(item.additionalData()).isNull();
        assertThat(item.reason()).isNull();
        assertThat(item.paymentMethod()).isNull();
        assertThat(item.operations()).isNull();
        assertThat(item.originalReference()).isNull();
    }

    @Test
    public void testMultipleNotificationItems() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 1000 },
                    "eventCode": "AUTHORISATION",
                    "eventDate": "2026-01-01T00:00:00+01:00",
                    "merchantAccountCode": "Test",
                    "merchantReference": "ref1",
                    "pspReference": "PSP_A",
                    "success": "true"
                  }
                },
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 1000 },
                    "eventCode": "CAPTURE",
                    "eventDate": "2026-01-01T01:00:00+01:00",
                    "merchantAccountCode": "Test",
                    "merchantReference": "ref1",
                    "originalReference": "PSP_A",
                    "pspReference": "PSP_B",
                    "success": "true"
                  }
                },
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 500 },
                    "eventCode": "REFUND",
                    "eventDate": "2026-01-01T02:00:00+01:00",
                    "merchantAccountCode": "Test",
                    "merchantReference": "ref1",
                    "originalReference": "PSP_A",
                    "pspReference": "PSP_C",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);

        assertThat(wrapper.notificationItemContainers()).hasSize(3);
        assertThat(wrapper.notificationItemContainers().get(0).notificationItem().eventCode()).isEqualTo("AUTHORISATION");
        assertThat(wrapper.notificationItemContainers().get(1).notificationItem().eventCode()).isEqualTo("CAPTURE");
        assertThat(wrapper.notificationItemContainers().get(2).notificationItem().eventCode()).isEqualTo("REFUND");
    }

    @Test
    public void testLiveTrue() throws Exception {
        String json = """
            {
              "live": "true",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 5000 },
                    "eventCode": "AUTHORISATION",
                    "eventDate": "2026-01-01T00:00:00+01:00",
                    "merchantAccountCode": "ProdMerchant",
                    "merchantReference": "prod-ref",
                    "pspReference": "PROD_PSP_001",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        NotificationWrapper wrapper = objectMapper.readValue(json, NotificationWrapper.class);

        assertThat(wrapper.live()).isEqualTo("true");
    }
}
