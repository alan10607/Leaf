package com.alan10607.leaf.service;

import com.alan10607.leaf.dto.LeafDTO;

public interface LeafService {
    LeafDTO getCount(String leafName);
    void vote(LeafDTO leafDTO);
    void create(LeafDTO leafDTO);
}