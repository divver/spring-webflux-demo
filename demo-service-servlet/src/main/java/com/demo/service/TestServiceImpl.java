package com.demo.service;

import com.demo.domain.Man;
import com.demo.domain.People;
import com.demo.rep.ManRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by divver on 2018/7/11.
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    PeopleService peopleService;

    @Autowired
    ManRepository manRepository;

    @Transactional
    @Override
    public void doSomething(People people) {
        peopleService.save(people);

        Man man = new Man();
        man.setFirstName("man");
        man.setLastName("man");
        manRepository.save(man);
    }

}
