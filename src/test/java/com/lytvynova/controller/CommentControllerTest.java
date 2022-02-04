package com.lytvynova.controller;

import com.lytvynova.entity.Comment;
import com.lytvynova.entity.Post;
import com.lytvynova.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    private Post post;

    Comment firstCommentForFirstPost;

    Comment secondCommentForFirstPost;

    private static final String BASE_URL = "/api/v1/posts/";

    @BeforeEach
    void setUp() {
        firstCommentForFirstPost = Comment.builder()
                .commentId(1L)
                .text("first comment for first post")
                .creationDate(LocalDate.now())
                .build();

        post = Post.builder()
                .id(1L)
                .title("post_1")
                .content("hello from post number one")
                .star(false)
                .comments(List.of(firstCommentForFirstPost))
                .build();
    }

    @Test
    public void addCommentToPost() throws Exception {
        secondCommentForFirstPost = Comment.builder()
                .commentId(2L)
                .text("second comment for first post")
                .creationDate(LocalDate.now())
                .post(post)
                .build();

        Comment inputComment = new Comment(null, "second comment for first post", null, null);

        Mockito.when(commentService.addComment(post.getId(), inputComment)).thenReturn(secondCommentForFirstPost);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/1/comments")
                        .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                                "  \"text\": \"comment for first post\"\n" + "}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllCommentsFromPostByPostId() throws Exception {
        Mockito.when(commentService.getCommentsByPostId(post.getId())).thenReturn(List.of(firstCommentForFirstPost));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1/comments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(commentService).getCommentsByPostId(1L);
    }

    @Test
    public void getCommentByPostIdAndCommentId() throws Exception {
        Mockito.when(commentService.getCommentByCommentId(post.getId(),firstCommentForFirstPost.getCommentId())).thenReturn(firstCommentForFirstPost);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1/comment/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(commentService).getCommentByCommentId(1L,1L);
    }


}