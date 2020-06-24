package com.tedredington.AdyenNotifications.controller;

import com.adyen.model.notification.NotificationRequest;
import com.tedredington.AdyenNotifications.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adyen")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @PostMapping(value="/notifications", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postNotifications(@RequestBody NotificationRequest notificationRequest) {

        notificationService.save(notificationRequest);

        return ResponseEntity.ok("[accepted]");
    }

    @GetMapping(value="/notifications")
    public ResponseEntity<String> getNotifications () {

        return ResponseEntity.ok("OK");
    }

    public NotificationController( NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping(value="/error", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postBadNotification(@RequestBody NotificationRequest notificationRequest) {

        // notificationService.save(notificationRequest);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("faking a bad request");
    }
}
