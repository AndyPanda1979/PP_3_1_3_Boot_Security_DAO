package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.models.User;


@Controller
public class UserController {

    @GetMapping(value = "/user/userinfo")
    public String userPage(ModelMap model, @AuthenticationPrincipal User testUser) {
        model.addAttribute("user", testUser);
        return ("user/userdetails_new");
    }
}
