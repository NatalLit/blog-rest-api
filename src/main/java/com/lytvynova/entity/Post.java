package com.lytvynova.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String content;

}
