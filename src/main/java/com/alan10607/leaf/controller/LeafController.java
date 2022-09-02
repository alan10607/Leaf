package com.alan10607.leaf.controller;

import com.alan10607.leaf.dto.LeafDTO;
import com.alan10607.leaf.service.LeafService;
import com.alan10607.leaf.util.ResponseUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/leaf")
@AllArgsConstructor
@Slf4j
public class LeafController {

    private final LeafService leafService;
    private final ResponseUtil responseUtil;

    @GetMapping("/getCount")
    public ResponseEntity getCount(@RequestParam String leafName){
        LeafDTO leafDTO;
        try{
            leafDTO = leafService.getCount(leafName);
            return responseUtil.ok(leafDTO);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseUtil.err(e);
        }
    }

    @PostMapping("/vote")
    public ResponseEntity vote(@RequestBody LeafDTO leafDTO){
        try{
            leafService.vote(leafDTO);
            return responseUtil.ok();
        }catch (Exception e){
            log.error(e.getMessage());
            return responseUtil.err(e);
        }
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody LeafDTO leafDTO){
        try{
            leafService.create(leafDTO);
            return responseUtil.ok();
        }catch (Exception e){
            log.error(e.getMessage());
            return responseUtil.err(e);
        }
    }

}