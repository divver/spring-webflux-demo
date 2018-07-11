package com.demo.service;

import com.demo.domain.People;
import com.demo.rep.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by divver on 2018/7/11.
 */
@Service
public class PeopleServiceImpl implements PeopleService {
    @Autowired
    PeopleRepository peopleRepository;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public void save(People people) {
        doSave(people);
    }

//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void doSave(People people){
        people = peopleRepository.save(people);

//        people.setFirstName("11111");
////        peopleRepository.save(people);
//        peopleRepository.flush();

        doSaveAsync(people);
    }

    public void doSaveAsync(People people){
        executorService.submit(() -> {
            updatePeople(people);
        });
    }

//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void updatePeople(People people){
        people.setFirstName("11111");
        peopleRepository.flush();
//        peopleRepository.save(people); // use with Propagation.NOT_SUPPORTED
    }
}
