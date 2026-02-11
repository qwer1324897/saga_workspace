package com.ch.sagaorder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
public class CreateOrderRequest {
    private Long product_id;
    private String product_name;
}
