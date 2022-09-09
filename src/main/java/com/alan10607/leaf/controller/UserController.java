package com.alan10607.leaf.controller;

import com.alan10607.leaf.dto.LeafUserDTO;
import com.alan10607.leaf.service.UserService;
import com.alan10607.leaf.util.ResponseUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final ResponseUtil responseUtil;

    @PostMapping("/findUser")
    public ResponseEntity findUser(@RequestBody LeafUserDTO leafUserDTO){
        try{
            leafUserDTO = userService.findUser(leafUserDTO);
            return responseUtil.ok(leafUserDTO);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseUtil.err(e);
        }
    }

    @PostMapping("/findAllUser")
    public ResponseEntity findAllUser(@RequestBody LeafUserDTO leafUserDTO){
        try{
            List<LeafUserDTO> leafUserDTOList = userService.findAllUser();
            return responseUtil.ok(leafUserDTOList);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseUtil.err(e);
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity createUser(@RequestBody LeafUserDTO leafUserDTO){
        try{
            userService.createUser(leafUserDTO);
            return responseUtil.ok();
        }catch (Exception e){
            log.error(e.getMessage());
            return responseUtil.err(e);
        }
    }

    @PostMapping("/deleteUser")
    public ResponseEntity deleteUser(@RequestBody LeafUserDTO leafUserDTO){
        try{
            userService.deleteUser(leafUserDTO);
            return responseUtil.ok();
        }catch (Exception e){
            log.error(e.getMessage());
            return responseUtil.err(e);
        }
    }

}