package com.social.mcnotification.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomUserSecurityContextFactory.class)
public @interface WithCustomUser {
    String email() default "testuser@example.com";

    String token() default "testtoken";

    String[] roles() default {"USER"};

    String userId() default "1";
}
