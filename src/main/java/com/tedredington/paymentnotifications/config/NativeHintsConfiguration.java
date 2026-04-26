package com.tedredington.paymentnotifications.config;

import com.tedredington.paymentnotifications.adyen.webhook.Amount;
import com.tedredington.paymentnotifications.adyen.webhook.NotificationContainer;
import com.tedredington.paymentnotifications.adyen.webhook.NotificationRequestItem;
import com.tedredington.paymentnotifications.adyen.webhook.NotificationWrapper;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@RegisterReflectionForBinding({
        NotificationWrapper.class,
        NotificationContainer.class,
        NotificationRequestItem.class,
        Amount.class
})
public class NativeHintsConfiguration {
}
