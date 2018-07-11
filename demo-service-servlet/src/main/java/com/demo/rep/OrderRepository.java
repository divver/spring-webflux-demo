//package com.demo.rep;
//
//import com.demo.domain.Order;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Tailable;
//
//public interface OrderRepository extends MongoRepository<Order, Long> {
//
//	@Tailable
//	Order findCarsBy();
//
//}
