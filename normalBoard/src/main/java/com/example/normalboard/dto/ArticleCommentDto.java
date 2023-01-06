package com.example.normalboard.dto;

import java.time.LocalDateTime;

public class ArticleCommentDto {

    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private String createdBy ;
    private String modifiedBy;
    private String content;


    public static ArticleCommentDto of(LocalDateTime createAt, String createdBy, LocalDateTime modified, String modifiedBy, String comment) {
        return new ArticleCommentDto(createAt,modified,createdBy,modifiedBy,comment);
    }

    private ArticleCommentDto(LocalDateTime createAt, LocalDateTime modifiedAt, String createdBy, String modified, String content) {
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
        this.createdBy = createdBy;
        this.modifiedBy = modified;
        this.content = content;
    }
}
