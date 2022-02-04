package com.lytvynova.service;

import com.lytvynova.entity.Comment;

import java.util.List;

public interface CommentService {

   public Comment addComment(Long id, Comment comment);

   List<Comment> getCommentsByPostId(Long id);

   Comment getCommentByCommentId(Long id, Long commentId);

}
