package com.ecommerce.glowshop.kafka;

import com.ecommerce.glowshop.model.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Profile("kafka")
public class KafkaOrderPlacedPublisher implements OrderPlacedPublisher {

    public static final String ORDER_PLACED_TOPIC = "order.placed";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void publish(Order order) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("event", "order.placed");
        payload.put("orderId", order.getId());
        payload.put("customerEmail", order.getUser().getEmail());
        payload.put("totalAmount", order.getTotalAmount());
        payload.put("status", order.getStatus().name());
        try {
            String json = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(ORDER_PLACED_TOPIC, String.valueOf(order.getId()), json);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize order event", e);
        }
    }
}
