package com.alan10607.leaf.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Leaf {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String leafName;

    private Long good;
    private Long bad;
    private LocalDateTime updatedDate;

    public Leaf(String leafName,
                Long good,
                Long bad,
                LocalDateTime updatedDate) {
        this.leafName = leafName;
        this.good = good;
        this.bad = bad;
        this.updatedDate = updatedDate;
    }

}