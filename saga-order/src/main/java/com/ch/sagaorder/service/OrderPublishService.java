package com.ch.sagaorder.service;

import com.ch.sagaorder.config.AmqpConfig;
import com.ch.sagaorder.domain.Order;
import com.ch.sagaorder.message.OrderCreatedMessage;
import com.ch.sagaorder.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderPublishService {

    private final OrderRepository orderRepository;  // DB 작업 또는 메모리 저장
    private final RabbitTemplate rabbitTemplate;   // RabbitMQ 에 메세지 전달

    //주문 등록
    public Order createAndPublish(Long productId, String productName) {

        // 1. 주문 생성. (메모리상에서만. 지금은 DB 없이 하는 중)
        Order order = orderRepository.create(productId, productName);

        // 2. 다른 서비스에게 전달하기 위한 requestId 꺼내기
        String requestId = MDC.get("requestId");

        // 3. RabbitMQ 에게 이벤트를 발생시켜야 하므로, RabbitMQ 의 호출객체 필요
        // RabbitTemplate.
        OrderCreatedMessage message = new OrderCreatedMessage(requestId, order.getOrderId(), order.getProductId(), order.getProductName());
         rabbitTemplate.convertAndSend(AmqpConfig.ORDER_CREATED_EXCHANGE,
                 AmqpConfig.ORDER_CREATED_ROUTING_KEY,
                 message);

        return order;
    }
}

