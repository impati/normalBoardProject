package com.example.normalboard.service;

import com.example.normalboard.domain.Article;
import com.example.normalboard.domain.ArticleComment;
import com.example.normalboard.domain.UserAccount;
import com.example.normalboard.dto.ArticleCommentDto;
import com.example.normalboard.repository.ArticleCommentRepository;
import com.example.normalboard.repository.ArticleRepository;
import com.example.normalboard.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ArticleCommentService {

    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> searchArticleComments(Long articleId) {
        return articleCommentRepository.findByArticle_Id(articleId)
                .stream()
                .map(ArticleCommentDto::from)
                .collect(toList());
    }

    public void saveArticleComment(ArticleCommentDto dto) {
        try {
            Article article = articleRepository.getReferenceById(dto.getArticleId());
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.getUserAccountDto().getUserId());

            ArticleComment articleComment = dto.toEntity(article, userAccount);

            if (dto.getParentCommentId() != null) {
                ArticleComment parentComment = articleCommentRepository.getReferenceById(dto.getParentCommentId());
                parentComment.addChildComment(articleComment);
            } else {
                articleCommentRepository.save(articleComment);
            }

        } catch (EntityNotFoundException e) {
            log.warn("?????? ?????? ??????. ????????? ???????????? ?????? ??? ???????????? - dto: {}", dto);
        }
    }

    public void updateArticleComment(ArticleCommentDto dto) {
        try {
            ArticleComment articleComment = articleCommentRepository.getReferenceById(dto.getId());
            if (dto.getContent() != null) {
                articleComment.setContent(dto.getContent());
            }
        } catch (EntityNotFoundException e) {
            log.warn("?????? ???????????? ??????. ????????? ?????? ??? ???????????? - dto: {}", dto);
        }
    }

    public void deleteArticleComment(Long articleCommentId, String userId) {
        articleCommentRepository.deleteByIdAndUserAccount_UserId(articleCommentId, userId);
    }

}
