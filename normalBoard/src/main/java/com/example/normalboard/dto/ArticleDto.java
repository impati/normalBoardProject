package com.example.normalboard.dto;

import com.example.normalboard.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ArticleDto {

    private Long id;
    private UserAccountDto userAccountDto;
    private String title;
    private String content;
    private String hashtag;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    public static ArticleDto of(Long id, UserAccountDto userAccountDto, String title, String content, String hashtag, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new ArticleDto(id, userAccountDto, title, content, hashtag, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static ArticleDto from(Article entity) {
            return new ArticleDto(
                    entity.getId(),
                    UserAccountDto.from(entity.getUserAccount()),
                    entity.getTitle(),
                    entity.getContent(),
                    entity.getHashtag(),
                    entity.getCreatedAt(),
                    entity.getCreatedBy(),
                    entity.getModifiedAt(),
                    entity.getModifiedBy()
            );
    }
    public Article toEntity() {
        return Article.of(
                userAccountDto.toEntity(),
                title,
                content,
                hashtag
        );
    }
}
