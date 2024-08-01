package com.wanted.august.service;


import com.wanted.august.model.Post;
import com.wanted.august.model.entity.PostEntity;
import com.wanted.august.model.entity.UserEntity;
import com.wanted.august.model.request.PostCreateRequest;
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

}
