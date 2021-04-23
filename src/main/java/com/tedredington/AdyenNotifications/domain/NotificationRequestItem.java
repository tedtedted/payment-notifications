package com.tedredington.AdyenNotifications.domain;

import com.fasterxml.jackson.annotation.*;
import io.micronaut.core.annotation.Introspected;
import org.junit.platform.commons.util.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Introspected
public class NotificationRequestItem {

    @JsonProperty("amount")
    private Amount amount;
    @JsonProperty("eventCode")
    private String eventCode;
    @JsonProperty("eventDate")
    private String eventDate;
    @JsonProperty("merchantAccountCode")
    private String merchantAccountCode;
    @JsonProperty("merchantReference")
    private String merchantReference;
    @JsonProperty("paymentMethod")
    private String paymentMethod;
    @JsonProperty("pspReference")
    private String pspReference;
    @JsonProperty("reason")
    private String reason;
    @JsonProperty("success")
    private String success;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("amount")
    public Amount getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    @JsonProperty("eventCode")
    public String getEventCode() {
        return eventCode;
    }

    @JsonProperty("eventCode")
    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    @JsonProperty("eventDate")
    public String getEventDate() {
        return eventDate;
    }

    @JsonProperty("eventDate")
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    @JsonProperty("merchantAccountCode")
    public String getMerchantAccountCode() {
        return merchantAccountCode;
    }

    @JsonProperty("merchantAccountCode")
    public void setMerchantAccountCode(String merchantAccountCode) {
        this.merchantAccountCode = merchantAccountCode;
    }

    @JsonProperty("merchantReference")
    public String getMerchantReference() {
        return merchantReference;
    }

    @JsonProperty("merchantReference")
    public void setMerchantReference(String merchantReference) {
        this.merchantReference = merchantReference;
    }

    @JsonProperty("paymentMethod")
    public String getPaymentMethod() {
        return paymentMethod;
    }

    @JsonProperty("paymentMethod")
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @JsonProperty("pspReference")
    public String getPspReference() {
        return pspReference;
    }

    @JsonProperty("pspReference")
    public void setPspReference(String pspReference) {
        this.pspReference = pspReference;
    }

    @JsonProperty("reason")
    public String getReason() {
        return reason;
    }

    @JsonProperty("reason")
    public void setReason(String reason) {
        this.reason = reason;
    }

    @JsonProperty("success")
    public String getSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(String success) {
        this.success = success;
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
        return new ToStringBuilder(this).append("amount", amount).append("eventCode", eventCode).append("eventDate", eventDate).append("merchantAccountCode", merchantAccountCode).append("merchantReference", merchantReference).append("paymentMethod", paymentMethod).append("pspReference", pspReference).append("reason", reason).append("success", success).append("additionalProperties", additionalProperties).toString();
    }
}