package com.alan10607.leaf.constant;

import com.alan10607.leaf.dto.LeafDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CountType {
    UNDEFINED(-1, ""),
    GOOD(1, "good"),
    BAD(0, "bad");
    private int voteFor;
    private String field;

    public LeafDTO getResLeafDTO(LeafDTO leafDTO, long res) {
        switch (this) {
            case GOOD:
                leafDTO.setGood(res);
                break;
            case BAD:
                leafDTO.setBad(res);
                break;
            default:
        }
        return leafDTO;
    }
}