package com.social.mcnotification.security.configuration;

import com.social.mcnotification.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Autowired
    private UserDetailsService userDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    

//
//    @Bean
//    @ConditionalOnProperty(prefix = "app.security", name = "type", havingValue = "inMemory")
//    public PasswordEncoder inMemoryPasswordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }

//    @Bean
//    @ConditionalOnProperty(prefix = "app.security", name = "type", havingValue = "inMemory")
//    public UserDetailsService inMemoryUserDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//
//        manager.createUser(User.withUsername("user")
//                .password("user")
//                .roles("user")
//                .build());
//
//        manager.createUser(User.withUsername("admin")
//                .password("admin")
//                .roles("USER", "ADMIN")
//                .build());
//
//        return manager;
//
//    }


    @Bean
    @ConditionalOnProperty(prefix = "app.security", name = "type", havingValue = "inMemory")
    public AuthenticationManager inMemoryAuthenticationManager(HttpSecurity http,
                                                               UserDetailsService inMemoryUserDetailsService) throws Exception {
        var authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.userDetailsService(inMemoryUserDetailsService);

        return authManagerBuilder.build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.authorizeHttpRequests((auth) -> auth.requestMatchers("/api/v1/notifications/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()).csrf(AbstractHttpConfigurer:: disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
