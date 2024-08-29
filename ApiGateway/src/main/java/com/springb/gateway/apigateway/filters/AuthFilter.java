package com.springb.gateway.apigateway.filters;

import com.springb.gateway.apigateway.utils.JwtValidator;
import com.springb.gateway.apigateway.utils.RouteValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Autowired
    RouteValidator routeValidator;
    @Autowired
    JwtValidator jwtValidator;

    AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (!routeValidator.isOpenEndpoint.test(exchange)) {

                if (!exchange.getRequest()
                        .getHeaders()
                        .containsKey(HttpHeaders.AUTHORIZATION)) {
                    log.error("missing auth header");
                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = exchange.getRequest()
                        .getHeaders()
                        .get(HttpHeaders.AUTHORIZATION)
                        .get(0);


                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    try {
                        String jwtToken = authHeader.substring(7);
                        jwtValidator.validateToken(jwtToken);
                    } catch (Exception e) {
                        log.error("not authorized\t\t{}", String.valueOf(e));
                        throw new RuntimeException("not authorized !");
                    }
                }
            }
            return chain.filter(exchange);
        });

    }

    public static class Config {

    }
}