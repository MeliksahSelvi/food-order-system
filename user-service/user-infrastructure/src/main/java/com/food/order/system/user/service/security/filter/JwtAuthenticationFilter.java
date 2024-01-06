package com.food.order.system.user.service.security.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.order.system.user.service.common.rest.ErrorDTO;
import com.food.order.system.user.service.exception.UserDomainException;
import com.food.order.system.user.service.security.credentials.JwtUserDetails;
import com.food.order.system.user.service.security.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final ObjectMapper objectMapper;
    private static final List<String> openApiEndpoints = List.of(
            "/auth/login",
            "/auth/register",
            "/swagger-  ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**");


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean isSecureEndpoint = servletPathSecureEndpoint(request);
        if (!isSecureEndpoint) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getToken(request);

        if (!StringUtils.hasText(token) || jwtTokenUtil.isTokenExpired(token)) {
            arrangementResponse(request, response);
            filterChain.doFilter(request, response);
            return;
        }

        JwtUserDetails userDetails = getJwtUserDetailsFromToken(token);

        if (userDetails == null) {
            arrangementResponse(request, response);
            filterChain.doFilter(request, response);
            return;
        }

        setAuthenticationToContext(userDetails);

        filterChain.doFilter(request, response);
    }

    private boolean servletPathSecureEndpoint(HttpServletRequest request) {
        return openApiEndpoints.stream()
                .noneMatch(s -> request.getServletPath().startsWith(s));
    }

    private String getToken(HttpServletRequest request) {
        String fullToken = request.getHeader("Authorization");

        String token = null;
        if (StringUtils.hasText(fullToken)) {
            String bearerStr = "Bearer ";

            if (fullToken.startsWith(bearerStr)) {
                token = fullToken.substring(bearerStr.length());
            }
        }
        return token;
    }

    private void arrangementResponse(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        returnErrorResponse(response);
    }

    private JwtUserDetails getJwtUserDetailsFromToken(String token) {
        String jwtUserDetailsAsStr = jwtTokenUtil.findUserDetailAsStrByToken(token);
        try {
            return objectMapper.readValue(jwtUserDetailsAsStr, JwtUserDetails.class);
        } catch (JsonProcessingException e) {
            log.error("Could not read JwtUserDetails object!", e);
            throw new UserDomainException("Could not read JwtUserDetails object!", e);
        }
    }

    private void returnErrorResponse(HttpServletResponse response) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .code(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message("Valid Token Necessary")
                .build();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorDTO));
        } catch (JsonProcessingException e) {
            log.error("Could not create ErrorDTO json!", e);
            throw new UserDomainException("Could not create ErrorDTO json!", e);
        } catch (IOException e) {
            log.error("Could not write response!");
            throw new RuntimeException(e);
        }
    }

    private void setAuthenticationToContext(JwtUserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
