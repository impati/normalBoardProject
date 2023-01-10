package com.example.normalboard.dto.request;

import com.example.normalboard.dto.ArticleCommentDto;
import com.example.normalboard.dto.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleCommentRequest {
    private Long articleId;
    private String content;

    public static ArticleCommentRequest of(Long articleId,String content){
        return new ArticleCommentRequest(articleId,content);
    }

    public ArticleCommentDto toDto(UserAccountDto accountDto){
        return ArticleCommentDto.of(
                articleId,
                accountDto,
                content
                );
    }

}
