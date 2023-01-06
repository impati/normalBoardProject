package com.example.normalboard.dto;

import com.example.normalboard.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class ArticleWithCommentsDto{
        private Long id;
        private UserAccountDto userAccountDto;
        private Set<ArticleCommentDto> articleCommentDtos;
        private String title;
        private String content;
        private String hashtag;
        private LocalDateTime createdAt;
        private String createdBy;
        private LocalDateTime modifiedAt;
        private String modifiedBy;

        public static ArticleWithCommentsDto of(Long id, UserAccountDto userAccountDto, Set<ArticleCommentDto> articleCommentDtos, String title, String content, String hashtag, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
                return new ArticleWithCommentsDto(id, userAccountDto, articleCommentDtos, title, content, hashtag, createdAt, createdBy, modifiedAt, modifiedBy);
        }

        public static ArticleWithCommentsDto from(Article entity) {
                return new ArticleWithCommentsDto(
                        entity.getId(),
                        UserAccountDto.from(entity.getUserAccount()),
                        entity.getArticleComments().stream()
                                .map(ArticleCommentDto::from)
                                .collect(Collectors.toCollection(LinkedHashSet::new)),
                        entity.getTitle(),
                        entity.getContent(),
                        entity.getHashtag(),
                        entity.getCreatedAt(),
                        entity.getCreatedBy(),
                        entity.getModifiedAt(),
                        entity.getModifiedBy()
                );
        }
}