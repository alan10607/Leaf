package com.alan10607.leaf.controller;

import com.alan10607.leaf.dto.LeafDTO;
import com.alan10607.leaf.service.LeafService;
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
@RequestMapping(path = "/leaf")
@AllArgsConstructor
@Slf4j
public class LeafController {
    private final LeafService leafService;
    private final ResponseUtil responseUtil;

    @PostMapping("/find")
    public ResponseEntity find(@RequestBody LeafDTO leafDTO){
        try{
            leafDTO = leafService.find(leafDTO);
            return responseUtil.ok(leafDTO);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseUtil.err(e);
        }
    }

    @PostMapping("/findAll")
    public ResponseEntity findAll(@RequestBody LeafDTO leafDTO){
        try{
            List<LeafDTO> leafDTOList = leafService.findAll();
            return responseUtil.ok(leafDTOList);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseUtil.err(e);
        }
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody LeafDTO leafDTO){
        try{
            leafService.update(leafDTO);
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

    @PostMapping("/delete")
    public ResponseEntity delete(@RequestBody LeafDTO leafDTO){
        try{
            leafService.delete(leafDTO);
            return responseUtil.ok();
        }catch (Exception e){
            log.error(e.getMessage());
            return responseUtil.err(e);
        }
    }

}