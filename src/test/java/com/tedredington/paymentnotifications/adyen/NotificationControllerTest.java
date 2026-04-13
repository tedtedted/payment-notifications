package com.tedredington.paymentnotifications.adyen;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.security.SignatureException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    public void testPostNotificationReturnsAccepted() throws Exception {
        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 1000 },
                    "eventCode": "AUTHORISATION",
                    "eventDate": "2026-01-01T00:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "ref-001",
                    "pspReference": "PSP_001",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        mockMvc.perform(post("/adyen/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("[accepted]"));

        verify(notificationService).save(any());
    }

    @Test
    public void testPostNotificationSignatureExceptionReturns500() throws Exception {
        doThrow(new SignatureException("Invalid HMAC")).when(notificationService).save(any());

        String json = """
            {
              "live": "false",
              "notificationItems": [
                {
                  "NotificationRequestItem": {
                    "amount": { "currency": "EUR", "value": 1000 },
                    "eventCode": "AUTHORISATION",
                    "eventDate": "2026-01-01T00:00:00+01:00",
                    "merchantAccountCode": "TestMerchant",
                    "merchantReference": "ref-001",
                    "pspReference": "PSP_001",
                    "success": "true"
                  }
                }
              ]
            }
            """;

        mockMvc.perform(post("/adyen/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("error"));
    }
}
