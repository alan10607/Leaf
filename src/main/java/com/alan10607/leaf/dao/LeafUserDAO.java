package com.alan10607.leaf.dao;

import com.alan10607.leaf.model.LeafUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeafUserDAO extends JpaRepository<LeafUser, Long> {
    Optional<LeafUser> findByEmail(String email);
}