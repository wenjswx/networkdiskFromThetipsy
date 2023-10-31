package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class IndexController {
//    IndexController
    public String index() {
        return "index";
    }

    @GetMapping("")
    public String toUpload() {
        return "newupload";
    }
}
