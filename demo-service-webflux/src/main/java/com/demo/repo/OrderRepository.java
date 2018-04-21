package com.demo.repo;

import com.demo.domain.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface OrderRepository extends ReactiveMongoRepository<Order, Long> {

	@Tailable
	Flux<Order> findCarsBy();

}
