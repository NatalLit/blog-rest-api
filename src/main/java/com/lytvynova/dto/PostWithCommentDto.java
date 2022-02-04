package com.lytvynova.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostWithCommentDto {

    private Long id;

    private String title;

    private String content;

    private boolean star;

    private List<CommentWithoutPostDto> commentWithoutPostDtos;


}
