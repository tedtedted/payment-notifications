package com.tedredington.AdyenNotifications.service;


import com.tedredington.AdyenNotifications.domain.Notification;

import javax.inject.Singleton;

@Singleton
public class NotificationService {

    public void save(Notification notification) {

        System.out.println(notification.toString());

    }
}