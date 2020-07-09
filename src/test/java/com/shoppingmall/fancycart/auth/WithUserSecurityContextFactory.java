package com.shoppingmall.fancycart.auth;

import com.shoppingmall.fancycart.service.CustomUserDetailsService;
import com.shoppingmall.fancycart.service.UserService;
import com.shoppingmall.fancycart.web.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class WithUserSecurityContextFactory implements WithSecurityContextFactory<WithUser> {

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public SecurityContext createSecurityContext(WithUser withUser) {
        String name = withUser.value();
        String email = name + "@naver.com";
        userService.addUser(UserRequestDto.Post.builder()
                .name(name)
                .email(email)
                .password("1234")
                .build());

        UserDetails principal = customUserDetailsService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        return context;
    }
}
