package com.lytvynova.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CommentWithPostDto {
    private Long commentId;

    private String text;

    private LocalDate creationDate;

    private PostWithoutCommentDto postWithoutCommentDto;
}
