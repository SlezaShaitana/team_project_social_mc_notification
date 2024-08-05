package com.social.mcnotification.security.configuration;

import com.social.mcnotification.security.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService userDetailsService;

    private final JwtTokenFilter jwtTokenFilter;


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


//    @Bean
//    @ConditionalOnProperty(prefix = "app.security", name = "type", havingValue = "inMemory")
//    public AuthenticationManager inMemoryAuthenticationManager(HttpSecurity http,
//                                                               UserDetailsService inMemoryUserDetailsService) throws Exception {
//        var authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//        authManagerBuilder.userDetailsService(inMemoryUserDetailsService);
//
//        return authManagerBuilder.build();
//    }


    //    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
//        http.authorizeHttpRequests((auth) -> auth.requestMatchers("/api/v1/notifications/**").hasAnyRole("USER", "ADMIN")
//                        .anyRequest().authenticated()).csrf(AbstractHttpConfigurer:: disable)
//                .httpBasic(Customizer.withDefaults())
//                .sessionManagement(httpSecuritySessionManagementConfigurer ->
//                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authenticationManager(authenticationManager).addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> auth.requestMatchers("/api/v1/notifications/**")
                        .hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling(configurer -> configurer.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .csrf(AbstractHttpConfigurer::disable).httpBasic(Customizer.withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }


}
