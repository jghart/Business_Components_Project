package com.ecommerce.glowshop.kafka;

import com.ecommerce.glowshop.model.Order;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!kafka")
public class NoOpOrderPlacedPublisher implements OrderPlacedPublisher {

    @Override
    public void publish(Order order) {
        // default: no broker
    }
}
