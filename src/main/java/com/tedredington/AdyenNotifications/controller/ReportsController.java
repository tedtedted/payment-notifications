package com.tedredington.AdyenNotifications.controller;

import com.tedredington.AdyenNotifications.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adyen")
public class ReportsController {

    @Autowired
    NotificationService notificationService;

    @PostMapping(value="/reports", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postReports() {

        return ResponseEntity.ok("[accepted]");
    }

    @GetMapping(value="/reports")
    public ResponseEntity<String> getReports () {

        return ResponseEntity.ok("OK");
    }

}
