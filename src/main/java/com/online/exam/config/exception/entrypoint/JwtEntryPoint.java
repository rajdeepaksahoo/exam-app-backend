package com.online.exam.config.exception.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.exam.exception.InvalidUsernameException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class JwtEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Map< String,String> responseStatus = new HashMap<>();
        responseStatus.put("STATUE","FAILED");
        
        if (authException instanceof BadCredentialsException) {
            responseStatus.put("REASON","INCORRECT PASSWORD !!!");
            String jsonResponse = objectMapper.writeValueAsString(responseStatus);
            writer.write(jsonResponse);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (authException instanceof LockedException) {
            writer.write("LockedException");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (authException instanceof DisabledException) {
            writer.write("DisabledException");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (authException instanceof InvalidUsernameException) {
            responseStatus.put("REASON",authException.getMessage());
            String jsonResponse = objectMapper.writeValueAsString(responseStatus);
            writer.write(jsonResponse);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            // Default handling for other types of exceptions
            writer.write("Access denied !!!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
