package com.ch.sagaorder.repository;


import com.ch.sagaorder.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class OrderRepository {

    private final AtomicLong seq = new AtomicLong(100);
    private final Map<Long, Order> orderList = new ConcurrentHashMap<>(); // db 없어서 대체

    // 주문 생성
    public Order create(Long productId, String productName) {
        // auto increment 흉내. 지금은 db 연동 안 하고 메모리에만 올리기 때문에
        long orderId = seq.incrementAndGet();   // auto_increment 흉내

        // 빌더 패턴을 쓰던, 생성자를 호출해서 쓰던, 어차피 인스턴스 생성이 목적이지만, 매개변수가 많을 수록 실수를 방지하려면
        // 빌더 패턴을 사용하는 것이 좋다.
        Order order = Order.builder()   // new Order() 가 아니라 빌더패턴을 사용하여 생성자 순서 보장
                .orderId(orderId)
                .productId(productId)
                .productName(productName)
                .build();
        orderList.put(orderId, order);  // 다수의 주문을 흉내냄.
        return order;
    }
}