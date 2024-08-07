package com.wanted.august.service;

import com.wanted.august.exception.AugustApplicationException;
import com.wanted.august.exception.ErrorCode;
import com.wanted.august.model.Post;
import com.wanted.august.model.entity.PostEntity;
import com.wanted.august.model.entity.UserEntity;
import com.wanted.august.model.request.PostCreateRequest;
import com.wanted.august.model.request.PostUpdateRequest;
import com.wanted.august.repository.PostRepository;
import com.wanted.august.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@EnableJpaAuditing
public class PostServiceTest {
    @InjectMocks
    PostServiceImpl postService;

    @Mock
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

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

    @Test
    void 포스트_생성시간이_자동으로_저장된다() { // TODO FIX
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
        assertThat(actual.getCreatedAt()).isNotNull();
    }

    @Test
    void 포스트_수정_성공() {
        // given
        String userName = "sion1234";
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName(userName)
                .password("encodedPassword")
                .build();

        PostUpdateRequest request = PostUpdateRequest.builder()
                .postId(1L)
                .title("포스트 제목1")
                .content("포스트 내용")
                .build();

        when(userService.findByUserNameOrElseThrow(userName)).thenReturn(userEntity);

    }

    @Test
    void 포스트_수정시_포스트가_존재하지_않으면_에러발생() {
        // given
        String userName = "sion1234";
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName(userName)
                .password("encodedPassword")
                .build();

        PostUpdateRequest request = PostUpdateRequest.builder().postId(1L).title("modi title").content("modi content").build();

        PostEntity entity = PostEntity.builder()
                .id(1L)
                .title("title1")
                .content("contet1")
                .user(userEntity)
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.empty());
        AugustApplicationException exception = Assertions.assertThrows(AugustApplicationException.class, () ->
                postService.update(request, userName));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 포스트_생성후_10일이상_지나면_수정불가_예외를_발생시킨다() {
        // given
        String userName = "sion1234";
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName(userName)
                .password("encodedPassword")
                .build();

        PostUpdateRequest request = PostUpdateRequest.builder()
                .postId(1L)
                .title("포스트 제목1")
                .content("포스트 내용")
                .build();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenDaysAgo = now.minus(10, ChronoUnit.DAYS); // 현재기준 10일 전을 생성일로 설정
        PostEntity entity = PostEntity.builder().id(1L).createdAt(tenDaysAgo).build();

        when(userService.findByUserNameOrElseThrow(userName)).thenReturn(userEntity);
        when(postRepository.findById(request.getPostId())).thenReturn(Optional.of(entity));

        // then
        AugustApplicationException exception = Assertions.assertThrows(AugustApplicationException.class, () ->
                postService.update(request, userName));
        Assertions.assertEquals(ErrorCode.POST_UPDATE_PERIOD_EXPIRED, exception.getErrorCode());
    }

    @Test
    void 포스트_수정시_생성_후_9일째되는_날이면_알람_메시지를_반환한다() {
        // given
        String userName = "sion1234";
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName(userName)
                .password("encodedPassword")
                .build();

        PostUpdateRequest request = PostUpdateRequest.builder()
                .postId(1L)
                .title("포스트 제목1")
                .content("포스트 내용")
                .build();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenDaysAgo = now.minus(9, ChronoUnit.DAYS); // 현재기준 9일 전을 생성일로 설정
        PostEntity entity = PostEntity.builder().id(1L).createdAt(tenDaysAgo).build();

        when(userService.findByUserNameOrElseThrow(userName)).thenReturn(userEntity);
        when(postRepository.findById(request.getPostId())).thenReturn(Optional.of(entity));

        // then
        String updatedMessage = postService.update(request, userName);
        assertThat(updatedMessage).isEqualTo("LAST_DAY_FOR_MODIFICATION");
    }

    @Test
    void 포스트_수정시_작성자와_로그인_유저가_다르면_에러발생() {
        // given
        String postWriter = "sion1234";
        String loggedInUser = "anotherLoginUser";

        UserEntity postWriterEntity = UserEntity.builder()
                .id(1L)
                .userName(postWriter)
                .password("encodedPassword")
                .build();

        PostUpdateRequest request = PostUpdateRequest.builder()
                .postId(1L)
                .title("modi title")
                .content("modi content")
                .build();

        PostEntity entity = PostEntity.builder()
                .id(1L)
                .title("title1")
                .content("contet1")
                .user(postWriterEntity)
                .build();

        // Mocking the postRepository to return the post entity when the post ID is 1L
        when(postRepository.findById(1L)).thenReturn(Optional.of(entity));

        // then
        AugustApplicationException exception = Assertions.assertThrows(AugustApplicationException.class, () ->
                postService.update(request, loggedInUser));
        Assertions.assertEquals(ErrorCode.INVALID_POST_WRITER, exception.getErrorCode());
    }
}


