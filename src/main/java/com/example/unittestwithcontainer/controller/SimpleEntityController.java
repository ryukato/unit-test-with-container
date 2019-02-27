package com.example.unittestwithcontainer.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.unittestwithcontainer.request.SimpleEntityCreateRequest;
import com.example.unittestwithcontainer.service.SimpleEntityService;
import com.example.unittestwithcontainer.dto.SimpleEntity;

@RestController
public class SimpleEntityController {
    SimpleEntityService simpleEntityService;

    public SimpleEntityController(SimpleEntityService simpleEntityService) {
        this.simpleEntityService = simpleEntityService;
    }

    @GetMapping("/simple-entity")
    public Collection<SimpleEntity> getAll() {
        return this.simpleEntityService.getAll();
    }

    @PostMapping("/simple-entity")
    public SimpleEntity save(HttpServletRequest request, @RequestBody SimpleEntityCreateRequest simpleEntityCreateRequest) {
        return simpleEntityService.save(simpleEntityCreateRequest);
    }


}
