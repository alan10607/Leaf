package com.alan10607.leaf.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/")
@AllArgsConstructor
@Slf4j
public class WebPageController {

    @RequestMapping("/index")
    public String index(Model model){
        try{
            String leafName = "leaf";//暫時給個預設
            model.addAttribute("leafName", leafName);
            model.addAttribute("picFileName", "leaf.jpg");//可以改成從DB查到
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return "index.html";//應對templates下的檔案
    }

    @RequestMapping("/index/{leafName}")
    public String indexWithLeafName(@PathVariable("leafName") String leafName, Model model){
        try{
            model.addAttribute("leafName", leafName);
            model.addAttribute("picFileName", "leaf.jpg");
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return "index.html";
    }

    @RequestMapping("/admin")
    public String admin(Model model) {
        return "admin/admin.html";
    }

    @RequestMapping("/admin/manager")
    public String manager(Model model) {
        return "admin/manager.html";
    }

    @GetMapping("/test")
    public String test(){
        return "Hello Leaf";
    }

}