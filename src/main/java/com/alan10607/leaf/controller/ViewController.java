package com.alan10607.leaf.controller;

import com.alan10607.leaf.dto.LeafDTO;
import com.alan10607.leaf.service.LeafService;
import com.alan10607.leaf.service.ViewService;
import com.alan10607.leaf.util.ResponseUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/view")
@AllArgsConstructor
@Slf4j
public class ViewController {

    private final LeafService leafService;
    private final ViewService viewService;
    private final ResponseUtil responseUtil;

    @PostMapping("/getCount")
    public ResponseEntity getCount(@RequestBody LeafDTO leafDTO){
        try{
            leafDTO = viewService.findCountFromRedis(leafDTO);
            return responseUtil.ok(leafDTO);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseUtil.err(e);
        }
    }

    @PostMapping("/vote")
    public ResponseEntity vote(@RequestBody LeafDTO leafDTO){
        try{
            leafDTO = viewService.countIncr(leafDTO);
            return responseUtil.ok(leafDTO);
        }catch (Exception e){
            log.error("", e);
            return responseUtil.err(e);
        }
    }

    @PostMapping("/test")
    public ResponseEntity test(@RequestBody LeafDTO leafDTO){
        try{
            int r = (int) (Math.random() * 10);
            if(r <= 1){
                leafDTO.setVoteFor(r == 0 ? "good" : "bad");
                viewService.countIncr(leafDTO);
            }else{
                leafDTO = viewService.findCountFromRedis(leafDTO);
            }
            return responseUtil.ok(leafDTO);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseUtil.err(e);
        }
    }

    @PostMapping("/test2")
    public ResponseEntity test2(@RequestBody LeafDTO leafDTO){
        try{
            //viewService.findCountFromRedis(leafDTO);
            //viewService.countIncr(leafDTO);
            viewService.saveCountToDB();
            return responseUtil.ok(leafDTO);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseUtil.err(e);
        }
    }

    @PostMapping("/test3")
    public ResponseEntity test3(@RequestBody LeafDTO leafDTO){
        try{
            viewService.findCountFromDB(leafDTO.getLeafName());
            return responseUtil.ok(leafDTO);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseUtil.err(e);
        }
    }
}