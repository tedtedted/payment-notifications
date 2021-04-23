package com.tedredington.AdyenNotifications.domain;

import com.fasterxml.jackson.annotation.*;
import io.micronaut.core.annotation.Introspected;
import org.junit.platform.commons.util.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Introspected
public class Amount {

    @JsonProperty("currency")
    private String currency;
    @JsonProperty("value")
    private Integer value; //1 2 3 4 5
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("currency")
    public String getCurrency() { // "USD" "EUR"

        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("value")
    public Integer getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(Integer value) {
        this.value = value;
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
        return new ToStringBuilder(this).append("currency", currency).append("value", value).append("additionalProperties", additionalProperties).toString();
    }

}