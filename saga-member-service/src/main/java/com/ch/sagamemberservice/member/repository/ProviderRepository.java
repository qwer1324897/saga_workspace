package com.ch.sagamemberservice.member.repository;

import com.ch.sagamemberservice.member.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider, Integer> {

    // Provider 하나 추출 select * from provider where provider_name = 'kakao'
    Optional<Provider> findByProviderName(String ProviderName);

}
