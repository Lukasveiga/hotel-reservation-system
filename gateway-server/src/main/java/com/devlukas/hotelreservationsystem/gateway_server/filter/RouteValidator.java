package com.devlukas.hotelreservationsystem.gateway_server.filter;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    @Value("${api.endpoint.base-url:/api/v1}")
    private static String baseUrl;

    public static final List<String> openApiEndpoints = List.of(
            baseUrl + "/hotel/**",
            baseUrl + "/eureka",
            baseUrl + "/api-docs"
    );

    public Predicate<ServerHttpRequest> isSecured = serverHttpRequest -> openApiEndpoints
            .stream().noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));
}