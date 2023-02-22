package com.example.normalboard.dto.request;

import com.example.normalboard.dto.ArticleCommentDto;
import com.example.normalboard.dto.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleCommentRequest {

    private Long articleId;
    private Long parentCommentId;
    private String content;

    public static ArticleCommentRequest of(Long articleId,Long articleCommentId,String content){
        return new ArticleCommentRequest(articleId,articleCommentId,content);
    }

    public static ArticleCommentRequest of(Long articleId,String content){
        return new ArticleCommentRequest(articleId,null,content);
    }

    public ArticleCommentDto toDto(UserAccountDto accountDto){
        return ArticleCommentDto.of(
                articleId,
                parentCommentId,
                accountDto,
                content
                );
    }

}
