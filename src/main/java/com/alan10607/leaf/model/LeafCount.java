package com.alan10607.leaf.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeafCount {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(nullable = false)
    private String leafName;

    private Long choice1;
    private Long choice2;
    private LocalDateTime updatedDate;

    public LeafCount(String leafName,
                     Long choice1,
                     Long choice2,
                     LocalDateTime updatedDate) {
        this.leafName = leafName;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.updatedDate = updatedDate;
    }

}