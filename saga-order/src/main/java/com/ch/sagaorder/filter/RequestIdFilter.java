package com.ch.sagaorder.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/*
    [목적]
    - 요청이 들어올 때 requestId를 확보. 헤더에서 꺼내고, 없으면 만든다.
*/
@Slf4j
@Component
public class RequestIdFilter extends OncePerRequestFilter {

    private static final String HEADER_NAME = "X-Request-ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String income = request.getHeader(HEADER_NAME); // 우리한테 들어온 키값.

        String requestId = (income==null || income.isBlank())? UUID.randomUUID().toString() : income; // 삼항 연산자. 비어있어? 비어있으면만들어: income;

        log.debug("게이트웨이에서 전달된 헤더에 들어있는 requestId = {}", requestId);
        log.debug("현재 나의 쓰레드에 들어있는 MDC의 requestId = {}", MDC.get("requestId"));

        MDC.put("requestId", requestId);
        response.setHeader(HEADER_NAME, requestId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            // doFilter() 이후의 코드는 아직 서버가 클라이언에게 응답을 완료한 시점이 아닌, 응답하기 직전이라는 점이
            // 스프링이 제공해주는 doFinally() 와는 다름. 중요한 건 나갈 때 꼬이지 않게 삭제해주는 것이 핵심.
            MDC.remove("requestId");
        }
    }

}