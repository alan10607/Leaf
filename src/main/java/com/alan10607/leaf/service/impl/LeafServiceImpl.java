package com.alan10607.leaf.service.impl;

import com.alan10607.leaf.dao.LeafDAO;
import com.alan10607.leaf.dto.LeafDTO;
import com.alan10607.leaf.model.Leaf;
import com.alan10607.leaf.service.LeafService;
import com.alan10607.leaf.service.ViewService;
import com.alan10607.leaf.util.TimeUtil;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LeafServiceImpl implements LeafService {
    private final ViewService viewService;
    private final LeafDAO leafDAO;
    private final TimeUtil timeUtil;

    /**
     * Find leaf by leaf name
     * @param leafDTO
     * @return
     */
    public LeafDTO find(LeafDTO leafDTO) {
        Leaf leaf = leafDAO.findByLeafName(leafDTO.getLeafName())
                .orElseThrow(() -> new IllegalStateException("LeafName not found"));

        leafDTO.setId(leaf.getId());
        leafDTO.setGood(leaf.getGood());
        leafDTO.setBad(leaf.getBad());
        leafDTO.setUpdatedDate(leaf.getUpdatedDate());
        return leafDTO;
    }

    /**
     * Find all leaf
     * @return
     */
    public List<LeafDTO> findAll() {
        List<LeafDTO> leafDTOList = leafDAO.findAll().stream()
                .map((leaf) -> {
                    return new LeafDTO(leaf.getId(),
                            leaf.getLeafName(),
                            leaf.getGood(),
                            leaf.getBad(),
                            leaf.getUpdatedDate());
                }).collect(Collectors.toList());
        return leafDTOList;
    }

    /**
     * Update leaf count
     * @param leafDTO
     */
    public void update(LeafDTO leafDTO) {
        if(Strings.isBlank(leafDTO.getLeafName()) || leafDTO.getGood() == null || leafDTO.getBad() == null)
            throw new IllegalStateException("Required parameter miss");

        Leaf leaf = leafDAO.findByLeafName(leafDTO.getLeafName())
                .orElseThrow(() -> new IllegalStateException("LeafName not found"));

        leaf.setGood(leaf.getGood() + leafDTO.getGood());
        leaf.setBad(leaf.getBad() + leafDTO.getBad());
        leaf.setUpdatedDate(timeUtil.now());
        leafDAO.save(leaf);
    }

    /**
     * Create a new leaf
     * @param leafDTO
     */
    public void create(LeafDTO leafDTO) {
        if(Strings.isBlank(leafDTO.getLeafName()))
            throw new IllegalStateException("LeafName can't be blank");

        leafDAO.findByLeafName(leafDTO.getLeafName())
                .ifPresent((l) -> { throw new IllegalStateException("LeafName already exist"); });

        Leaf leaf = new Leaf(leafDTO.getLeafName(), 0L, 0L, timeUtil.now());
        leafDAO.save(leaf);

        //更新Redis批次的leafName list
        viewService.findAllLeafNameFromDB();
    }

    /**
     * Delete a leaf
     * @param leafDTO
     */
    public void delete(LeafDTO leafDTO) {
        if(Strings.isBlank(leafDTO.getLeafName()))
            throw new IllegalStateException("LeafName can't be blank");

        Leaf leaf = leafDAO.findByLeafName(leafDTO.getLeafName())
                .orElseThrow(() -> new IllegalStateException("LeafName not found"));

        leafDAO.delete(leaf);

        //更新Redis批次的leafName list
        viewService.findAllLeafNameFromDB();
    }

}