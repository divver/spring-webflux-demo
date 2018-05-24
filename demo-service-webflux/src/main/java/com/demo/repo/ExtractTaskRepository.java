package com.demo.repo;

import com.demo.domain.ExtractTask;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtractTaskRepository extends ReactiveCrudRepository<ExtractTask, Long> {
}
