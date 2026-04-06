package com.ecommerce.glowshop.kafka;

import com.ecommerce.glowshop.model.Order;

/**
 * Fired after an order is successfully persisted. Kafka implementation sends to {@code order.placed}.
 */
public interface OrderPlacedPublisher {

    void publish(Order order);
}
