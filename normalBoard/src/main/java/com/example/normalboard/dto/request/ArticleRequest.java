package com.example.normalboard.dto.request;

import com.example.normalboard.dto.ArticleDto;
import com.example.normalboard.dto.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleRequest {
    private String title;
    private String content;
    private String hashtag;

    public static ArticleRequest of(String title, String content, String hashtag) {
        return new ArticleRequest(title, content, hashtag);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto) {
        return ArticleDto.of(
                userAccountDto,
                title,
                content,
                hashtag
        );
    }
}

