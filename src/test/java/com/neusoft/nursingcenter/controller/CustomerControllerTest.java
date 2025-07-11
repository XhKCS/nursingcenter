package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neusoft.nursingcenter.entity.Customer;
import com.neusoft.nursingcenter.mapper.CustomerMapper;
import com.neusoft.nursingcenter.mapper.NursingLevelMapper;
import com.neusoft.nursingcenter.mapper.UserMapper;
import com.neusoft.nursingcenter.redisdao.RedisDao;
import com.neusoft.nursingcenter.service.CustomerService;
import com.neusoft.nursingcenter.util.WebSocket;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class CustomerControllerTest {
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private NursingLevelMapper nursingLevelMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisDao redisDao;
//    @Resource
//    private WebSocket webSocket;

    private MockMvc mockMvc;
    private MockHttpSession session;

    private CustomerController controller = new CustomerController();

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        controller.setCustomerMapper(customerMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        System.out.println("CustomerController test begin---");
    }

    @AfterEach
    void tearDown() {
        System.out.println("CustomerController test end---");
    }

    @Test
    void pageByNurseId() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        // 正常的测试用例：nurseId为4
        Map<String, Object> request1 = new HashMap<>();
        request1.put("nurseId", 4);
        request1.put("name", "");
        request1.put("current", 1);
        request1.put("size", 5);
        String jsonRequest1 = objectMapper.writeValueAsString(request1);
        RequestBuilder requestBuilder1 = MockMvcRequestBuilders
                .post("/customer/pageByNurseId")
                .contentType(MediaType.APPLICATION_JSON) //注意post不是写accept,而是写contentType
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(jsonRequest1);
        MvcResult result1 = mockMvc.perform(requestBuilder1).andReturn();
        String jsonResponse1 = result1.getResponse().getContentAsString();
        System.out.println("jsonResponse1（正常测试用例返回结果）: "+jsonResponse1);
        Map<String, Object> responseMap1 = objectMapper.readValue(jsonResponse1, Map.class);
        assertEquals(200, responseMap1.get("status"));

        // 异常的测试用例：nurseId为-1
        Map<String, Object> request2 = new HashMap<>();
        request2.put("nurseId", -1);
        request2.put("name", "");
        request2.put("current", 1);
        request2.put("size", 5);
        String jsonRequest2 = objectMapper.writeValueAsString(request2);
        RequestBuilder requestBuilder2 = MockMvcRequestBuilders
                .post("/customer/pageByNurseId")
                .contentType(MediaType.APPLICATION_JSON) //注意post不是写accept,而是写contentType
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(jsonRequest2);
        MvcResult result2 = mockMvc.perform(requestBuilder2).andReturn();
        String jsonResponse2 = result2.getResponse().getContentAsString();
        System.out.println("jsonResponse2（异常测试用例返回结果）: "+jsonResponse2);
        Map<String, Object> responseMap2 = objectMapper.readValue(jsonResponse2, Map.class);
        assertEquals(0, responseMap2.get("total"));
    }

    @Test
    void update() {

    }
}