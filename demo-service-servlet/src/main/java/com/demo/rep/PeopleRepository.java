package com.demo.rep;

import com.demo.domain.People;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by divver on 2018/7/11.
 */
public interface PeopleRepository extends JpaRepository<People, Long> {
}
