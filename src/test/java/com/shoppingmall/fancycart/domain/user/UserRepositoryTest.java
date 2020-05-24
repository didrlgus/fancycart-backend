package com.shoppingmall.fancycart.domain.user;

import com.shoppingmall.fancycart.config.auth.CustomUserDetailsService;
import com.shoppingmall.fancycart.config.auth.JwtUtilService;
import com.shoppingmall.fancycart.config.auth.dto.UserPrincipal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtUtilService jwtUtilService;

    // 사전 인증 처리
    @Before
    public void before() {
        userRepository.save(User.builder().role(Role.GUEST).email("rlgusdid@naver.com").name("양기현").build());

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("rlgusdid@naver.com");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
    // 유저 생성 테스트
    @Test
    public void getUserTest() throws Exception {
        Optional<User> userOpt = userRepository.findByEmail("rlgusdid@naver.com");
        User user = userOpt.get();

        assertNotNull(user);
        assertEquals(user.getEmail(),"rlgusdid@naver.com");
        assertEquals(user.getName(), "양기현");
        assertEquals(user.getRole(), Role.GUEST);
    }
    // jwt 토큰 발급, 유효성 검사 테스트
    @Test
    public void getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtUtilService.createToken(authentication);
        String userName = jwtUtilService.extractUsername(token);

        assertNotNull(authentication);
        assertEquals(userName, "rlgusdid@naver.com");
        assertTrue(jwtUtilService.validateToken(token));
    }

    @After
    public void after() {
        userRepository.deleteAll();
    }
}
