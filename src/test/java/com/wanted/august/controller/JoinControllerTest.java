package com.wanted.august.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.august.model.UserRole;
import com.wanted.august.model.request.UserJoinRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JoinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
//    @WithAnonymousUser
    public void 회원가입시_작성자_유효성검증_통과() throws Exception {
        String userName = "sion1234";
        String writer = "sion";
        String password = "paSS123!@#";
        String email = "sion@naver.com";
        String phone = "010-1111-2222";

//        when(userService.join(userName, password)).thenReturn(mock(User.class));
        UserJoinRequest request = new UserJoinRequest(
                userName,
                writer,
                password,
                email,
                phone,
                UserRole.USER
        );

        mockMvc.perform(post("/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
//    @WithAnonymousUser
    public void 회원가입시_이메일_유효성검사() throws Exception {
        String userName = "sion1234";
        String writer = "sion";
        String password = "paSS123!@#";
        String email = "sionnaver.com";
        String phone = "010-1111-2222";

        UserJoinRequest request = new UserJoinRequest(
                userName,
                writer,
                password,
                email,
                phone,
                UserRole.USER
        );

        mockMvc.perform(post("/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
//    @WithAnonymousUser
    public void 회원가입시_휴대폰_유효성검사() throws Exception {
        String userName = "sion1234";
        String writer = "sion";
        String password = "paSS123!@#";
        String email = "sion@naver.com";
        String phone = "010-11112222";

        UserJoinRequest request = new UserJoinRequest(
                userName,
                writer,
                password,
                email,
                phone,
                UserRole.USER
        );

        mockMvc.perform(post("/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
//    @WithAnonymousUser
    public void 회원가입시_작성자() throws Exception {
        String userName = "sion1234";
        String writer = "sion";
        String password = "paSS123!@#";
        String email = "sionnaver.com";
        String phone = "010-1111-2222";

        UserJoinRequest request = new UserJoinRequest(
                userName,
                writer,
                password,
                email,
                phone,
                UserRole.USER
        );

        mockMvc.perform(post("/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
