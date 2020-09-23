package com.task.task.repository;

import com.task.task.entities.BlackListsJWT;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListsJWTRepository extends JpaRepository<BlackListsJWT, Long> {

    boolean existsByJwt(String jwt);
}
