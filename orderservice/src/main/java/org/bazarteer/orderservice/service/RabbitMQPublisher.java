package org.bazarteer.orderservice.service;

import org.bazarteer.orderservice.config.RabbitMQConfig;
import org.bazarteer.orderservice.model.Order;
import org.bazarteer.orderservice.model.OrderPlacedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQPublisher {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishOrderPlaced (Order order) {
        OrderPlacedMessage message = new OrderPlacedMessage(order.getId(), order.getSellerId(), order.getProductId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_ORDER_PLACED, message);
    }
}
