package com.demo.repo;

import com.demo.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderH2Reposity extends JpaRepository<Item, Long> {
}
