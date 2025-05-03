package com.online.exam.config.security;

import com.online.exam.config.exception.entrypoint.JwtEntryPoint;
import com.online.exam.config.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private JwtFilter jwtFilter;
    @Autowired
    public void setJwtFilter(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain chain(HttpSecurity security) throws Exception {

        security.csrf(i->i.disable())
                .authorizeHttpRequests(req-> req
                        .requestMatchers("/register","/login","/token","/upload","/validate","/getUploadedFile").permitAll()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        security.exceptionHandling(exception -> exception.authenticationEntryPoint(new JwtEntryPoint()));
        security.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return security.build();
    }

}
