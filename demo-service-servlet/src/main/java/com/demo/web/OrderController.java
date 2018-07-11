package com.demo.web;

import com.demo.domain.People;
import com.demo.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class OrderController {
//	@Autowired
//	private OrderRepository orderRepository;
//
//	@ApiOperation(value = "query orders", response = Order.class)
//	@GetMapping(value = "/orders", produces = "application/stream+json")
//	public List<Order> query() {
//		return this.orderRepository.findAll();
//	}
//
//	@ApiOperation(value = "get order by id", response = Order.class)
//	@GetMapping("/order/{id}")
//	public Optional<Order> get(@ApiParam(value = "id") @PathVariable("id") Long id) {
//		return this.orderRepository.findById(id);
//	}
//
//	@ApiOperation(value = "submit orders", response = Order.class)
//	@PostMapping(path="/orders", consumes = "application/json")
//	@ResponseStatus(HttpStatus.CREATED)
//	public List<Order> submit(@ApiParam(value = "orders") @RequestBody List<Order> orders) {
//		return this.orderRepository.insert(orders);
//	}

	@Autowired
	TestService testService;

	@ApiOperation(value = "submit people", response = String.class)
	@PostMapping(path="/people", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public String submitPeople(@ApiParam @RequestBody People people) {
		testService.doSomething(people);
		return "ok";
	}

}
