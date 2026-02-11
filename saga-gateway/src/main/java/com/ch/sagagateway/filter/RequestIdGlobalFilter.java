package com.ch.sagagateway.filter;

import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/*
    [목적]
    - 모든 요청의 최전방에서 filter 로 requestId 라는 이름의 MDC(로그에 남기기 위해 사용하는 컨텍스트 저장소) 를 발급
    - 요청 헤더에다가 생성한 requestId 를 싣어서 하위 영역(DownStream)으로 전파
    - 어디에? > 로그 MDC 객체에 저장
*/
public class RequestIdGlobalFilter implements GlobalFilter, Ordered {

    private static final String HEADER_NAME = "X-Request-ID";

    /*
     비동기 처리의 반환값에 사용 됨
     미래에 0개 또는 1개의 결과를 바탕으로 비동기를 제공하는 객체 . 즉, 결과값은 없고 완료 신호만 가진 객체이다.
    */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        /*ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
         이미 존재하면 꺼내고, 없으면 만들자
        ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ*/
        String requestId = exchange.getRequest().getHeaders().getFirst(HEADER_NAME);  // requestId 에 많이 사용되는 관례다.

        if(requestId==null) {   // 없을 경우, 생성.
            requestId = UUID.randomUUID().toString();
        }

        // MDC 에 저장
        MDC.put("requestId", requestId);

        // 있으면 기존 헤더를 그냥 쓰면 되지만, 없었을 경우엔 헤더에 대입.
        ServerHttpRequest request = exchange.getRequest().mutate().header(HEADER_NAME, requestId).build();
        ServerWebExchange serverWebExchange = exchange.mutate().request(request).build();

        return chain.filter(serverWebExchange)
                // doFinally 는 서버에서 클라이언트에게 응답 전송이 완료되었거나, 종료되었을 때(클라이언트가 연결 끊기) 호출되는 메서드.
                // 로그 컨텍스트를 지우지 않으면, 즉 청소하지 않으면 다른 유저와의 requestId 꼬여버림
                .doFinally(signalType -> MDC.remove("requestId"));
    }

    // Gateway 에는 여러 필터가 지원되는데, 이 때 순서를 정할 수 있음.
    // 값이 작을 수록 우선순위가 높음. (음수가 더 높음. 1순위 2순위보다 0순위가 높고, -1순위는 0순위보다 앞순위.)
    @Override
    public int getOrder() {
        return -1;
    }
}
