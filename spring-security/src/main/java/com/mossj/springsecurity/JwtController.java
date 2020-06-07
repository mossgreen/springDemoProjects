package com.mossj.springsecurity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

    @GetMapping("/api/guest")
    public String guest() {
        return "This is a guest api msg";
    }

    @GetMapping("/api/admin")
    public String admin() {
        return "this is a admin api msg";
    }
}
