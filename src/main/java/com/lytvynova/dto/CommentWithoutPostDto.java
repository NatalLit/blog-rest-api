package com.lytvynova.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class CommentWithoutPostDto {

    private Long commentId;

    private String text;

    private LocalDate creationDate;
}
