package com.alan10607.leaf.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Data
@NoArgsConstructor
public class LeafDTO {
    private String leafName;
    private Long good;
    private Long bad;
    private String voteFor;
    private Long id;
    private LocalDateTime updatedDate;

    public LeafDTO(Long id,
                   String leafName,
                   Long good,
                   Long bad,
                   LocalDateTime updatedDate) {
        this.id = id;
        this.leafName = leafName;
        this.good = good;
        this.bad = bad;
        this.updatedDate = updatedDate;
    }
}