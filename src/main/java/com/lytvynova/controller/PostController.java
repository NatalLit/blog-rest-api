package com.lytvynova.controller;

import com.lytvynova.entity.Post;
import com.lytvynova.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostServiceImpl postService;

    Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping
    public List<Post> findAllPosts() {
        List<Post> posts = postService.findAll();
        logger.info("posts {}", posts);

        return posts;
    }

    @PostMapping
    public Post addPost(@RequestBody Post post) {
        logger.info("add post {}", post);
        return postService.addPost(post);
    }

    @PutMapping("/{id}")
    public Post editPost(@PathVariable("id") Long id, @RequestBody Post post) {
        return postService.editPost(id, post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
    }


}
