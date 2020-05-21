package com.shoppingmall.fancycart.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Controller
public class IndexController {

    @GetMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response,
                        Model model, Authentication authentication) {
        if(authentication != null) {
            model.addAttribute("userName", authentication.getName());
        }

        return "index";
    }
}
