package com.example.unittestwithcontainer.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.unittestwithcontainer.dto.SimpleEntity;

public interface SimpleEntityRepository extends JpaRepository<SimpleEntity, Integer> {
}
