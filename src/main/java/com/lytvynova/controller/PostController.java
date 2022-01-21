package com.lytvynova.controller;

import com.lytvynova.entity.Post;
import com.lytvynova.service.PostService;
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
    private final PostService postService;

    Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping
    public List<Post> findAllPosts() {
        List<Post> posts = postService.findAll();
        logger.info("posts {}", posts);

        return posts;
    }

    @GetMapping(params = {"title"})
    public List<Post> findAllPostsByTitle(@RequestParam(value = "title", required = false) String title) {
        List<Post> postsWithTitle = postService.findAllPostsByTitle(title);
        logger.info("posts by title {}", postsWithTitle);

        return postsWithTitle;
    }

    @GetMapping(params = {"sort"})
    public List<Post> sortAllPosts(@RequestParam(value = "sort", required = false) String sortParameter) {
        List<Post> sortedPosts = postService.sortAllPosts(sortParameter);
        logger.info("sorted posts by title {}", sortedPosts);

        return sortedPosts;
    }

    @PostMapping
    public Post addPost(@RequestBody Post post) {
        logger.info("add post {}", post);
        return postService.addPost(post);
    }

    @PutMapping("/{id}")
    public Post editPost(@PathVariable("id") Long id, @RequestBody Post post) {
        logger.info("edit post {}", post);
        return postService.editPost(id, post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable("id") Long id) {
        logger.info("delete post by id {}", id);
        postService.deletePost(id);
    }

}
