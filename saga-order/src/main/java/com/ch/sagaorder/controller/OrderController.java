package com.ch.sagaorder.controller;

import com.ch.sagaorder.dto.CreateOrderRequest;
import com.ch.sagaorder.service.OrderPublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderPublishService orderPublishService;

    // 주문 요청 처리
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) { // 객체화 되어있어야 json 자동 매핑
        log.debug("상품의 pk 값은: {}", request.getProduct_id());
        log.debug("상품의 이름은: {}", request.getProduct_name());

        orderPublishService.createAndPublish(request.getProduct_id(), request.getProduct_name());

        return ResponseEntity.ok("oooooooooooooook");
    }
}
