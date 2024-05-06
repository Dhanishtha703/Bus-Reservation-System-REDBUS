package com.mohit.brs.config;

import com.mohit.brs.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
    The JwtAuthenticationFilter class plays a crucial role in a Spring Security setup where
    JSON Web Tokens (JWT) are used for authentication. It intercepts incoming requests and checks
    for the presence of a valid JWT in the request header.

    Here's a general outline of its role:

    Intercepting Requests: The filter intercepts incoming HTTP requests before they reach the endpoint controllers.
    Extracting JWT: It extracts the JWT token from the request header.
    Validating JWT: The filter validates the JWT token to ensure it's not expired, its signature is correct, etc.
    This usually involves verifying the token against a secret key or public key.
    Setting Authentication: If the JWT is valid, the filter sets the authentication information in the Spring
    Security context, allowing the user to be authenticated for subsequent requests.
    Passing Control: Finally, the filter allows the request to proceed to the next filter or the endpoint
    controller.
 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Autowired
    private JwtHelper jwtHelper;


    @Autowired
    private UserService userService;

    /*
    he doFilterInternal method is a core method in a servlet filter. It's called by the servlet container (e.g., Tomcat) every time a request is made to the web application.

    Here's a breakdown of its role:

    Intercepting Requests: The method intercepts every incoming HTTP request to the web application
    before it reaches the target servlet or resource.
    Processing the Request: It performs operations on the request, such as extracting parameters,
    headers, or cookies, and possibly modifying them.
    Performing Filter Logic: The main logic of the filter is implemented here.
    This can include tasks like authentication, logging, input validation, modifying
    requests or responses, etc.
    Calling the Next Filter or Servlet: After processing the request, the filter can either pass the request along the filter chain to the next filter or servlet, or it can choose to block the request and send a response directly.
    Handling Exceptions: It may handle any exceptions that occur during its execution.

    In the case of security filters, like the JWT authentication filter, the doFilterInternal method typically:

    Extracts authentication information from the request (such as JWT token).
    Validates the authentication information.
    Sets up the security context if authentication is successful.
    Passes the request to the next filter or servlet in the chain.
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String requestHeader = request.getHeader("Authorization");
        //Bearer 2352345235sdfrsfgsdfsdf
        logger.info(" Header :  {}", requestHeader);
        String username = null;
        String token = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            //looking good
            token = requestHeader.substring(7);
            try {

                username = this.jwtHelper.getUsernameFromToken(token);

            } catch (IllegalArgumentException e) {
                logger.info("Illegal Argument while fetching the username !!");
                e.printStackTrace();
            } catch (ExpiredJwtException e) {
                logger.info("Given jwt token is expired !!");
                e.printStackTrace();
            } catch (MalformedJwtException e) {
                logger.info("Some changed has done in token !! Invalid Token");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();

            }


        } else {
            logger.info("Invalid Header Value !! ");
        }


        //
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            //fetch user detail from username
            UserDetails userDetails = userService.loadUserByUsername(username);
            Boolean validateToken = jwtHelper.validateToken(token, userDetails);
            if (validateToken) {

                //set the authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);


            } else {
                logger.info("Validation fails !!");
            }


        }

        filterChain.doFilter(request, response);

    }
}