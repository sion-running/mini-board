package com.wanted.august.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.august.model.User;
import com.wanted.august.model.request.PostCreateRequest;
import com.wanted.august.service.UserServiceImpl;
import com.wanted.august.utils.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    @WithMockUser(username = "sion1234", roles = {"USER"})
    void 포스트_제목이_200자를_넘으면_에러발생() throws Exception {
        PostCreateRequest request = PostCreateRequest.builder()
                .title("title 12345 title 12345 title 12345 title 12345 title 12345 title 12345 title 12345 title 12345 title 12345 title 12345 title 12345 title 12345 title 12345 title 12345 title 12345 title 12345 title 12345")
                .content("내용1")
                .build();

        User mockUser = User.builder().username("sion1234").build();
        when(userService.loadUserByUsername("sion1234")).thenReturn(mockUser);

        // ObjectMapper를 실제로 사용하지 않도록 수정
        // 미리 json 변환된 내용을 설정
        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "sion1234", roles = {"USER"})
    void 포스트_내용이_1000자를_넘으면_에러발생() throws Exception {
        PostCreateRequest request = PostCreateRequest.builder()
                .title("title 1")
                .content("content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345 content 12345")
                .build();

        User mockUser = User.builder().username("sion1234").build();
        when(userService.loadUserByUsername("sion1234")).thenReturn(mockUser);

        // ObjectMapper를 실제로 사용하지 않도록 수정
        // 미리 json 변환된 내용을 설정
        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
