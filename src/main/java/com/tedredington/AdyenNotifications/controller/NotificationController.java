package com.tedredington.AdyenNotifications.controller;

import com.tedredington.AdyenNotifications.domain.Notification;
import com.tedredington.AdyenNotifications.service.NotificationService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.views.View;

@Controller("/adyen/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @Post
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public HttpResponse postNotification(@Body Notification notification){

        notificationService.save(notification);
        return HttpResponse.ok("[accepted]");
    }


    @Get
    @View("index")
    public HttpResponse index() {
        return HttpResponse.ok();
    }
}
