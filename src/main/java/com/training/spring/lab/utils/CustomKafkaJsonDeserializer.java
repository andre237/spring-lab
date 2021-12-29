package com.training.spring.lab.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.spring.lab.common.annotation.KafkaPayload;
import com.training.spring.lab.common.schemas.BaseSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Slf4j @Component
public class CustomKafkaJsonDeserializer implements Deserializer<BaseSchema> {

    private final ObjectMapper jsonParser = new ObjectMapper();
    private final HashMap<String, Class<?>> mappedSchemas = new HashMap<>();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Reflections reflections = new Reflections("com.training.spring.lab");
        reflections.getTypesAnnotatedWith(KafkaPayload.class).stream()
                .filter(BaseSchema.class::isAssignableFrom)
                .forEach(type ->
                    mappedSchemas.put(type.getAnnotation(KafkaPayload.class).identifier(), type)
                );
    }

    @Override
    public BaseSchema deserialize(String topic, byte[] payload) {
        try {
            JsonNode rootNode = jsonParser.readTree(payload);
            JsonNode payloadNode = rootNode.get("data");

            Class<?> identifier = mappedSchemas.get(rootNode.get("identifier").asText());
            if (identifier != null && payloadNode != null) {
                return (BaseSchema) jsonParser.treeToValue(payloadNode, identifier);
            }

            log.error("Could not find candidate schema to deserialization. Payload = {}", rootNode);
        } catch (Exception ex) {
            log.error("Deserialization failure {}", ex.getMessage());
        }

        return null;
    }

}
