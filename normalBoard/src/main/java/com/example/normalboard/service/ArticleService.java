package com.example.normalboard.service;

import com.example.normalboard.domain.Article;
import com.example.normalboard.domain.type.SearchType;
import com.example.normalboard.dto.ArticleDto;
import com.example.normalboard.dto.ArticleWithCommentsDto;
import com.example.normalboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if(hasText(searchKeyword)){
            switch (searchType){
                case NICKNAME:
                    return articleRepository
                            .findByUserAccount_NicknameContaining(searchKeyword,pageable)
                            .map(ArticleDto :: from);
                case CONTENT:
                    return articleRepository
                            .findByContentContaining(searchKeyword,pageable)
                            .map(ArticleDto :: from);
                case TITLE :
                    return  articleRepository
                            .findByTitleContaining(searchKeyword,pageable)
                            .map(ArticleDto :: from);
                case ID :
                    return articleRepository
                            .findByUserAccount_UserIdContaining(searchKeyword,pageable)
                            .map(ArticleDto :: from);
                case HASHTAG:
                    return articleRepository
                            .findByHashtag("#" + searchKeyword,pageable)
                            .map(ArticleDto :: from);
            }
        }
        return articleRepository
                .findAll(pageable)
                .map(ArticleDto :: from);
    }

    private boolean hasText(String searchKeyword){
        if(StringUtils.hasText(searchKeyword))return true;
        return false;
    }



    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long articleId) {
        return articleRepository
                .findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    public void saveArticle(ArticleDto dto) {
        articleRepository.save(dto.toEntity());
    }

    public void updateArticle(ArticleDto dto) {
        try{
            tryToUpdateArticle(dto);
        }catch (EntityNotFoundException e){
            log.warn("수정할 게시글이 없습니다.");
        }
    }
    private void tryToUpdateArticle(ArticleDto dto){
        Article article = articleRepository.getReferenceById(dto.getId());
        article.updateContent(dto.getTitle(),dto.getContent(),dto.getHashtag());
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    public long getArticleCount() {
        return articleRepository.count();
    }

    public Page<ArticleDto> searchArticlesViaHashtag(String hashtag,Pageable pageable) {
        if(!StringUtils.hasText(hashtag)) return Page.empty(pageable);
        return articleRepository
                .findByHashtag(hashtag,pageable)
                .map(ArticleDto::from);
    }

    public List<String> getHashtags() {
        return articleRepository
                .findAllDistinctHashtags();
    }
}
