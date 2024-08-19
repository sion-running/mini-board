package com.wanted.august.service;


import com.wanted.august.exception.AugustApplicationException;
import com.wanted.august.exception.ErrorCode;
import com.wanted.august.model.Post;
import com.wanted.august.model.PostDetail;
import com.wanted.august.model.UserRole;
import com.wanted.august.model.entity.PostEntity;
import com.wanted.august.model.entity.UserEntity;
import com.wanted.august.model.request.PostCreateRequest;
import com.wanted.august.model.request.PostUpdateRequest;
import com.wanted.august.model.request.SearchRequest;
import com.wanted.august.model.response.PostDetailResponse;
import com.wanted.august.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private static final Integer LAST_ALLOWED_DAY_FOR_MODIFICATION = 9;

    @Override
    public Post create(PostCreateRequest request, String userName) {
        UserEntity userEntity = userService.findByUserNameOrElseThrow(userName);
        PostEntity postEntity = PostEntity.toEntity(request, userEntity);

        PostEntity saved = postRepository.save(postEntity);
        return Post.fromEntity(saved);
    }

    @Override
    public PostDetailResponse getPostDetail(Long postId) {
        PostEntity postEntity = findByPostIdOrElseThrow(postId);

        // 수정 가능일 계산
        long leftDays = LAST_ALLOWED_DAY_FOR_MODIFICATION - getDaysSincePostCreated(postEntity);
        PostDetailResponse response = PostDetailResponse.fromEntity(postEntity);
        response.setLeftDaysForModification(leftDays);

        return response;
    }

    @Override
    public Page<PostDetail> search(SearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPageStart(), request.getPageSize(), getSortDetail(request)); // TODO sort type 문자열 리팩토링
        String title = request.getTitle();

        if (title == null || title.isBlank()) {
            return postRepository.findPostDetailsWithViewCount(pageable);
        }

        return postRepository.findPostDetailsWithViewCountByTitle(pageable, title);
    }

    @Override
    public String update(PostUpdateRequest request, String loginUserName) {
        UserEntity userEntity = userService.findByUserNameOrElseThrow(loginUserName);
        PostEntity postEntity = findByPostIdOrElseThrow(request.getPostId());
        long dayDiff = getDaysSincePostCreated(postEntity);

        if (dayDiff > LAST_ALLOWED_DAY_FOR_MODIFICATION) {
            throw new AugustApplicationException(ErrorCode.POST_UPDATE_PERIOD_EXPIRED);
        }

        if (dayDiff == LAST_ALLOWED_DAY_FOR_MODIFICATION) {
            return "LAST_DAY_FOR_MODIFICATION";
        }

        if (!hasPermissionForModification(postEntity, userEntity)) {
            throw new AugustApplicationException(ErrorCode.NO_PERMISSION_FOR_THE_POST);
        }

        postEntity.setTitle(request.getTitle());
        postEntity.setContent(request.getContent());
        postRepository.save(postEntity);

        return "MODIFIED";
    }

    @Override
    public void delete(Long postId, Boolean isSoftDelete, String loginUserName) {
        UserEntity userEntity = userService.findByUserNameOrElseThrow(loginUserName);
        PostEntity postEntity = findByPostIdOrElseThrow(postId);

        if (!hasPermissionForModification(postEntity, userEntity)) {
            throw new AugustApplicationException(ErrorCode.NO_PERMISSION_FOR_THE_POST);
        }

        if (isSoftDelete) {
            postEntity.setDeletedAt(LocalDateTime.now());
            postRepository.save(postEntity);
            return;
        }

        postRepository.delete(postEntity);
    }

    public PostEntity findByPostIdOrElseThrow(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new AugustApplicationException(ErrorCode.POST_NOT_FOUND));
    }

    /**
     * 포스트에 대한 변경(수정, 삭제) 권한을 체크하는 메소드
     * 현재는 관리자가 수정과 삭제 모두 가능하나 추후 변경되면 분리
     */
    private Boolean hasPermissionForModification(PostEntity postEntity, UserEntity loginUser) {
        if (loginUser.getRole().equals(UserRole.ADMIN)) {
            return true; // 관리자는 모든 게시물 수정 및 삭제 가능
        }

        if (postEntity.getUser().getUserName().equals(loginUser.getUserName())) {
            return true; // 작성자 본인만 수정 및 삭제 가능
        }

        return false;
    };

    private long getDaysSincePostCreated(PostEntity entity) {
        LocalDateTime createdAt = entity.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.DAYS.between(createdAt, now);
    }

    private Sort getSortDetail(SearchRequest request) {
        String sortDirection = request.getDirection();
        if (sortDirection.equals("ASC")) {
            return Sort.by("createdAt").ascending();
        }

        return Sort.by("createdAt").descending();
    }
}
