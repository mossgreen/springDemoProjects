package com.mossj.springsecurity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    @RequestMapping("/loginPage")
    public String login() {
        return "login_page";
    }

    @RequestMapping("/logoutPage")
    public String logout() {
        return "logout_page";
    }
}
