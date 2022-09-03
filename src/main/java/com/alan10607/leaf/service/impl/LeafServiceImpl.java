package com.alan10607.leaf.service.impl;

import com.alan10607.leaf.dao.LeafCountDAO;
import com.alan10607.leaf.dto.LeafDTO;
import com.alan10607.leaf.model.Leaf;
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
        Leaf leaf = leafCountDAO.findByLeafName(leafName)
                .orElseThrow(() -> new IllegalStateException("LeafName Not Found"));

        LeafDTO leafDTO = new LeafDTO();
        leafDTO.setLeafName(leaf.getLeafName());
        leafDTO.setGood(leaf.getGood());
        leafDTO.setBad(leaf.getBad());
        return leafDTO;
    }

    /**
     * Update leaf count
     * @param leafDTO
     */
    public void vote(LeafDTO leafDTO) {
        Leaf leaf = leafCountDAO.findByLeafName(leafDTO.getLeafName())
                .orElseThrow(() -> new IllegalStateException("LeafName Not Found"));

        leaf.setGood(leaf.getGood() + leafDTO.getGood());
        leaf.setBad(leaf.getBad() + leafDTO.getBad());
        leaf.setUpdatedDate(timeUtil.now());
        leafCountDAO.save(leaf);
    }

    /**
     * Update a new leaf
     * @param leafDTO
     */
    public void create(LeafDTO leafDTO) {
        if(Strings.isBlank(leafDTO.getLeafName()))
            throw new IllegalStateException("LeafName can't be blank");

        if(leafCountDAO.findByLeafName(leafDTO.getLeafName()).isPresent())
            throw new IllegalStateException("LeafName already exist");

        Leaf leaf = new Leaf(leafDTO.getLeafName(), 0L, 0L, timeUtil.now());
        leafCountDAO.save(leaf);
    }

}