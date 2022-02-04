package com.lytvynova.repository;

import com.lytvynova.entity.Comment;
import com.lytvynova.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository <Comment, Long> {
    List<Comment> findAllByPost(Post post);

    Comment findByCommentIdAndPost(Long commentId, Post post);

}
