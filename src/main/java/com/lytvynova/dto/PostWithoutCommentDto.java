package com.lytvynova.dto;

import lombok.Data;

@Data
public class PostWithoutCommentDto {
    private Long id;

    private String title;

    private String content;

    private boolean star;


}
