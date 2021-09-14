package com.example.tutoriel.springcloudgateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class ResourcesConfig {
    @Bean
    RouterFunction<ServerResponse> staticWebHostingRoutes() {
        RouterFunctions.Builder routerFunction = RouterFunctions.route();
        routerFunction.onError(IllegalStateException.class,
                (e, request) -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error encountered while creating route"
                        + e.getMessage()));
        routerFunction.add(RouterFunctions.resources("/tutoriel/internal/**", new ClassPathResource("randomfiles/")));
        routerFunction.add(RouterFunctions.resources("/tutoriel/external/**",
                new FileSystemResource("C:\\tmp\\")));
        return routerFunction.build();
    }

}
