package com.tedredington.paymentnotifications;

import org.springframework.beans.factory.annotation.Autowired;
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
