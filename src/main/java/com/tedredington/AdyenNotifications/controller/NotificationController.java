package com.tedredington.AdyenNotifications.controller;

import com.adyen.model.notification.NotificationRequest;
import com.tedredington.AdyenNotifications.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(value="/notifications", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getNotifications (@RequestBody NotificationRequest notificationRequest) {

        notificationService.save(notificationRequest);

        return ResponseEntity.ok("OK");
    }

    public NotificationController( NotificationService notificationService) {
        this.notificationService = notificationService;
    }
}
