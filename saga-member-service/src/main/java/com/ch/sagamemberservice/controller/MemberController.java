package com.ch.sagamemberservice.controller;

import com.ch.sagamemberservice.member.entity.Provider;
import com.ch.sagamemberservice.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<?> getProvider(String providerName) {

        Provider provider = memberService.getProvider(providerName);

        return ResponseEntity.ok(Map.of(
                "provider_id 는 ", provider.getProviderId(),
                "프로바이더는: ", provider.getProviderName()
        ));
    }
}
