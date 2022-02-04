package com.lytvynova.service;

import com.lytvynova.dto.CommentWithPostDto;
import com.lytvynova.dto.PostWithoutCommentDto;
import com.lytvynova.entity.Comment;
import com.lytvynova.entity.Post;
import com.lytvynova.repository.CommentRepository;
import com.lytvynova.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    @Override
    public Comment addComment(Long id, Comment comment) {
        Post post = postRepository.getById(id);
        comment.setPost(post);
        comment.setCreationDate(LocalDate.now());
        return commentRepository.save(comment);

    }

    @Override
    public List<Comment> getCommentsByPostId(Long id) {
        Post post = postRepository.getById(id);

        return  commentRepository.findAllByPost(post);
    }

    @Override
    public Comment getCommentByCommentId(Long id, Long commentId) {
        Post post = postRepository.getById(id);

        Comment comment = commentRepository.findByCommentIdAndPost(commentId, post);

        return comment;
    }


}
