package com.shoppingmall.fancycart.web;

import com.shoppingmall.fancycart.config.auth.LoginUser;
import com.shoppingmall.fancycart.config.auth.dto.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/api/v1/profiles")
    public String getProfileView(Model model, @LoginUser SessionUser user) {

        if(user != null) {
            model.addAttribute("user", user);
        }

        return "me/profiles";
    }
}
