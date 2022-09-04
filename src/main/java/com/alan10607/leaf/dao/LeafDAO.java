package com.alan10607.leaf.dao;

import com.alan10607.leaf.model.Leaf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeafDAO extends JpaRepository<Leaf, Long> {
    Optional<Leaf> findByLeafName(String leafName);

    @Query(value = "SELECT l.leafName FROM Leaf l")
    List<String> findLeafName();

    @Transactional
    @Modifying
    @Query(value = "UPDATE Leaf l SET l.good = ?1, l.bad = ?2, l.updatedDate = ?3 WHERE l.leafName = ?4")
    int updateCounts(long good, long bad, LocalDateTime now, String leafName);

}