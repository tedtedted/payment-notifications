package com.tedredington.paymentnotifications.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
public class MongoMapKeyDotReplacementConfiguration {

    private final MappingMongoConverter mongoConverter;

    public MongoMapKeyDotReplacementConfiguration(MappingMongoConverter mongoConverter) {
        this.mongoConverter = mongoConverter;
        this.mongoConverter.setMapKeyDotReplacement("");
    }
}
