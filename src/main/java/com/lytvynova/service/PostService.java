package com.lytvynova.service;

import com.lytvynova.entity.Post;

import java.util.List;

public interface PostService {

    public List<Post> findAll();

    public Post addPost (Post post);

   public Post editPost(Long id, Post post);

    public void deletePost(Long id);

}
