package com.tedredington.paymentnotifications.controller;

import com.adyen.model.notification.NotificationRequest;
import com.tedredington.paymentnotifications.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.SignatureException;

@RestController()
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping()
    public ResponseEntity postNotification(@RequestBody NotificationRequest notification) {

        try {
            notificationService.save(notification);
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("[accepted]");

    }
}
