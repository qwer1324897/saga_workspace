package com.ch.sagaproduct.consumer;

import com.ch.sagaproduct.message.OrderCreatedMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
public class OrderCreatedConsumer {

    // 주의. 현재 시점 만들고 있는 재고관리 앱에서는 주문완료 처리에 대해 웹 상의 요청,
    // 클라리언트의 요청에 의해 주문완료 여부를 전달받는 방식이 아닌 RabbitMQ 의 메세지를 청취하는 방식으로 전달받는다.
    // 따라서 현재로써는 Controller 가 필요 없다. 또한 requestId 도 헤더값으로 전달될 일도 현재로선 없다.
    // 다만 재고관리팀으로 주문 이외의 다른 요청에 대해서는 충분히 컨트롤러에서 요청을 처리할 수도 있음.
    // 가령 배송관리팀이 재고관리팀에게 현재 재고량을 요청한다면? > Controller 에서 요청을 받아 처리해야 함. 이 땐 헤더로 requestId가 넘어옴.

    @RabbitListener(queues = "order.created.queue")  // Listener 에는 json 문자열을 > java 객체로 자동변환해주는 기능이 이미 포함되어 있음.
    public void onMessage(OrderCreatedMessage message) {
        log.debug("주문 생성 감지");

        // RabbitMQ 서버의 리슨을 담당하는 쓰레드는 MDC의 존재를 모르는 리스터 전용
        // 반드시 MDC 를 재저장 해야함
        MDC.put("requestId", message.getRequestId());

        try {
            // 재고관리 업무
            log.debug("재고관리 업무 시작");
        } finally {
            MDC.remove("requestId");
        }
    }
}
