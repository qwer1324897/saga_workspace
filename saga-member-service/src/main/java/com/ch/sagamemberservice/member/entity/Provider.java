package com.ch.sagamemberservice.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "provider")
@NoArgsConstructor  // JPA 를 위해 파라미터 없는 생성자 준비. JPA 가 호출하므로.
@Getter
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "providerId")
    private Integer providerId;

    @Column(name = "provider_name")
    private String providerName;

}
