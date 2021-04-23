package com.tedredington.AdyenNotifications.domain;

import com.fasterxml.jackson.annotation.*;
import io.micronaut.core.annotation.Introspected;
import org.junit.platform.commons.util.ToStringBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Introspected
public class Notification {

    @JsonProperty("live")
    private String live;
    @JsonProperty("notificationItems")
    private List<NotificationItem> notificationItems = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("live")
    public String getLive() {
        return live;
    }

    @JsonProperty("live")
    public void setLive(String live) {
        this.live = live;
    }

    @JsonProperty("notificationItems")
    public List<NotificationItem> getNotificationItems() {
        return notificationItems;
    }

    @JsonProperty("notificationItems")
    public void setNotificationItems(List<NotificationItem> notificationItems) {
        this.notificationItems = notificationItems;
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
        return new ToStringBuilder(this).append("live", live).append("notificationItems", notificationItems).append("additionalProperties", additionalProperties).toString();
    }

}