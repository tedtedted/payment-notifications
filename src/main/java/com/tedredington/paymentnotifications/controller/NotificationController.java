package com.tedredington.paymentnotifications.controller;

import com.adyen.model.notification.NotificationRequest;
import com.tedredington.paymentnotifications.domain.EventTypes;
import com.tedredington.paymentnotifications.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.util.Locale;

@RestController()
@RequestMapping("adyen")
public class NotificationController {

    Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("events")
    public ResponseEntity postNotification(@RequestBody NotificationRequest notification) {

        try {
            notificationService.save(notification);
            return ResponseEntity.ok("[accepted]");
        } catch (SignatureException e) {
            logger.warn("ERROR: " + e.getLocalizedMessage());
            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
