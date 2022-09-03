package com.alan10607.leaf.service;

import com.alan10607.leaf.dto.LeafDTO;

public interface ViewService {
    LeafDTO findCountFromRedis(String leafName);
    boolean findCountFromDB(String leafName) throws Exception;
    long countIncr(String leafName, int voteFor);
//    void saveCountToDB();
}