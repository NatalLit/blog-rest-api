package com.lytvynova.controller;

import com.lytvynova.dto.CommentWithoutPostDto;
import com.lytvynova.dto.PostWithCommentDto;
import com.lytvynova.dto.PostWithoutCommentDto;
import com.lytvynova.entity.Comment;
import com.lytvynova.entity.Post;
import com.lytvynova.service.PostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping
    public List<PostWithoutCommentDto> findAllPosts() {
        List<Post> posts = postService.findAll();
        logger.info("posts {}", posts);

        return toPostWithoutCommentDtoList(posts);
    }

    @GetMapping(params = {"title"})
    public List<PostWithoutCommentDto> findAllPostsByTitle(@RequestParam(value = "title", required = false) String title) {
        List<Post> postsWithTitle = postService.findAllPostsByTitle(title);
        logger.info("posts by title {}", postsWithTitle);

        return toPostWithoutCommentDtoList(postsWithTitle);
    }

    @GetMapping(params = {"sort"})
    public List<PostWithoutCommentDto> sortAllPosts(@RequestParam(value = "sort", required = false) String sortParameter) {
        List<Post> sortedPosts = postService.sortAllPosts(sortParameter);
        logger.info("sorted posts by title {}", sortedPosts);

        return toPostWithoutCommentDtoList(sortedPosts);
    }

    @GetMapping("/star")
    public List<PostWithoutCommentDto> findAllTopPosts() {
        List<Post> topPosts = postService.findAllTopPosts();
        logger.info("top posts  {}", topPosts);

        return toPostWithoutCommentDtoList(topPosts);
    }

    //GET /api/v1/posts/1/full - возвращает JSON поста с ид = 1, и всеми вложенными комментариями в него
    @GetMapping("/{id}/full")
    public PostWithCommentDto getPostWithComments(@PathVariable("id") Long id) {
        Post postWithComments = postService.getPostWithComments(id);
        logger.info("post with comments {}", postWithComments);

        return toPostWithCommentDto(postWithComments);
    }

    @PostMapping
    public void addPost(@RequestBody Post post) {
        logger.info("add post {}", post);
        postService.addPost(post);
    }

    @PutMapping("/{id}")
    public void editPost(@PathVariable("id") Long id, @RequestBody Post post) {
        logger.info("post to update: {}", post);
        Post updatedPost = postService.editPost(id, post);
        logger.info("updated post: {}", updatedPost);

    }

    @PutMapping("/{id}/star")
    public void markTopPost(@PathVariable("id") Long id) {
        logger.info("mark post as top by id {}", id);
        postService.markTopPost(id);
    }


    @DeleteMapping("/{id}/star")
    public void deleteTopMark(@PathVariable("id") Long id) {
        logger.info("delete top mark by id {}", id);
        postService.deleteTopMark(id);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable("id") Long id) {
        logger.info("delete post by id {}", id);
        postService.deletePost(id);
    }

    private List<PostWithoutCommentDto> toPostWithoutCommentDtoList(List<Post> posts) {

        List<PostWithoutCommentDto> postWithoutCommentDtos = new ArrayList<>();
        for (Post post : posts) {
            postWithoutCommentDtos.add(toPostWithoutCommentDto(post));
        }

        return postWithoutCommentDtos;
    }


    private PostWithoutCommentDto toPostWithoutCommentDto(Post post) {
        PostWithoutCommentDto postWithoutCommentDto = new PostWithoutCommentDto();

        postWithoutCommentDto.setId(post.getId());
        postWithoutCommentDto.setTitle(post.getTitle());
        postWithoutCommentDto.setContent(post.getContent());
        postWithoutCommentDto.setStar(post.isStar());

        return postWithoutCommentDto;
    }

    private PostWithCommentDto toPostWithCommentDto(Post post) {
        PostWithCommentDto postWithCommentDto = new PostWithCommentDto();
        postWithCommentDto.setId(post.getId());
        postWithCommentDto.setTitle(post.getTitle());
        postWithCommentDto.setContent(post.getContent());
        postWithCommentDto.setStar(post.isStar());

        List<Comment> comments = post.getComments();
        List<CommentWithoutPostDto> commentWithoutPostDtos = new ArrayList<>();
        for (Comment comment : comments) {
            CommentWithoutPostDto commentWithoutPostDto = new CommentWithoutPostDto();
            commentWithoutPostDto.setCommentId(comment.getCommentId());
            commentWithoutPostDto.setText(comment.getText());
            commentWithoutPostDto.setCreationDate(comment.getCreationDate());

            commentWithoutPostDtos.add(commentWithoutPostDto);
        }
        postWithCommentDto.setCommentWithoutPostDtos(commentWithoutPostDtos);

        return postWithCommentDto;
    }

}
