package com.shoppingmall.fancycart.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/api/v1/profiles")
    public String getProfileView(Model model, Authentication authentication) {
        if(authentication != null) {
            model.addAttribute("userName", authentication.getName());
        }

        return "me/profiles";
    }
}
