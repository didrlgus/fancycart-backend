package com.shoppingmall.fancycart.config.auth;

import com.shoppingmall.fancycart.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/api/v1/category", "/api/v1/products", "/api/v1/products/**").permitAll()
                .antMatchers("/api/v1/profile/**", "/api/v1/user/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                .antMatchers(HttpMethod.POST, "/api/v1/products/**/reviews/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                .antMatchers(HttpMethod.POST, "/api/v1/category", "/api/v1/products").hasRole(Role.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/api/v1/category/**", "/api/v1/products/**").hasRole(Role.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/api/v1/category/**", "/api/v1/products/**").hasRole(Role.ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
    }
}