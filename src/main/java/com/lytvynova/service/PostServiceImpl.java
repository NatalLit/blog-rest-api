package com.lytvynova.service;

import com.lytvynova.dto.PostWithoutCommentDto;
import com.lytvynova.entity.Post;
import com.lytvynova.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        List<Post> posts = postRepository.findAllByTitleIgnoreCase(title);

        return posts;
    }

    @Override
    public List<Post> findAllTopPosts() {
        List<Post> posts = postRepository.findByStarTrue();

        return posts;
    }


    @Override
    public Post addPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post editPost(Long id, Post post) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            throw new IllegalArgumentException("there is no post with provided id");
        }
        Post postToUpdate = postOptional.get();
        postToUpdate.setTitle(post.getTitle());
        postToUpdate.setContent(post.getContent());


        return postRepository.save(postToUpdate);
    }

    @Override
    public Post markTopPost(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            throw new IllegalArgumentException("there is no post with provided id");
        }
        Post postToMarkAsTop = postOptional.get();
        postToMarkAsTop.setStar(true);
       return postRepository.save(postToMarkAsTop);

    }

    @Override
    public Post deleteTopMark(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            throw new IllegalArgumentException("there is no post with provided id");
        }
        Post postToDeleteMark = postOptional.get();
        postToDeleteMark.setStar(false);

        return postRepository.save(postToDeleteMark);
    }

    @Override
    public Post getPostWithComments(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            throw new IllegalArgumentException("there is no post with provided id");
        }
        return postOptional.get();

    }


    @Override
    public void deletePost(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            throw new IllegalArgumentException("there is no post with provided id");
        }
        postRepository.deleteById(id);
    }

    @Override
    public List<Post> sortAllPosts(String sortParameter) {
        return postRepository.findAll(Sort.by(Sort.Direction.ASC, sortParameter));
    }

    private PostWithoutCommentDto toPostWithoutCommentDto(Post post) {
        PostWithoutCommentDto postWithoutCommentDto = new PostWithoutCommentDto();

        postWithoutCommentDto.setId(post.getId());
        postWithoutCommentDto.setTitle(post.getTitle());
        postWithoutCommentDto.setContent(post.getContent());
        postWithoutCommentDto.setStar(post.isStar());

        return postWithoutCommentDto;

    }
}
