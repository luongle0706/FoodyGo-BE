package com.foodygo.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sdw391/v1/user")
public class UserController {

    @GetMapping("/test")
    public String testne() {
        return "testne";
    }

}
