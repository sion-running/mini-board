package com.wanted.august.service;


import com.wanted.august.exception.AugustApplicationException;
import com.wanted.august.exception.ErrorCode;
import com.wanted.august.model.Post;
import com.wanted.august.model.entity.PostEntity;
import com.wanted.august.model.entity.UserEntity;
import com.wanted.august.model.request.PostCreateRequest;
import com.wanted.august.model.request.PostUpdateRequest;
import com.wanted.august.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    @Override
    public Post create(PostCreateRequest request, String userName) {
        UserEntity userEntity = userService.findByUserNameOrElseThrow(userName);
        PostEntity postEntity = PostEntity.toEntity(request, userEntity);

        PostEntity saved = postRepository.save(postEntity);
        return Post.fromEntity(saved);
    }

    @Override
    public void update(PostUpdateRequest request, String userName) {
        UserEntity userEntity = userService.findByUserNameOrElseThrow(userName);
        PostEntity postEntity = postRepository.findById(request.getPostId()).orElseThrow(() -> new AugustApplicationException(ErrorCode.POST_NOT_FOUND));

        if (!postEntity.getUser().getUserName().equals(userName)) {
            throw new AugustApplicationException(ErrorCode.INVALID_POST_WRITER); // 작성자가 아니라면 수정불가
        }

        postEntity.setTitle(request.getTitle());
        postEntity.setContent(request.getContent());
        postRepository.save(postEntity);
    }
}
