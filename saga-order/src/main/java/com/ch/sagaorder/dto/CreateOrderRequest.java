package com.ch.sagaorder.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
public class CreateOrderRequest {
    private Long productId;
    private String productName;
}
