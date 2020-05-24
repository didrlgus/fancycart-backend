package com.shoppingmall.fancycart.auth;

import com.shoppingmall.fancycart.config.auth.CustomUserDetailsService;
import com.shoppingmall.fancycart.domain.user.Role;
import com.shoppingmall.fancycart.domain.user.User;
import com.shoppingmall.fancycart.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;

    public static final String ROAD_ADDR = "경기 성남시 분당구 정자로 2";
    public static final String BUILDING_NAME = "분당 정자동 푸르지오시티";
    public static final String DETAIL_ADDR = "508동 201호";
    public static final String AUTH_EMAIL = "rlgusdid@naver.com";
    public static final String NAME = "양기현";

    public void authenticate() {
        saveUser();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(AUTH_EMAIL);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public User getAuthenticatedUser() {
        return userRepository.findByEmail(AUTH_EMAIL).orElseThrow(NoSuchElementException::new);
    }

    public void saveUser() {
        userRepository.save(User.builder().role(Role.USER).email(AUTH_EMAIL).name(NAME).build());
    }
}
