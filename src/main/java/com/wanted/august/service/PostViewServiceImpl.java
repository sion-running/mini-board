package com.wanted.august.service;

import com.wanted.august.model.entity.PostEntity;
import com.wanted.august.model.entity.PostViewEntity;
import com.wanted.august.model.entity.UserEntity;
import com.wanted.august.repository.PostViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostViewServiceImpl implements PostViewService {
    private final PostViewRepository postViewRepository;
    private final PostService postService;
    private final UserService userService;

    @Override
    @Transactional
    public PostViewEntity addCount(Long postId, String userName) {
        PostEntity postEntity = postService.findByPostIdOrElseThrow(postId);
        UserEntity userEntity = userService.findByUserNameOrElseThrow(userName);

        Optional<PostViewEntity> postView = postViewRepository.findByPostIdAndUserName(postId, userName);

        if (postView.isPresent()) {
            PostViewEntity entity = postView.get();
            entity.setUpdatedAt(LocalDateTime.now());
            return postViewRepository.save(entity);
        }

        PostViewEntity postViewEntity = PostViewEntity.builder()
                .postId(postId)
                .userName(userName)
                .build();

        return postViewRepository.save(postViewEntity);
    }
}
