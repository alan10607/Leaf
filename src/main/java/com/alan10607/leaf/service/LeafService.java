package com.alan10607.leaf.service;

import com.alan10607.leaf.dto.LeafDTO;

public interface LeafService {
    LeafDTO findCount(String leafName);
    void updateCount(LeafDTO leafDTO);
    void create(LeafDTO leafDTO);
}