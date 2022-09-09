package com.alan10607.leaf.dto;

import com.alan10607.leaf.model.LeafRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Data
@NoArgsConstructor
public class LeafUserDTO {
    private long id;
    private String userName;
    private String email;
    private String pw;
    private List<LeafRole> userRole;
    private LocalDateTime updatedDate;

    public LeafUserDTO(long id,
                       String userName,
                       String email,
                       String pw,
                       List<LeafRole> userRole,
                       LocalDateTime updatedDate) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.pw = pw;
        this.userRole = userRole;
        this.updatedDate = updatedDate;
    }
}