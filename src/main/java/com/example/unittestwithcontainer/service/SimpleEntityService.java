package com.example.unittestwithcontainer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.unittestwithcontainer.dto.SimpleEntity;
import com.example.unittestwithcontainer.repo.SimpleEntityRepository;
import com.example.unittestwithcontainer.request.SimpleEntityCreateRequest;

@Service
public class SimpleEntityService {

    SimpleEntityRepository simpleEntityRepository;

    public SimpleEntityService(SimpleEntityRepository simpleEntityRepository) {
        this.simpleEntityRepository = simpleEntityRepository;
    }

    public SimpleEntity save(SimpleEntityCreateRequest simpleEntityCreateRequest) {
        SimpleEntity simpleEntity = new SimpleEntity();
        simpleEntity.setName(simpleEntityCreateRequest.getName());
        return simpleEntityRepository.save(simpleEntity);
    }

    public Optional<SimpleEntity> getById(Integer id) {
        return simpleEntityRepository.findById(id);
    }

    public List<SimpleEntity> getAll() {
        return simpleEntityRepository.findAll();
    }

}
