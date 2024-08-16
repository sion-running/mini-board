package com.wanted.august.service;

import com.wanted.august.model.entity.PostEntity;
import com.wanted.august.model.entity.PostViewEntity;
import com.wanted.august.model.entity.UserEntity;
import com.wanted.august.repository.PostViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostViewServiceImpl implements PostViewService {
    private final PostViewRepository postViewRepository;
    private final PostService postService;
    private final UserService userService;

    @Override
    public PostViewEntity addCount(Long postId, String userName) {
        PostEntity postEntity = postService.findByPostIdOrElseThrow(postId);
        UserEntity userEntity = userService.findByUserNameOrElseThrow(userName);

        Optional<PostViewEntity> postView = postViewRepository.findByPostIdAndUserName(postId, userName);

        // TODO
        //  Q. 동시성에 대한 고려는 어떻게 할까?
        // 1. 처음 조회하는 경우 동시에 클릭한다면 여러개가 insert될 가능성은 없는지?
        // 2. 아니면 막아줄 처리가 필요한지? (디비에 postId와 userName을 묶어서 키로 설정?)
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
