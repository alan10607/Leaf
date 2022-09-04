package com.alan10607.leaf.controller;

import com.alan10607.leaf.dto.LeafDTO;
import com.alan10607.leaf.service.LeafService;
import com.alan10607.leaf.service.ViewService;
import com.alan10607.leaf.util.ResponseUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Controller
@RequestMapping(path = "/")
@AllArgsConstructor
@Slf4j
public class ViewController {

    private final LeafService leafService;
    private final ViewService viewService;
    private final ResponseUtil responseUtil;


    @RequestMapping("/index")
    public String index(@RequestParam(value = "leafName", required = false) String leafName, Model model){
        try{
            if(leafName == null) leafName = "leaf";//!!!暫時給個預設 還沒想好怎麼給明子

            model.addAttribute("leafName", leafName);
            model.addAttribute("baseUrl", "/view");
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return "index";//應對templates下的檔案
    }
/*
    @PostMapping("/vote")
    public ResponseEntity vote(@RequestBody LeafDTO leafDTO){
        try{
            Long res = viewService.countIncr(leafDTO.getLeafName(), leafDTO.getVoteFor());
            return responseUtil.ok(res);
        }catch (Exception e){
            log.error("", e);
            return responseUtil.err(e);
        }
    }

*/
    @PostMapping("/view/getCount")
    public ResponseEntity getCount(@RequestBody LeafDTO leafDTO){
        try{
            leafDTO = viewService.findCountFromRedis(leafDTO);
            return responseUtil.ok(leafDTO);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseUtil.err(e);
        }
    }

    @PostMapping("/view/test")
    public ResponseEntity test(@RequestBody LeafDTO leafDTO){
        try{
            int r = (int) (Math.random() * 10);
            if(r <= 1){
                leafDTO.setVoteFor(r + 1);
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

    @PostMapping("/view/test2")
    public ResponseEntity test2(@RequestBody LeafDTO leafDTO){
        try{
            //viewService.findCountFromRedis(leafDTO);
            viewService.countIncr(leafDTO);
            return responseUtil.ok(leafDTO);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseUtil.err(e);
        }
    }

    @PostMapping("/view/test3")
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
