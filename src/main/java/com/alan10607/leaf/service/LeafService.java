package com.alan10607.leaf.service;

import com.alan10607.leaf.dto.LeafDTO;

import java.util.List;

public interface LeafService {
    LeafDTO find(LeafDTO leafDTO);
    List<LeafDTO> findAll();
    void update(LeafDTO leafDTO);
    void create(LeafDTO leafDTO);
    void delete(LeafDTO leafDTO);

}