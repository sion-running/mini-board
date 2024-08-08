package com.wanted.august.service;

import com.wanted.august.exception.AugustApplicationException;
import com.wanted.august.exception.ErrorCode;
import com.wanted.august.model.Post;
import com.wanted.august.model.entity.PostEntity;
import com.wanted.august.model.entity.UserEntity;
import com.wanted.august.model.request.PostCreateRequest;
import com.wanted.august.model.request.PostUpdateRequest;
import com.wanted.august.model.request.SearchRequest;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
    void 포스트_조회시_title로_검색이_가능하다() {
        // given
        SearchRequest searchRequest = SearchRequest.builder().keyword("오후").build();

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName("user1234")
                .password("encodedPassword")
                .build();

        PostEntity post1 = PostEntity.builder()
                .id(1L)
                .title("오전의 포스트")
                .content("content")
                .user(userEntity)
                .createdAt(LocalDateTime.now().minusDays(2))
                .build();

        PostEntity post2 = PostEntity.builder()
                .id(2L)
                .title("오후의 포스트1")
                .content("content1")
                .user(userEntity)
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        PostEntity post3 = PostEntity.builder()
                .id(3L)
                .title("오후의 포스트2")
                .content("content2")
                .user(userEntity)
                .createdAt(LocalDateTime.now())
                .build();

        List<PostEntity> posts = Arrays.asList(post2, post3);

        // when
        when(postRepository.findByTitleContaining(searchRequest.getKeyword())).thenReturn(posts);
        List<Post> postList = postService.searchList(searchRequest);

        // then
        assertThat(postList).hasSize(2);
        assertThat(postList).extracting("title").doesNotContain("오전의 포스트");
    }

    @Test
    void 포스트_조회시_검색어가_없다면_생성일_기준_내림차순_정렬해서_보여준다() {
        // given
        SearchRequest searchRequest = SearchRequest.builder().build();

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName("user1234")
                .password("encodedPassword")
                .build();

        PostEntity post1 = PostEntity.builder()
                .id(1L)
                .title("title1")
                .content("content1")
                .user(userEntity)
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        PostEntity post2 = PostEntity.builder()
                .id(2L)
                .title("title2")
                .content("content2")
                .user(userEntity)
                .createdAt(LocalDateTime.now())
                .build();

        List<PostEntity> posts = Arrays.asList(post2, post1); // Post2가 최신, Post1이 이전 게시글
        when(postRepository.findAllByOrderByCreatedAtDesc()).thenReturn(posts);

        // when
        List<Post> postList = postService.searchList(searchRequest);

        // then
        assertThat(postList).hasSize(2);
        assertThat(postList.get(0).getId()).isEqualTo(post2.getId()); // 가장 최신 게시글
        assertThat(postList.get(1).getId()).isEqualTo(post1.getId()); // 그 다음 게시글
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
                .title("modified title")
                .content("modified content")
                .build();

        PostEntity entity = PostEntity.builder()
                .id(1L)
                .title("original post title")
                .content("original post content")
                .user(userEntity)
                .createdAt(LocalDateTime.now().minus(1, ChronoUnit.DAYS))
                .build();

        when(userService.findByUserNameOrElseThrow(userName)).thenReturn(userEntity);
        when(postRepository.findById(request.getPostId())).thenReturn(Optional.of(entity));

        postService.update(request, userName);

        assertThat(entity.getTitle()).isEqualTo("modified title");
        assertThat(entity.getContent()).isEqualTo("modified content");
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
    void 포스트_삭제시_포스트가_존재하지_않으면_예외를_발생시킨다() {
        // given
        String userName = "sion1234";
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName(userName)
                .password("encodedPassword")
                .build();

        PostEntity entity = PostEntity.builder()
                .id(1L)
                .title("title1")
                .content("contet1")
                .user(userEntity)
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        AugustApplicationException exception = Assertions.assertThrows(AugustApplicationException.class, () ->
                postService.delete(1L, true, userName));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 포스트_수정시_작성자와_로그인_유저가_다르면_에러발생() {
        // given
        String postWriter = "sion1234";
        String loggedInUser = "anotherLoginUser";

        UserEntity loggedInUserEntity = UserEntity.builder()
                .id(1L)
                .userName(loggedInUser)
                .password("encodedPassword")
                .build();

        UserEntity postWriterEntity = UserEntity.builder()
                .id(2L)
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
                .createdAt(LocalDateTime.now().minus(1, ChronoUnit.DAYS))
                .build();

        // Mocking the postRepository to return the post entity when the post ID is 1L
        when(userService.findByUserNameOrElseThrow(loggedInUser)).thenReturn(loggedInUserEntity);
        when(postRepository.findById(1L)).thenReturn(Optional.of(entity));

        // then
        AugustApplicationException exception = Assertions.assertThrows(AugustApplicationException.class, () ->
                postService.update(request, loggedInUser));
        Assertions.assertEquals(ErrorCode.NO_PERMISSION_FOR_THE_POST, exception.getErrorCode());
    }

    @Test
    void 포스트_삭제시_현재_로그인_유저가_해당_포스트의_작성자가_아니면_예외를_발생시킨다() {
        // given
        String postWriter = "sion1234";
        String loggedInUser = "anotherLoginUser";

        UserEntity loggedInUserEntity = UserEntity.builder()
                .id(1L)
                .userName(loggedInUser)
                .password("encodedPassword")
                .build();

        UserEntity postWriterEntity = UserEntity.builder()
                .id(2L)
                .userName(postWriter)
                .password("encodedPassword")
                .build();

        PostEntity entity = PostEntity.builder()
                .id(1L)
                .title("title1")
                .content("contet1")
                .user(postWriterEntity)
                .createdAt(LocalDateTime.now().minus(1, ChronoUnit.DAYS))
                .build();

        when(userService.findByUserNameOrElseThrow(loggedInUser)).thenReturn(loggedInUserEntity);
        when(postRepository.findById(1L)).thenReturn(Optional.of(entity));

        // then
        AugustApplicationException exception = Assertions.assertThrows(AugustApplicationException.class, () ->
                postService.delete(1L, true, loggedInUser));
        Assertions.assertEquals(ErrorCode.NO_PERMISSION_FOR_THE_POST, exception.getErrorCode());
    }

    @Test
    void 포스트_논리삭제_성공() {
        String userName= "user1234";
        UserEntity postWriterEntity = UserEntity.builder()
                .id(1L)
                .userName(userName)
                .password("encodedPassword")
                .build();

        PostEntity entity = PostEntity.builder()
                .id(1L)
                .title("title1")
                .content("contet1")
                .user(postWriterEntity)
                .createdAt(LocalDateTime.now().minus(1, ChronoUnit.DAYS))
                .build();

        when(userService.findByUserNameOrElseThrow(userName)).thenReturn(postWriterEntity);
        when(postRepository.findById(1L)).thenReturn(Optional.of(entity));

        postService.delete(entity.getId(), true, userName);

        assertThat(entity.getDeletedAt()).isNotNull();
    }

    @Test
    void 포스트_물리삭제_성공() {
        // given
        String userName = "user1234";
        UserEntity postWriterEntity = UserEntity.builder()
                .id(1L)
                .userName(userName)
                .password("encodedPassword")
                .build();

        PostEntity entity = PostEntity.builder()
                .id(1L)
                .title("title1")
                .content("contet1")
                .user(postWriterEntity)
                .createdAt(LocalDateTime.now().minus(1, ChronoUnit.DAYS))
                .build();

        when(userService.findByUserNameOrElseThrow(userName)).thenReturn(postWriterEntity);
        when(postRepository.findById(1L)).thenReturn(Optional.of(entity));

        // when
        postService.delete(entity.getId(), false, userName);
        verify(postRepository, times(1)).delete(entity);
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        Optional<PostEntity> deletedEntity = postRepository.findById(1L);
        assertThat(deletedEntity).isEmpty();
    }
}


