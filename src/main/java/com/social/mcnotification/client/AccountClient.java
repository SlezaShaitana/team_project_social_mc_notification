package com.social.mcnotification.client;

import com.social.mcnotification.client.dto.AccountDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name = "mc-account", url = "http://79.174.80.200:8085/api/v1/account")
public interface AccountClient {
    @GetMapping("/{id}")
    AccountDataDTO getDataMyAccountById(@RequestHeader("Authorization") String headerRequestByAuth, @PathVariable UUID id);
}