package com.wanted.august.service;

import com.wanted.august.model.Post;
import com.wanted.august.model.entity.PostEntity;
import com.wanted.august.model.entity.UserEntity;
import com.wanted.august.model.request.PostCreateRequest;
import com.wanted.august.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @InjectMocks
    PostServiceImpl postService;

    @Mock
    UserServiceImpl userService;

    @Mock
    PostRepository postRepository;

    @Test
    void 포스트_등록_성공() {
        // given
        String userName = "sion1234";
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName(userName)
                .password("encodedPassword")
                .build();

        PostCreateRequest request = PostCreateRequest.builder()
                .title("포스트 제목1")
                .content("포스트 내용")
                .build();

        PostEntity postEntity = PostEntity.toEntity(request, userEntity);
        when(userService.findByUserNameOrElseThrow(userName)).thenReturn(userEntity);
        when(postRepository.save(ArgumentMatchers.any(PostEntity.class))).thenReturn(postEntity);

        Post actual = postService.create(request, userName);
        assertThat(actual.getTitle()).isEqualTo(request.getTitle());
        assertThat(actual.getContent()).isEqualTo(request.getContent());
    }
}
