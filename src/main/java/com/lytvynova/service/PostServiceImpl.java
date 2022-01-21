package com.lytvynova.service;

import com.lytvynova.entity.Post;
import com.lytvynova.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findAllPostsByTitle(String title) {
        return postRepository.findAllByTitleIgnoreCase(title);
    }

    @Override
    public Post addPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post editPost(Long id, Post post) {
        Post postToUpdate = postRepository.findById(id).get();

        if (Objects.nonNull(post.getTitle()) &&
                !"".equalsIgnoreCase(post.getTitle())) {
            postToUpdate.setTitle(post.getTitle());
        }
        if (Objects.nonNull(post.getContent()) &&
                !"".equalsIgnoreCase(post.getContent())) {
            postToUpdate.setContent(post.getContent());
        }
        return postRepository.save(postToUpdate);
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public List<Post> sortAllPosts(String sortParameter) {
        return postRepository.findAll(Sort.by(Sort.Direction.ASC, sortParameter));
    }


}
