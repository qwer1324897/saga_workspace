package com.ch.sagaproduct.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
    [목적]
    - 주문 생성 이벤트를 위한 Exchange(우체국)/Queue/Binding 선언
    - 메세지 보낼 때 json 변경을 할 수 있는 컨버터 처리
*/
@Configuration
public class AmqpConfig {

    // 주문 생성용 이벤트
    public static final String ORDER_CREATED_EXCHANGE = "order.created.exchange";
    public static final String ORDER_CREATED_QUEUE = "order.created.queue";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    @Bean
    public Declarables amqpDeclarables() {
        DirectExchange exchange = new DirectExchange(ORDER_CREATED_EXCHANGE, true, false);
        Queue queue = QueueBuilder.durable(ORDER_CREATED_QUEUE).build();

        // 이 Exchange 로 들어오는 메세지 중 routing key 가 ORDER_CREATED_ROUTING_KEY 와 일치하면, 지정한 queue로 전달하라는 명령을 binding 이라 함
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(ORDER_CREATED_ROUTING_KEY);

        return new Declarables(exchange, queue, binding);
    }

    /*
     RabbitMQ 에게 데이터 전송 시 객체 > json
    */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}