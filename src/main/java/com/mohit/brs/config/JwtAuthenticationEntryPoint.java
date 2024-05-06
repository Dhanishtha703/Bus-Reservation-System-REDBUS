package com.mohit.brs.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /*
    The commence method is part of the AuthenticationEntryPoint interface in Spring Security.
    It's called when a user attempts to access a secured resource without being authenticated.
    This method handles the beginning of the authentication process.

    Here's an explanation of the commence method:

    HttpServletRequest request: Represents the HTTP request made by the user.
    HttpServletResponse response: Represents the HTTP response that will be sent back to the client.
    AuthenticationException authException: Represents the exception that caused the authentication failure.
    In the provided implementation, the commence method:

    Sets the HTTP status of the response to SC_UNAUTHORIZED (401) to indicate that the request
    lacks valid authentication credentials.
    Retrieves the PrintWriter from the response to write a message back to the client.
    Writes a message to the response indicating "Access Denied !!" along with the message from
    the AuthenticationException.
     */

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        writer.println("Access Denied !! " + authException.getMessage());
    }
}
