package com.lytvynova.controller;

import com.lytvynova.entity.Post;
import com.lytvynova.service.PostService;
import org.hamcrest.Matchers;
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

import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    private Post post;

    private Post oneMorePost = new Post(2L, "post_2", "hello from post number two");

    private static final String BASE_URL = "/api/v1/posts/";

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .id(1L)
                .title("post_1")
                .content("hello from post number one")
                .build();

    }

    @Test
    public void returnListOfPosts() throws Exception {
        Mockito.when(postService.findAll()).thenReturn(List.of(post, oneMorePost));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("post_2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("post_1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].content").value("hello from post number two"));

        verify(postService).findAll();
    }

    @Test
    public void returnAllPostsWithProvidedTitle() throws Exception {
        Mockito.when(postService.findAllPostsByTitle("post_1")).thenReturn(List.of(post));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "?title=post_1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("post_1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("hello from post number one"));

        verify(postService).findAllPostsByTitle("post_1");

    }

    @Test
    public void sortPostsByTitle() throws Exception {

        Mockito.when(postService.sortAllPosts("title")).thenReturn(List.of(post, oneMorePost));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "?sort=title")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("post_1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].content").value("hello from post number two"));

        verify(postService).sortAllPosts("title");
    }

    @Test
    public void addPostCorrectly() throws Exception {
        Post inputPost = Post.builder()
                .title("post_1")
                .content("hello from post number one")
                .build();

        Mockito.when(postService.addPost(inputPost)).thenReturn(post);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                                "  \"title\": \"post_1\",\n" +
                                "  \"content\": \"hello from post number one\"\n" +
                                "}\n"))
                .andExpect(status().isOk());
    }

    @Test
    public void editPostCorrectly() throws Exception {
        Long idToUpdate = 2L;
        Post postToUpdate = new Post(2L, "updated title of post_2", "updated hello from post number two");
        Mockito.when(postService.editPost(idToUpdate, postToUpdate)).thenReturn(postToUpdate);

        mockMvc.perform(put(BASE_URL + "/2")
                        .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                                "  \"title\": \"updated title of post_2\",\n" +
                                "  \"content\": \"updated hello from post number two\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deletePostsById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(postService).deletePost(1L);
    }

}