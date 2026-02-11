package com.ch.sagaorder.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// RabbitMQ 에게 보낼 메세지 (반드시 requestId 추가)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedMessage {
    private String requestId;
    private Long OrderId;
    private Long productId;
    private String productName;
}
