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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public String update(PostUpdateRequest request, String userName) {
        UserEntity userEntity = userService.findByUserNameOrElseThrow(userName);
        PostEntity postEntity = postRepository.findById(request.getPostId()).orElseThrow(() -> new AugustApplicationException(ErrorCode.POST_NOT_FOUND));
        long dayDiff = getDaysSincePostCreated(postEntity);

        if (dayDiff > LAST_ALLOWED_DAY_FOR_MODIFICATION) {
            throw new AugustApplicationException(ErrorCode.POST_UPDATE_PERIOD_EXPIRED);
        }

        if (dayDiff == LAST_ALLOWED_DAY_FOR_MODIFICATION) {
            return "LAST_DAY_FOR_MODIFICATION";
        }

        if (!postEntity.getUser().getUserName().equals(userName)) {
            throw new AugustApplicationException(ErrorCode.INVALID_POST_WRITER); // 작성자가 아니라면 수정불가
        }

        postEntity.setTitle(request.getTitle());
        postEntity.setContent(request.getContent());
        postRepository.save(postEntity);

        return "MODIFIED";
    }

    @Override
    public List<Post> searchList(SearchRequest searchRequest) {
        if (Objects.isNull(searchRequest.getKeyword()) || searchRequest.getKeyword().isEmpty()) {
            return findAllByOrderCreatedAt(searchRequest.getDirection());
        }

        return findByTitleContaining(searchRequest.getKeyword());
    }

    private List<Post> findAllByOrderCreatedAt(String direction) {
        if (direction.equals("ASC")) {
            return postRepository.findAllByOrderByCreatedAtAsc().stream().map(Post::fromEntity).collect(Collectors.toList());
        }

        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(Post::fromEntity).collect(Collectors.toList());
    }

    private List<Post> findByTitleContaining(String keyword) {
        List<PostEntity> list = postRepository.findByTitleContaining(keyword);
        return list.stream().map(Post::fromEntity).collect(Collectors.toList());
    }


    private long getDaysSincePostCreated(PostEntity entity) {
        LocalDateTime createdAt = entity.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.DAYS.between(createdAt, now);
    }
}
