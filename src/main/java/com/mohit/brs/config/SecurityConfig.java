package com.mohit.brs.config;

import com.mohit.brs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/*
This SecurityConfig class is a Spring configuration class responsible for configuring
security settings for the application. Here's a short and crisp explanation:
*/

@Configuration
public class SecurityConfig {


    @Autowired
    private JwtAuthenticationEntryPoint point;
    @Autowired
    private JwtAuthenticationFilter filter;

    @Autowired
    UserService userService;

    /*
    Security Filter Chain: Configures security filter chain using securityFilterChain bean. It:
    Disables CSRF protection.

    Specifies URL patterns and their corresponding access permissions.
    Sets up an authentication entry point for handling authentication exceptions.
    Sets session management to stateless to ensure JWT-based authentication.
    Adds the JwtAuthenticationFilter before the UsernamePasswordAuthenticationFilter to handle JWT authentication.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeRequests()
                .requestMatchers("/*").authenticated()
                .requestMatchers("/api/v1/user/register")
                .permitAll()
                .requestMatchers("/api/v1/user/login")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /*
    BCryptPasswordEncoder Bean: Provides a BCrypt password encoder bean for encoding and verifying passwords.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    DaoAuthenticationProvider Bean: Configures DAO-based authentication provider with the user details service and password encoder.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    /*
    AuthenticationManager Bean: Configures the authentication manager bean using AuthenticationConfiguration, which enables method-level security.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration autheticationConfiguration)
            throws Exception {
        return autheticationConfiguration.getAuthenticationManager();
    }

    /*
    JwtAuthenticationFilter Bean: Creates an instance of the JwtAuthenticationFilter.
     */

    @Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationFilter();
    }

}
