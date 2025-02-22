package com.tedredington.paymentnotifications;

import com.adyen.model.notification.NotificationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.SignatureException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceJsonTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private ObjectMapper objectMapper;
    private String jsonPayload;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // JSON String mimicking the Adyen webhook
        jsonPayload = """
        {
           "live":"false",
           "notificationItems":[
              {
                 "NotificationRequestItem":{
                    "additionalData": {
                        "hmacSignature": "validHmac"
                    },
                    "eventCode":"AUTHORISATION",
                    "success":"true",
                    "eventDate":"2019-06-28T18:03:50+01:00",
                    "merchantAccountCode":"YOUR_MERCHANT_ACCOUNT",
                    "pspReference": "7914073381342284",
                    "merchantReference": "YOUR_REFERENCE",
                    "amount": {
                        "value":1130,
                        "currency":"EUR"
                    }
                 }
              }
           ]
        }
        """;
    }

    @Test
    void testSave_ValidJson_ShouldSaveToRepository() throws IOException, SignatureException {
        // Convert JSON string to NotificationRequest
        NotificationRequest notificationRequest = objectMapper.readValue(jsonPayload, NotificationRequest.class);

        // Mock the static method using Mockito's mockStatic
        try (var mockedStatic = mockStatic(NotificationService.HmacValidation.class)) {
            mockedStatic.when(() -> NotificationService.HmacValidation.validateHmac(notificationRequest))
                    .thenReturn(NotificationService.HmacStatus.VALID);

            // Call the service method
            notificationService.save(notificationRequest);

            // Verify the repository was called once
            verify(notificationRepository, times(1)).save(notificationRequest);
        }
    }
}