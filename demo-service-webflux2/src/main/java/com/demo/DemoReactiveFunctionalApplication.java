package com.demo;

import com.demo.web.OrderHandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@SpringBootApplication
public class DemoReactiveFunctionalApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoReactiveFunctionalApplication.class, args);
  }

  @Bean
  public RouterFunction<?> routes(OrderHandler handler) {
    return RouterFunctions.route(GET("/orders"), handler::orders)
            .andRoute(GET("/order/{id}"), handler::order)
            .andRoute(POST("/orders"), handler::submit);
  }
}
