package com.alan10607.leaf.service;

import com.alan10607.leaf.dto.LeafDTO;

public interface ViewService {
    LeafDTO findCountFromRedis(String leafName);
//    void findCountFromDB(String leafName);
    long countIncr(String leafName, int voteFor);
//    void saveCountToDB();
}