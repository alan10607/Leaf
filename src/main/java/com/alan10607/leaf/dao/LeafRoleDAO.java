package com.alan10607.leaf.dao;

import com.alan10607.leaf.model.LeafRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeafRoleDAO extends JpaRepository<LeafRole, Long> {
    LeafRole findByRoleName(String roleName);
}