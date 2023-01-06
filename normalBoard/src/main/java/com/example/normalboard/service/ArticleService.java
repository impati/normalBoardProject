package com.example.normalboard.service;

import com.example.normalboard.domain.Article;
import com.example.normalboard.domain.type.SearchType;
import com.example.normalboard.dto.ArticleDto;
import com.example.normalboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String search_keyword){
        return Page.empty();
    }

    public ArticleDto searchArticle(Long id) {
        return ArticleDto.of("","","");
    }

    public void saveArticle(ArticleDto dto) {

    }

    public void updateArticle(Long id, ArticleDto dto) {

    }

    public void deleteArticle(Long id) {
    }
}
