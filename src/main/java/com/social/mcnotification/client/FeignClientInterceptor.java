package com.social.mcnotification.client;

import com.social.mcnotification.security.jwt.UserModel;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel user = (UserModel) authentication.getPrincipal();
        String token = user.getToken();
        requestTemplate.header("Authorization", "Bearer " + token);

    }
}
