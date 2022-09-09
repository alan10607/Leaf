package com.alan10607.leaf.service;

import com.alan10607.leaf.dto.LeafDTO;

import java.util.List;

public interface ViewService {
    LeafDTO findCountFromRedis(LeafDTO leafDTO) throws Exception;
    LeafDTO countIncr(LeafDTO leafDTO) throws Exception;
    boolean findCountFromDB(String leafName) throws Exception;
    boolean saveCountToDB() throws Exception;
    List<String> findAllLeafNameFromDB();
}