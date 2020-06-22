package com.tedredington.AdyenNotifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class AdyenNotificationsApplication {

	public static void main(String[] args) {

		SpringApplication.run(AdyenNotificationsApplication.class, args);

	}
}
