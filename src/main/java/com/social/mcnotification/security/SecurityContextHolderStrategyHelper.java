package com.social.mcnotification.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

public class SecurityContextHolderStrategyHelper {
    private static final SecurityContextHolderStrategy contextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    public static void clearContext() {
        contextHolderStrategy.clearContext();
    }

    public static SecurityContext getContext() {
        return contextHolderStrategy.getContext();
    }

    public static void setContext(SecurityContext context) {
        contextHolderStrategy.setContext(context);
    }

    public static SecurityContext createEmptyContext() {
        return contextHolderStrategy.createEmptyContext();
    }

}
