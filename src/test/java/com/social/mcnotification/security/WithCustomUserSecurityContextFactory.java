package com.social.mcnotification.security;

import com.social.mcnotification.security.jwt.UserModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;
import java.util.UUID;

public class WithCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomUser withCustomUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        List<String> roles = List.of(withCustomUser.roles());
        UserModel userModel = new UserModel(
                UUID.fromString(withCustomUser.userId()),
                withCustomUser.token(),
                withCustomUser.email(),
                roles
        );
        Authentication auth = new CustomAuthentication(userModel);

        context.setAuthentication(auth);
        return context;

    }
}
