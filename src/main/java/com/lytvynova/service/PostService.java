package com.lytvynova.service;

import com.lytvynova.dto.PostWithoutCommentDto;
import com.lytvynova.entity.Post;

import java.util.List;

public interface PostService {

    public List<Post> findAll();

    public List<Post> findAllPostsByTitle(String title);

    public Post addPost(Post post);

    public Post editPost(Long id, Post post);

    public void deletePost(Long id);

    public List<Post> sortAllPosts(String sortParameter);

   public List<Post> findAllTopPosts();

    public Post markTopPost(Long id);

    public Post deleteTopMark(Long id);

    public Post getPostWithComments(Long id);


}
