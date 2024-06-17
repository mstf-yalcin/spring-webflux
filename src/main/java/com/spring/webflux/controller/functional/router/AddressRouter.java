package com.spring.webflux.controller.functional.router;

import com.spring.webflux.controller.functional.handler.AddressHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AddressRouter {
    private static final String ADDRESS_API_PREFIX = "/api/v2/address";

    @Bean
    public RouterFunction<ServerResponse> addressRoutes(AddressHandler addressHandler) {
        return RouterFunctions
                .nest(RequestPredicates.path(ADDRESS_API_PREFIX),
                        RouterFunctions.route()
                                .POST("", addressHandler::handleCreate)
                                .GET("", addressHandler::handleGetAll)
                                .GET("/{id}", addressHandler::handleGetById)
                                .PUT("/{id}", addressHandler::handleUpdate)
                                .DELETE("/{id}", addressHandler::handleDelete)
                                .build());

    }

}
