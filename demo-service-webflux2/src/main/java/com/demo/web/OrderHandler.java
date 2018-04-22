package com.demo.web;

import com.demo.domain.Order;
import com.demo.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;


@Component
public class OrderHandler {

	@Autowired
	private OrderRepository orderRepository;

	public Mono<ServerResponse> orders(ServerRequest request) {
		return ok().contentType(APPLICATION_STREAM_JSON).body(
					this.orderRepository.findAll().log(), Order.class
		);
	}

	public Mono<ServerResponse> order(ServerRequest request) {
		Long id = Long.valueOf(request.pathVariable("id"));
		return ok().contentType(APPLICATION_JSON).body(
					this.orderRepository.findById(id).log(), Order.class
		);
	}

	public Mono<ServerResponse> submit(ServerRequest request) {
		return ok().contentType(APPLICATION_JSON).build(
				this.orderRepository.insert(request.bodyToFlux(Order.class)).log().then()
		);
	}

}
