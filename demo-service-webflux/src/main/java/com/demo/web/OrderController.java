package com.demo.web;

import com.demo.domain.Order;
import com.demo.domain.Item;
import com.demo.repo.OrderH2Reposity;
import com.demo.repo.OrderRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Api
@RestController
public class OrderController {
	@Autowired
	private OrderRepository orderRepository;

	@ApiOperation(value = "query orders", response = Order.class)
	@GetMapping(value = "/orders")//, produces = "application/stream+json"
	public Flux<Order> query() {
		return this.orderRepository.findAll().log();
	}

	@ApiOperation(value = "get order by id", response = Order.class)
	@GetMapping("/order/{id}")
	public Mono<Order> get(@ApiParam(value = "id") @PathVariable("id") Long id) {
		return this.orderRepository.findById(id).log();
	}

	@ApiOperation(value = "submit orders", response = Order.class)
	@PostMapping(path="/orders", consumes = "application/stream+json") //!!!must use Netty HttpServer
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Void> submit(@ApiParam(value = "orders") @RequestBody Flux<Order> orders) {
		return this.orderRepository.insert(orders).then();
	}

	@PostMapping(path="/orders/add", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Flux<Order> submit(@RequestBody List<Order> orders) {
		return this.orderRepository.insert(Flux.fromIterable(orders));
	}


	//-----------------WebClient Demo-------------------//
	private final WebClient orderClient = WebClient.create("http://localhost:8080");

	@GetMapping(value = "/orders/s", produces = "application/stream+json")
	public Flux<Order> query_from_servlet() {
		return orderClient.get()
				.uri("/orders")
				.retrieve()
				.bodyToFlux(Order.class)
				.log();
	}

	private final WebClient orderClient2 = WebClient.create("http://localhost:8082");

	@PostMapping(path="/orders/f", consumes = "application/stream+json")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Void> submit_to_functional(@RequestBody Flux<Order> orders) {
		return orderClient2.post()
				.uri("/orders")
				.contentType(MediaType.APPLICATION_STREAM_JSON)
				.body(orders, Order.class)
				.retrieve()
				.bodyToMono(Void.class);
	}

	//-----------------JPA Demo : how to wrap a synchronous, blocking call ?----------------//
	@Autowired
	private OrderH2Reposity orderH2Reposity;

	private Scheduler db_workers = Schedulers.newElastic("db-worker");

	@ApiOperation(value = "query order items", response = Item.class)
	@GetMapping(value = "/items")//, produces = "application/stream+json"
	public Flux<Item> queryFromH2() {
//		return Flux.fromIterable(this.orderH2Reposity.findAll()).log();

		return Mono.fromCallable(() -> this.orderH2Reposity.findAll())
				.flux()
				.flatMap(items -> Flux.fromIterable(items))
				.subscribeOn(db_workers)
				.log();
	}

	@ApiOperation(value = "submit order items", response = Item.class)
	@PostMapping(path="/items", consumes = "application/stream+json")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Void> submitToH2(@ApiParam(value = "orders") @RequestBody Flux<Item> orders) {
//		return Mono.create(sink -> sink.success(this.orderH2Reposity.saveAll(orders.collectList().block()))).then();

		return Mono.fromCallable(() -> this.orderH2Reposity.saveAll(orders.collectList().block()))
				.subscribeOn(db_workers)
				.log()
				.then();
	}
}
