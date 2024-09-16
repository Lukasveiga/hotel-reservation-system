package com.devlukas.hotelreservationsystem.gateway_server.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final RouteValidator routeValidator;

    public AuthFilter(RouteValidator routeValidator) {
        super(Config.class);
        this.routeValidator = routeValidator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> chain.filter(exchange));
    }

    public static class Config {}
}
