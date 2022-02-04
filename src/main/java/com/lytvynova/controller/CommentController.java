package com.lytvynova.controller;

import com.lytvynova.dto.CommentWithPostDto;
import com.lytvynova.dto.PostWithoutCommentDto;
import com.lytvynova.entity.Comment;
import com.lytvynova.entity.Post;
import com.lytvynova.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/{id}/comments")
    public void addComment(@PathVariable("id") Long id, @RequestBody Comment comment) {
        logger.info("add comment {}", comment);
        commentService.addComment(id, comment);
        logger.info("add comment {}", comment);

    }

    @GetMapping("/{id}/comments")
    public List<CommentWithPostDto> getCommentsByPostId(@PathVariable("id") Long id) {
        List<Comment> comments = commentService.getCommentsByPostId(id);
        logger.info("get comments {}", comments);
        return toCommentWithPostDtoList(comments);
    }

    @GetMapping("/{id}/comment/{commentId}")
    public CommentWithPostDto getCommentByCommentId(@PathVariable("id") Long id,
                                                    @PathVariable("commentId") Long commentId){
        Comment comment = commentService.getCommentByCommentId(id,commentId);
        logger.info("get comment {}", comment);
        return toCommentWithPostDto(comment);

    }


    private List<CommentWithPostDto> toCommentWithPostDtoList(List<Comment> comments) {

        List<CommentWithPostDto> commentWithoutCommentDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentWithoutCommentDtos.add(toCommentWithPostDto(comment));
        }

        return commentWithoutCommentDtos;
    }

    CommentWithPostDto toCommentWithPostDto(Comment comment) {
        CommentWithPostDto commentWithPostDto = new CommentWithPostDto();
        commentWithPostDto.setCommentId(comment.getCommentId());
        commentWithPostDto.setText(comment.getText());
        commentWithPostDto.setCreationDate(comment.getCreationDate());

        Post post = comment.getPost();
        PostWithoutCommentDto postWithoutCommentDto = new PostWithoutCommentDto();
        postWithoutCommentDto.setId(post.getId());
        postWithoutCommentDto.setTitle(post.getTitle());
        postWithoutCommentDto.setContent(post.getContent());
        postWithoutCommentDto.setStar(post.isStar());

        commentWithPostDto.setPostWithoutCommentDto(postWithoutCommentDto);

        return commentWithPostDto;
    }

}
