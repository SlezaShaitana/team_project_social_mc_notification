package com.social.mcnotification.security;

import com.social.mcnotification.security.jwt.UserModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthentication implements Authentication {
    private final UserModel userModel;
    private boolean authenticated = true;

    public CustomAuthentication(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userModel.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return userModel.getPassword();
    }

    @Override
    public Object getDetails() {
        return userModel;
    }

    @Override
    public Object getPrincipal() {
        return userModel;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return userModel.getUsername();
    }
}
