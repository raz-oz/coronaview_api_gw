package com.rad.ms.corona_view.api_gw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.List;

@RestController
@EnableEurekaClient
@SpringBootApplication
public class CoronaViewApiGatewayApplication implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(CoronaViewApiGatewayApplication.class);
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private ApplicationContext applicationContext;

    private final String ACCESS_CLIENT_NAME = "CORONA-VIEW-ACCESS";


    public static void main(String[] args) {
        SpringApplication.run(CoronaViewApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator accessRoutes(RouteLocatorBuilder builder) {
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(ACCESS_CLIENT_NAME);
        if (!serviceInstances.isEmpty()) {
            int i = 0;
            ServiceInstance access_service = serviceInstances.get(i);
            while (!access_service.getServiceId().equals(ACCESS_CLIENT_NAME) && (i+1 < serviceInstances.size())) {
                i++;
                access_service = serviceInstances.get(i);
            };
            URI accessURI = access_service.getUri();
            return builder.routes()
                    .route(p -> p
                            .path("/users/{user_id}")
                            .filters(f -> f.circuitBreaker(config -> config.setFallbackUri("http://localhost:8401/fallback")))
                            .uri(accessURI))
                    .route(p -> p
                            .path("/users")
                            .filters(f -> f.circuitBreaker(config -> config.setFallbackUri("http://localhost:8401/fallback")))
                            .uri(accessURI))
                    .build();
        }
        else {
            LOG.error("Error identifying access client.");
            return builder.routes().build();
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event)
    {
        try
        {

            String ip       = InetAddress.getLocalHost().getHostAddress();
            String hostName = InetAddress.getLocalHost().getHostName();
            int port        = applicationContext.getBean(Environment.class).getProperty("server.port", Integer.class, 0);

            System.out.println("*****************************************************");
            System.out.println("* Coronaview API-GW is UP and Ready ");
            System.out.println("* Host=" + hostName + ", IP=" + ip + ", Port=" + port);
            System.out.println("*****************************************************");
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
    }

    @GetMapping("/fallback")
    public String fallback(){
        return "Page currenly un-available.\n Please try again later.";
    }
}
