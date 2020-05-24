package com.shoppingmall.fancycart.domain;

import com.shoppingmall.fancycart.auth.AuthUtils;
import com.shoppingmall.fancycart.config.auth.CustomUserDetailsService;
import com.shoppingmall.fancycart.config.auth.dto.UserPrincipal;
import com.shoppingmall.fancycart.domain.user.Role;
import com.shoppingmall.fancycart.domain.user.User;
import com.shoppingmall.fancycart.domain.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.shoppingmall.fancycart.auth.AuthUtils.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthUtils authUtils;

    @Before
    public void before() {
        userRepository.deleteAll();
        authUtils.authenticate();
    }

    // 유저 인증 테스트
    @Test
    public void userAuthenticationTest() {
        Authentication authentication = authUtils.getAuthentication();
        assertEquals(authentication.getAuthorities().stream().map(String::valueOf)
                .collect(Collectors.toList()).get(0), Role.USER.getKey());
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        assertEquals(userPrincipal.getEmail(), AUTH_EMAIL);
    }

    // 유저 조회 테스트
    @Test
    public void selectUserTest() {
        User user = findUser();

        assertEquals(user.getRoleKey(), "ROLE_USER");
        assertEquals(user.getEmail(), AUTH_EMAIL);
    }

    // 유저 정보 변경 테스트
    @Test
    public void updateUserTest() {
        User user = findUser();
        user = user.update(ROAD_ADDR, BUILDING_NAME, DETAIL_ADDR);

        userRepository.save(user);

        User modifiedUser = findUser();

        assertEquals(modifiedUser.getRoadAddr(), ROAD_ADDR);
        assertEquals(modifiedUser.getBuildingName(), BUILDING_NAME);
        assertEquals(modifiedUser.getDetailAddr(), DETAIL_ADDR);
    }

    private User findUser() {
        Optional<User> user = userRepository.findByEmail(AUTH_EMAIL);
        user.orElseThrow(NoSuchElementException::new);
        return user.get();
    }
}
