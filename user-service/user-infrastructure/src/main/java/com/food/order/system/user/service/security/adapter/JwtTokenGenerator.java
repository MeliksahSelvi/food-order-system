package com.food.order.system.user.service.security.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.order.system.user.service.dto.JwtToken;
import com.food.order.system.user.service.entity.User;
import com.food.order.system.user.service.exception.UserDomainException;
import com.food.order.system.user.service.ports.output.TokenGenerator;
import com.food.order.system.user.service.security.credentials.JwtUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenGenerator implements TokenGenerator {

    @Value("${jwt.security.key}")
    private String APP_KEY;

    @Value("${jwt.security.key.expire.time}")
    private Long EXPIRE_TIME;

    private final ObjectMapper objectMapper;

    @Override
    public JwtToken generateToken(User user) {
        JwtUserDetails jwtUserDetails = new JwtUserDetails(user);

        addSecurityContextHolder(jwtUserDetails);

        String userDetailsAsStr = convertJsonFromJwtUser(jwtUserDetails);
        String token = createToken(userDetailsAsStr);

        return new JwtToken(jwtUserDetails.getId(), token, EXPIRE_TIME);
    }

    private String createToken(String userDetailsAsStr) {
        String token = Jwts.builder()
                .setSubject(userDetailsAsStr)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS512, APP_KEY)
                .compact();
        return token;
    }

    private void addSecurityContextHolder(JwtUserDetails jwtUserDetails) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(jwtUserDetails, null, jwtUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private String convertJsonFromJwtUser(JwtUserDetails jwtUserDetails) {
        try {
            return objectMapper.writeValueAsString(jwtUserDetails);
        } catch (JsonProcessingException e) {
            log.error("Could not create JwtUserDetails json!", e);
            throw new UserDomainException("Could not create JwtUserDetails json!", e);
        }
    }
}
