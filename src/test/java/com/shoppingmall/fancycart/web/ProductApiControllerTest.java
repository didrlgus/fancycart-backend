package com.shoppingmall.fancycart.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingmall.fancycart.web.dto.ProductRequestDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.shoppingmall.fancycart.utils.ApiUtils.API_VERSION;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductApiControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

//    @Test
//    public void getProductsTest() throws Exception {
//        ProductRequestDto.Get productRequestDto = ProductRequestDto.Get.builder()
//                .category("bags")
//                .build();
//        MvcResult result = mockMvc.perform(get(API_VERSION + "/products")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(new ObjectMapper().writeValueAsString(productRequestDto)))
//                .andExpect(status().isOk())
//                .andReturn();
//    }

}
