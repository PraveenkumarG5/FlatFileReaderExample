package com.tcs.poc.batch;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.module.kotlin.KotlinModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.module.kotlin.KotlinModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.module.kotlin.KotlinModule;

public class JacksonConfig {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new KotlinModule.Builder().build());

        // Configure coercion to FAIL for numeric input to String fields
        objectMapper.coercionConfigFor(String.class)
                .setCoercion(CoercionInputShape.Integer, CoercionAction.Fail)
                .setCoercion(CoercionInputShape.Float, CoercionAction.Fail)
                .setCoercion(CoercionInputShape.Boolean, CoercionAction.Fail);

        String json = "{\"name\": \"test\", \"amount\": {\"value\": 12452.42, \"currency\": \"CHF\"}}";

        try {
            Transaction result = objectMapper.readValue(json, Transaction.class);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Deserialization failed: " + e.getMessage());
        }
    }

}