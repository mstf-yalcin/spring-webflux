package com.spring.webflux.controller.functional.router;

import com.spring.webflux.controller.functional.handler.CustomerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CustomerRouter {
    private static final String CUSTOMER_API_PREFIX = "/api/v2/customer";

    @Bean
    public RouterFunction<ServerResponse> customerRoutes(CustomerHandler customerHandler) {
        return RouterFunctions
                .nest(RequestPredicates.path(CUSTOMER_API_PREFIX),
                        RouterFunctions.route()
                                .POST("", customerHandler::handleCreate)
                                .GET("", customerHandler::handleGetAll)
                                .GET("/stream", customerHandler::handleStreamGetAll)
                                .GET("/{id}/addresses", customerHandler::handleGetAllWithAddresses)
                                .GET("/{id}", customerHandler::handleGetById)
                                .PUT("/{id}", customerHandler::handleUpdate)
                                .DELETE("/{id}", customerHandler::handleDelete)
                                .build());

    }
}


