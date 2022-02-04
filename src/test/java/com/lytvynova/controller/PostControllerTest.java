package com.lytvynova.controller;

import com.lytvynova.dto.PostWithoutCommentDto;
import com.lytvynova.entity.Comment;
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

import java.time.LocalDate;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
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

    private Post firstPost;

    private Post secondPost;

    private static final String BASE_URL = "/api/v1/posts/";

    @BeforeEach
    void setUp() {
        firstPost = Post.builder()
                .id(1L)
                .title("post_1")
                .content("hello from post number one")
                .star(false)
                .build();

        secondPost = Post.builder()
                .id(2L)
                .title("post_2")
                .content("hello from post number two")
                .star(true)
                .build();
    }

    @Test
    public void returnListOfPosts() throws Exception {
        Mockito.when(postService.findAll()).thenReturn(List.of(firstPost, secondPost));

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
        Mockito.when(postService.findAllPostsByTitle("post_1")).thenReturn(List.of(firstPost));

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

        Mockito.when(postService.sortAllPosts("title")).thenReturn(List.of(firstPost, secondPost));

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

        Mockito.when(postService.addPost(inputPost)).thenReturn(firstPost);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                                "  \"title\": \"post_1\",\n" +
                                "  \"content\": \"hello from post number one\"\n" +
                                "}\n"))
                .andExpect(status().isOk());
    }

    @Test
    public void editPostCorrectly() throws Exception {
        Long idToUpdate = 1L;
        Post postToUpdate = Post.builder()
                .title("updated title of post_1")
                .content("updated content for post number one")
                .build();

        Post updatedPost = Post.builder()
                .id(1L)
                .title("updated title of post_1")
                .content("updated content for post number one")
                .star(false)
                .build();

        Mockito.when(postService.editPost(idToUpdate, postToUpdate)).thenReturn(updatedPost);

        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"title\": \"updated title of post_1\",\n" +
                                "  \"content\": \"updated content for post number one\"\n" +
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

    @Test
    public void markPostAsTop() throws Exception {
        Post postWithStarTrue = Post.builder()
                .id(1L)
                .title("post_1")
                .content("hello from post number one")
                .star(true)
                .build();

        Mockito.when(postService.markTopPost(firstPost.getId())).thenReturn(postWithStarTrue);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/1/star")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(postService).markTopPost(1L);
    }

    @Test
    public void findAllTopPosts() throws Exception {

        List<Post> postsWithStarTrue = List.of(secondPost);

        Mockito.when(postService.findAllTopPosts()).thenReturn(postsWithStarTrue);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/star")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(postService).findAllTopPosts();
    }

    @Test
    public void deleteTopMark() throws Exception {
        Post postWithStarFalse = Post.builder()
                .id(2L)
                .title("post_2")
                .content("hello from post number two")
                .star(false)
                .build();

        Mockito.when(postService.deleteTopMark(secondPost.getId())).thenReturn(postWithStarFalse);

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/2/star")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(postService).deleteTopMark(2L);
    }





}