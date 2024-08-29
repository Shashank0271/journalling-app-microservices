package com.springb.gateway.apigateway.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    private final List<String> openApiEndpoints = List.of("/auth/signup", "/auth/login");
    public Predicate<ServerWebExchange> isOpenEndpoint = serverWebExchange ->
            openApiEndpoints.stream()
                    .anyMatch(uri -> serverWebExchange.getRequest()
                            .getPath()
                            .value()
                            .contains(uri)
                    );
}
