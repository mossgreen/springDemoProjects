package com.mossj.springsecurity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @GetMapping(value = {"/", "/home"})
    public String home() {
        return "index";
    }

    @GetMapping("/api")
    @ResponseBody
    public String api() {
        return "security api";
    }

    @GetMapping("/home/ip")
    @ResponseBody
    public String ip(HttpServletRequest request) {
        return request.getRemoteAddr();
    }
}
