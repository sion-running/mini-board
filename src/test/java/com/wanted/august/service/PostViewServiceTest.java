package com.wanted.august.service;

import com.wanted.august.model.entity.PostEntity;
import com.wanted.august.model.entity.PostViewEntity;
import com.wanted.august.repository.PostViewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostViewServiceTest {
    @InjectMocks
    PostViewServiceImpl postViewService;

    @Mock
    PostViewRepository postViewRepository;

    @Mock
    PostServiceImpl postService;

    @Mock
    UserServiceImpl userService;

    @Test
    void 유저가_같은_포스트를_클릭하면_조회수_테이블에_insert_하지_않는다() {
        // given
        Long postId = 1L;
        String userName = "tester";
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDaysAgo = now.minus(1, ChronoUnit.DAYS);

        PostViewEntity postViewEntity = PostViewEntity.builder()
                .id(1L)
                .postId(postId)
                .userName(userName)
                .createdAt(oneDaysAgo)
                .updatedAt(oneDaysAgo)
                .build();

        // when
        when(postViewRepository.findByPostIdAndUserName(postId, userName)).thenReturn(Optional.of(postViewEntity));

        postViewService.addCount(postId, userName);

        assertThat(postViewEntity.getId()).isEqualTo(1L);
        assertThat(postViewEntity.getUpdatedAt()).isAfter(oneDaysAgo);
    }

    @Test
    void 유저가_처음으로_포스트를_클릭하면_테이블에_insert_한다() {
        // given
        Long postId = 1L;
        String userName = "tester";

        PostViewEntity postViewEntity = PostViewEntity.builder()
                .id(1L)
                .postId(postId)
                .userName(userName)
                .build();

        // when
        when(postViewRepository.save(ArgumentMatchers.any(PostViewEntity.class))).thenReturn(postViewEntity);

        PostViewEntity actual = postViewService.addCount(postId, userName);

        assertThat(actual.getId()).isEqualTo(postViewEntity.getId());
    }
}
