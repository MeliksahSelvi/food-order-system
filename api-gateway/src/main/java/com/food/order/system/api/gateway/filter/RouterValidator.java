package com.food.order.system.api.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

/**
 * @Author mselvi
 * @Created 30.12.2023
 */

@Component
public class RouterValidator {

    private static final List<String> openApiEndpoints = List.of(
//            "/user-service/auth/login",
//            "/user-service/auth/register",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**"
    );


    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
