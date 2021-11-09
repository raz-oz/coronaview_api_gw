package com.rad.ms.corona_view.api_gw;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController {

    private final String ACCESS_CLIENT_NAME = "lb://CORONA-VIEW-ACCESS";
    private final String ACCESS_DATA_NAME = "lb://CORONA-VIEW-DATA";

    @Bean
    public RouteLocator accessRoutes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route(p -> p
                        .path("/users/*")
                        .filters(f -> f.circuitBreaker(config -> config.setFallbackUri("http://localhost:8401/fallback")))
                        .uri(ACCESS_CLIENT_NAME))
                .route(p -> p
                        .path("/users")
                        .filters(f -> f.circuitBreaker(config -> config.setFallbackUri("http://localhost:8401/fallback")))
                        .uri(ACCESS_CLIENT_NAME))
                .route(p -> p
                        .path("/recovered")
                        .filters(f -> f.circuitBreaker(config -> config.setFallbackUri("http://localhost:8404/fallback")))
                        .uri(ACCESS_DATA_NAME))
                .route(p -> p
                        .path("/recovered/*")
                        .filters(f -> f.circuitBreaker(config -> config.setFallbackUri("http://localhost:8404/fallback")))
                        .uri(ACCESS_DATA_NAME))
                .route(p -> p
                        .path("/hospitalized")
                        .filters(f -> f.circuitBreaker(config -> config.setFallbackUri("http://localhost:8404/fallback")))
                        .uri(ACCESS_DATA_NAME))
                .route(p -> p
                        .path("/hospitalized/*")
                        .filters(f -> f.circuitBreaker(config -> config.setFallbackUri("http://localhost:8404/fallback")))
                        .uri(ACCESS_DATA_NAME))
                .route(p -> p
                        .path("/isolated")
                        .filters(f -> f.circuitBreaker(config -> config.setFallbackUri("http://localhost:8404/fallback")))
                        .uri(ACCESS_DATA_NAME))
                .route(p -> p
                        .path("/isolated/*")
                        .filters(f -> f.circuitBreaker(config -> config.setFallbackUri("http://localhost:8404/fallback")))
                        .uri(ACCESS_DATA_NAME))
                .route(p -> p
                        .path("/covidbyarea")
                        .filters(f -> f.circuitBreaker(config -> config.setFallbackUri("http://localhost:8404/fallback")))
                        .uri(ACCESS_DATA_NAME))
                .route(p -> p
                        .path("/covidbyarea/*")
                        .filters(f -> f.circuitBreaker(config -> config.setFallbackUri("http://localhost:8404/fallback")))
                        .uri(ACCESS_DATA_NAME))
                .route(p -> p
                        .path("/updatedata")
                        .filters(f -> f.circuitBreaker(config -> config.setFallbackUri("http://localhost:8404/fallback")))
                        .uri(ACCESS_DATA_NAME))
                .build();
    }

    @GetMapping("/fallback")
    public String fallback(){
        return "Page currenly un-available.\n Please try again later.";
    }
}
