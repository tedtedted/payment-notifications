package com.tedredington.AdyenNotifications.domain;

import com.fasterxml.jackson.annotation.*;
import io.micronaut.core.annotation.Introspected;
import org.junit.platform.commons.util.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Introspected
public class NotificationItem {

    @JsonProperty("NotificationRequestItem")
    private NotificationRequestItem notificationRequestItem;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("NotificationRequestItem")
    public NotificationRequestItem getNotificationRequestItem() {
        return notificationRequestItem;
    }

    @JsonProperty("NotificationRequestItem")
    public void setNotificationRequestItem(NotificationRequestItem notificationRequestItem) {
        this.notificationRequestItem = notificationRequestItem;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("notificationRequestItem", notificationRequestItem).append("additionalProperties", additionalProperties).toString();
    }

}