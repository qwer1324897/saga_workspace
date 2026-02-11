package com.ch.sagaorder.domain;

import lombok.Builder;
import lombok.Getter;

// 주문, 환불, 취소 등 컴퓨터 엔지니어링과 무관한 비즈니스 업무
@Getter
public class Order {
    private final Long orderId;
    private final Long productId;
    private final String productName;

    // new 연산자로 파라미터를 메모리에 올리면 순서보장이 안 되는데, 생성자를 만들고 빌더패턴(애노테이션)을 적용하여 해결할 수 있다.
    @Builder
    public Order(Long orderId, Long productId, String productName) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
    }
}

