package com.example.normalboard.dto;

import lombok.Data;

@Data
public class ArticleDto {
    private String title;
    private String content;
    private String hashtag;


    public static ArticleDto of(String title, String content, String hashtag) {
        return new ArticleDto(title,content,hashtag);
    }

    private ArticleDto(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }
}
