package com.alan10607.leaf.service.impl;

import com.alan10607.leaf.dao.LeafCountDAO;
import com.alan10607.leaf.dto.LeafDTO;
import com.alan10607.leaf.model.LeafCount;
import com.alan10607.leaf.service.LeafService;
import com.alan10607.leaf.util.TimeUtil;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LeafServiceImpl implements LeafService {

    private final LeafCountDAO leafCountDAO;
    private final TimeUtil timeUtil;

    /**
     * Get leaf's count by leaf name
     * @param leafName
     * @return
     */
    public LeafDTO getCount(String leafName) {
        LeafCount leafCount = leafCountDAO.findByLeafName(leafName)
                .orElseThrow(() -> new IllegalStateException("LeafName Not Found"));

        LeafDTO leafDTO = new LeafDTO();
        leafDTO.setLeafName(leafCount.getLeafName());
        leafDTO.setChoice1(leafCount.getChoice1());
        leafDTO.setChoice2(leafCount.getChoice2());
        return leafDTO;
    }

    /**
     * Update leaf count
     * @param leafDTO
     */
    public void vote(LeafDTO leafDTO) {
        LeafCount leafCount = leafCountDAO.findByLeafName(leafDTO.getLeafName())
                .orElseThrow(() -> new IllegalStateException("LeafName Not Found"));

        leafCount.setChoice1(leafCount.getChoice1() + leafDTO.getChoice1());
        leafCount.setChoice2(leafCount.getChoice2() + leafDTO.getChoice2());
        leafCount.setUpdatedDate(timeUtil.now());
        leafCountDAO.save(leafCount);
    }

    /**
     * Update a new leaf
     * @param leafDTO
     */
    public void create(LeafDTO leafDTO) {
        if(Strings.isBlank(leafDTO.getLeafName()))
            throw new IllegalStateException("LeafName can't be blank");

        LeafCount leafCount = new LeafCount(leafDTO.getLeafName(), 0L, 0L, timeUtil.now());
        leafCountDAO.save(leafCount);
    }

}