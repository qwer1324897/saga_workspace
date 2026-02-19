package com.ch.sagamemberservice.member.service;

import com.ch.sagamemberservice.member.entity.Provider;
import com.ch.sagamemberservice.member.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // autowired
public class MemberService {

    private final ProviderRepository providerRepository;

    // sns 사업자 반환
    public Provider getProvider(String providerName) {
        return providerRepository.findByProviderName(providerName).orElseThrow(()-> new IllegalArgumentException("찾을 수 없는디요?"));
    }
}