package com.ch.sagaorder.controller;

import com.ch.sagaorder.dto.CreateOrderRequest;
import com.ch.sagaorder.service.OrderPublishService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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
    public ResponseEntity<?> createOrder(HttpServletRequest req, @RequestBody CreateOrderRequest request) { // 객체화 되어있어야 json 자동 매핑
        // 당연히 헤더에는 들어있따.
        String requestId= req.getHeader("X-REQUEST-ID");
        log.debug("주문 컨트롤러에서 체크해본 requestId ={}",requestId);
        log.debug("주문 컨트롤러에서 체크해본 MDC 안의 requsetId={}", MDC.get("requestId"));
        // 필터에서 저장된 MDC 가 살아있는지 체크하자

        log.debug("상품의 pk 값은: {}", request.getProductId());
        log.debug("상품의 이름은: {}", request.getProductName());

        orderPublishService.createAndPublish(request.getProductId(), request.getProductName());

        return ResponseEntity.ok("oooooooooooooook");
    }
}
