package com.demo.web;

import com.demo.domain.Order;
import com.demo.rep.OrderRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api
@RestController
public class OrderController {
	@Autowired
	private OrderRepository orderRepository;

	@ApiOperation(value = "query orders", response = Order.class)
	@GetMapping(value = "/orders", produces = "application/stream+json")
	public List<Order> query() {
		return this.orderRepository.findAll();
	}

	@ApiOperation(value = "get order by id", response = Order.class)
	@GetMapping("/order/{id}")
	public Optional<Order> get(@ApiParam(value = "id") @PathVariable("id") Long id) {
		return this.orderRepository.findById(id);
	}

	@ApiOperation(value = "submit orders", response = Order.class)
	@PostMapping(path="/orders", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public List<Order> submit(@ApiParam(value = "orders") @RequestBody List<Order> orders) {
		return this.orderRepository.insert(orders);
	}

}
