package com.alan10607.leaf.service;

import com.alan10607.leaf.dto.LeafDTO;

public interface ViewService {
    LeafDTO findCountFromRedis(LeafDTO leafDTO) throws Exception;
    void countIncr(LeafDTO leafDTO) throws Exception;
    boolean findCountFromDB(String leafName) throws Exception;
    boolean saveCountToDB() throws Exception;
}