package com.ecommerce.glowshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

/**
 * Kafka auto-configuration is excluded so the app runs without a broker by default.
 * Run with {@code --spring.profiles.active=kafka} and a local broker for order events.
 */
@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
public class GlowshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlowshopApplication.class, args);
    }

}
