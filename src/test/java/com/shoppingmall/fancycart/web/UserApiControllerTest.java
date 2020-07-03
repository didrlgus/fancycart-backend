package com.shoppingmall.fancycart.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingmall.fancycart.domain.user.UserRepository;
import com.shoppingmall.fancycart.excepaion.ErrorResponse;
import com.shoppingmall.fancycart.web.dto.AuthenticationRequestDto;
import com.shoppingmall.fancycart.web.dto.UserRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static com.shoppingmall.fancycart.utils.ApiUtils.API_VERSION;
import static com.shoppingmall.fancycart.utils.ExceptionUtils.*;
import static com.shoppingmall.fancycart.utils.RequestSuccessUtils.ADD_USER_SUCCESS_MESSAGE;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @After
    public void after() {
        userRepository.deleteAll();
    }

    // 회원가입 테스트
    @Test
    public void addUserTest() throws Exception {
        UserRequestDto.Post userPostRequestDto = getValidUserPostRequestDto();

        MvcResult result = callAddUserAPI(userPostRequestDto, status().isOk());
        assertEquals(result.getResponse().getContentAsString(), ADD_USER_SUCCESS_MESSAGE);
    }

    // 회원가입 유효성 테스트 (올바르지 않은 입력값)
    @Test
    public void addUserValidInputValTest() throws Exception {
        UserRequestDto.Post userPostRequestDto = getInValidUserPostRequestDto();

        MvcResult result = callAddUserAPI(userPostRequestDto, status().is4xxClientError());

        ErrorResponse errorResponse = getErrorResponse(result);

        assertEquals(errorResponse.getMessage(), INPUT_EXCEPTION_MESSAGE);
    }

    // 회원가입 유효성 테스트 (이메일 중복)
    @Test
    public void addUserDuplicatedMailTest() throws Exception {
        UserRequestDto.Post userPostRequestDto = getValidUserPostRequestDto();

        MvcResult result = null;
        for(int i = 0; i < 2; i++) {
            ResultMatcher status = i == 0 ? status().isOk() : status().is4xxClientError();
            result = callAddUserAPI(userPostRequestDto, status);
        }

        ErrorResponse errorResponse = getErrorResponse(result);

        assertEquals(errorResponse.getMessage(), DUPLICATED_EMAIL_MASSAGE);
    }

    // 인증(로그인) 테스트
    @Test
    public void authenticationTest() throws Exception {
        // 회원 가입
        UserRequestDto.Post userPostRequestDto = getValidUserPostRequestDto();
        callAddUserAPI(userPostRequestDto, status().isOk());

        // 로그인
        AuthenticationRequestDto authenticationRequestDto = getAuthenticationRequestDto(userPostRequestDto);
        callAuthenticationAPI(authenticationRequestDto, status().isOk());
    }

    // 인증(로그인) 실패 테스트
    @Test
    public void authenticationFailTest() throws Exception {
        // 회원 가입
        UserRequestDto.Post userPostRequestDto = getValidUserPostRequestDto();
        callAddUserAPI(userPostRequestDto, status().isOk());

        // 로그인 (아이디 o, 비밀번호 x)
        AuthenticationRequestDto authenticationRequestDto = getInvalidPwdAuthenticationRequestDto(userPostRequestDto);
        MvcResult result = callAuthenticationAPI(authenticationRequestDto, status().is4xxClientError());
        ErrorResponse errorResponse = getErrorResponse(result);
        assertEquals(errorResponse.getMessage(), AUTHENTICATION_EXCEPTION_MESSAGE);

        // 로그인 (아이디 x)
        authenticationRequestDto = getInvalidEmaliAuthenticationRequestDto(userPostRequestDto);
        result = callAuthenticationAPI(authenticationRequestDto, status().is4xxClientError());
        errorResponse = getErrorResponse(result);
        assertEquals(errorResponse.getMessage(), AUTHENTICATION_EXCEPTION_MESSAGE);
    }

    public MvcResult callAddUserAPI(UserRequestDto.Post requestDto, ResultMatcher status) throws Exception {
        return mockMvc.perform(post(API_VERSION + "/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status)
                .andReturn();
    }

    public MvcResult callAuthenticationAPI(AuthenticationRequestDto authenticationRequestDto,
                                           ResultMatcher status) throws Exception {
        return mockMvc.perform(post(API_VERSION + "/authentication")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(authenticationRequestDto)))
                .andExpect(status)
                .andReturn();
    }

    public UserRequestDto.Post getValidUserPostRequestDto() {
        return UserRequestDto.Post.builder()
                .name("test")
                .email("test@gmail.com")
                .password("1234")
                .agreeMessageByEmail(true)
                .roadAddr("정자로")
                .buildingName("한솔마을")
                .detailAddr("505-501")
                .build();
    }

    public UserRequestDto.Post getInValidUserPostRequestDto() {
        return UserRequestDto.Post.builder()
                .name("test")
                .email("test@gmail.com")
                .password("")
                .agreeMessageByEmail(true)
                .roadAddr("정자로")
                .buildingName("한솔마을")
                .detailAddr("505-501")
                .build();
    }

    public ErrorResponse getErrorResponse(MvcResult result) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
    }

    public AuthenticationRequestDto getAuthenticationRequestDto(UserRequestDto.Post userPostRequestDto) {
        return AuthenticationRequestDto.builder()
                .email(userPostRequestDto.getEmail())
                .password(userPostRequestDto.getPassword())
                .build();
    }

    public AuthenticationRequestDto getInvalidPwdAuthenticationRequestDto(UserRequestDto.Post userPostRequestDto) {
        String inValidPassword = "1111";
        return AuthenticationRequestDto.builder()
                .email(userPostRequestDto.getEmail())
                .password(inValidPassword)
                .build();
    }

    public AuthenticationRequestDto getInvalidEmaliAuthenticationRequestDto(UserRequestDto.Post userPostRequestDto) {
        String inValidEmail = "test1@gmail.com";
        return AuthenticationRequestDto.builder()
                .email(inValidEmail)
                .password(userPostRequestDto.getPassword())
                .build();
    }
}
