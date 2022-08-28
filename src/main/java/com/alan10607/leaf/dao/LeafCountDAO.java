package com.alan10607.leaf.dao;

import com.alan10607.leaf.model.LeafCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeafCountDAO extends JpaRepository<LeafCount, Long> {
    Optional<LeafCount> findByLeafName(String leafName);

    /*
    @Transactional
    @Modifying
    @Query(value = "UPDATE leafCount c SET c.choice1 = ?1, c.choice2 = ?2, c.updatedDate = ?3 WHERE c.leafName = ?4")
    int updateChoice(long choice1, long choice2, LocalDateTime now, String leafName);
    */

}