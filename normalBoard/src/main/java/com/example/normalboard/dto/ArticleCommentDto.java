package com.example.normalboard.dto;

import com.example.normalboard.domain.Article;
import com.example.normalboard.domain.ArticleComment;
import com.example.normalboard.domain.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

import static java.time.LocalTime.now;

@Data
@AllArgsConstructor
public class ArticleCommentDto {
    private Long id;
    private Long articleId;
    private  UserAccountDto userAccountDto;
    private String content;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    public static ArticleCommentDto from(ArticleComment entity) {
        return new ArticleCommentDto(
                entity.getId(),
                entity.getArticle().getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public static ArticleCommentDto of(Long id, Long articleId, UserAccountDto userAccountDto, String content, LocalDateTime now, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new ArticleCommentDto(id,articleId,userAccountDto,content,now,createdBy,modifiedAt,modifiedBy);
    }

    public static ArticleCommentDto of(Long articleId, UserAccountDto accountDto, String content) {
        return new ArticleCommentDto(null,articleId,accountDto,content,null,null,null,null);
    }

    public ArticleComment toEntity(Article article, UserAccount account) {
        return ArticleComment.of(
                account,
                article,
                content
        );
    }



}
