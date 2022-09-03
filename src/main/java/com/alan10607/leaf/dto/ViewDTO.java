package com.alan10607.leaf.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class ViewDTO {
    private Long good;
    private Long bad;
}