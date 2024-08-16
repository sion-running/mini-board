package com.wanted.august.service;

import com.wanted.august.exception.AugustApplicationException;
import com.wanted.august.exception.ErrorCode;
import com.wanted.august.model.Comment;
import com.wanted.august.model.User;
import com.wanted.august.model.UserRole;
import com.wanted.august.model.entity.CommentEntity;
import com.wanted.august.model.entity.PostEntity;
import com.wanted.august.model.entity.UserEntity;
import com.wanted.august.model.request.CommentCreateRequest;
import com.wanted.august.model.request.CommentUpdateRequest;
import com.wanted.august.model.request.UserJoinRequest;
import com.wanted.august.model.request.UserLoginRequest;
import com.wanted.august.repository.CommentRepository;
import com.wanted.august.repository.UserRepository;
import com.wanted.august.utils.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    CommentServiceImpl commentService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    UserServiceImpl userService;

    @Mock
    PostServiceImpl postService;

    @Test
    void 댓글작성시_유저가_해당_포스트에_이미_댓글을_단_적이_있다면_예외를_발생시킨다() {
        // given
        String writerName = "sion1234";
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName(writerName)
                .build();

        CommentCreateRequest request = CommentCreateRequest.builder()
                .postId(1L)
                .content("new comment")
                .build();

        PostEntity postEntity = PostEntity.builder().id(1L).build();

        when(userService.findByUserNameOrElseThrow(writerName)).thenReturn(userEntity);
        when(postService.findByPostIdOrElseThrow(postEntity.getId())).thenReturn(postEntity);
        when(commentRepository.findByPostIdAndUserName(1L, writerName)).thenReturn(Optional.of(new CommentEntity()));

        // then
        AugustApplicationException exception = Assertions.assertThrows(AugustApplicationException.class, () ->
                commentService.addComment(request, writerName));
        Assertions.assertEquals(ErrorCode.COMMENT_ALREADY_EXISTS, exception.getErrorCode());
    }

    @Test
    void 댓글_등록_성공() {
        // given
        String writerName = "sion1234";
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName(writerName)
                .build();

        PostEntity postEntity = PostEntity.builder().id(1L).build();

        CommentCreateRequest request = CommentCreateRequest.builder()
                .postId(1L)
                .content("new comment")
                .build();
        CommentEntity commentEntity = CommentEntity.toEntity(request, writerName);

        when(userService.findByUserNameOrElseThrow(writerName)).thenReturn(userEntity);
        when(postService.findByPostIdOrElseThrow(postEntity.getId())).thenReturn(postEntity);
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);

        Comment actual = commentService.addComment(request, writerName);
        assertThat(actual.getId()).isEqualTo(commentEntity.getId());
        assertThat(actual.getContent()).isEqualTo(request.getContent());
    }

    @Test
    void 댓글_수정시_댓글이_존재하지_않으면_예외를_발생시킨다() {
        // given
        String writerName = "sion1234";
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName(writerName)
                .build();

        PostEntity postEntity = PostEntity.builder().id(1L).build();

        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .postId(1L)
                .commentId(200L)
                .content("댓글 달래요")
                .build();

        when(commentRepository.findById(200L)).thenReturn(Optional.empty());
        AugustApplicationException exception = Assertions.assertThrows(AugustApplicationException.class, () ->
                commentService.update(request, writerName));
        Assertions.assertEquals(ErrorCode.COMMENT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 댓글_수정시_작성자가_본인이_아니면_예외를_발생시킨다() {
        // given
        String writerName = "sion1234";
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName(writerName)
                .build();

        PostEntity postEntity = PostEntity.builder().id(1L).build();

        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .postId(1L)
                .commentId(1L)
                .content("댓글 달래요")
                .build();

        CommentEntity commentEntity = CommentEntity.builder().postId(1L).userName("diffName").build();

        when(commentRepository.findById(1L)).thenReturn(Optional.of(commentEntity));

        AugustApplicationException exception = Assertions.assertThrows(AugustApplicationException.class, () ->
                commentService.update(request, writerName));
        Assertions.assertEquals(ErrorCode.NO_PERMISSION_FOR_THE_COMMENT, exception.getErrorCode());
    }

    @Test
    void 댓글_수정_성공() {
        // given
        String writerName = "sion1234";
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName(writerName)
                .build();

        PostEntity postEntity = PostEntity.builder().id(1L).build();

        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .postId(1L)
                .commentId(1L)
                .content("수정된 댓글")
                .build();

        CommentEntity commentEntity = CommentEntity.builder().id(1L).postId(1L).userName(writerName).content("이전 댓글").build();

        when(commentRepository.findById(1L)).thenReturn(Optional.of(commentEntity));
        when(commentRepository.save(commentEntity)).thenReturn(commentEntity);

        Comment updated = commentService.update(request, writerName);
        assertThat(updated.getContent()).isEqualTo(request.getContent());
        assertThat(updated.getId()).isEqualTo(request.getCommentId());
    }
}
