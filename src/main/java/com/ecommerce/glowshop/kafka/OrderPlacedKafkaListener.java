package com.ecommerce.glowshop.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class OrderPlacedKafkaListener {

    private static final Logger log = LoggerFactory.getLogger(OrderPlacedKafkaListener.class);

    @KafkaListener(topics = KafkaOrderPlacedPublisher.ORDER_PLACED_TOPIC,
            groupId = "${spring.kafka.consumer.group-id}")
    public void onOrderPlaced(String message) {
        log.info("[order.placed] {}", message);
    }
}
