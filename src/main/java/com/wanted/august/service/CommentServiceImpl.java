package com.wanted.august.service;

import com.wanted.august.exception.AugustApplicationException;
import com.wanted.august.exception.ErrorCode;
import com.wanted.august.model.Comment;
import com.wanted.august.model.entity.CommentEntity;
import com.wanted.august.model.entity.PostEntity;
import com.wanted.august.model.entity.UserEntity;
import com.wanted.august.model.request.CommentCreateRequest;
import com.wanted.august.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    @Override
    public Comment addComment(CommentCreateRequest request, String writerName) {
        // 존재하는 포스트인지 검증
        PostEntity postEntity = postService.findByPostIdOrElseThrow(request.getPostId());

        // 작성 유저 검증
        UserEntity commentWriter = userService.findByUserNameOrElseThrow(writerName);

        // 이미 댓글을 단 적이 있는 지 확인
        commentRepository.findByPostIdAndUserName(postEntity.getId(), commentWriter.getUserName()).ifPresent(it -> {
            throw new AugustApplicationException(ErrorCode.COMMENT_ALREADY_EXISTS);
        });

        // 댓글 등록
        CommentEntity entity = CommentEntity.toEntity(request, writerName);
        CommentEntity saved = commentRepository.save(entity);

        return Comment.fromEntity(saved);
    }
}
