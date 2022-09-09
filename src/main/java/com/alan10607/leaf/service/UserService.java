package com.alan10607.leaf.service;

import com.alan10607.leaf.dto.LeafUserDTO;
import com.alan10607.leaf.model.LeafRole;

import java.util.List;

public interface UserService {
    LeafUserDTO findUser(LeafUserDTO leafUserDTO);
    List<LeafUserDTO> findAllUser();
    void createUser(LeafUserDTO leafUserDTO);
    void deleteUser(LeafUserDTO leafUserDTO);
    void saveRole(LeafRole leafRole);
}