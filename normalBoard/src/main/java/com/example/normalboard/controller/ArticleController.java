package com.example.normalboard.controller;

import com.example.normalboard.domain.Article;
import com.example.normalboard.domain.type.SearchType;
import com.example.normalboard.dto.ArticleDto;
import com.example.normalboard.dto.ArticleWithCommentsDto;
import com.example.normalboard.dto.response.ArticleResponse;
import com.example.normalboard.dto.response.ArticleWithCommentResponse;
import com.example.normalboard.repository.ArticleRepository;
import com.example.normalboard.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

/**
 * /articles
 * /articles/{article-id}
 * /articles/search
 * /articles/search-hashtag
 */
@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping()
    public String returnArticlesView(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable,
            Model model){
        model.addAttribute("articles",articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse :: from));
        return "/articles/index";
    }

    @GetMapping("/{articleId}")
    public String returnArticleView(@PathVariable Long articleId, Model model){
        ArticleWithCommentsDto article = articleService.getArticle(articleId);
        model.addAttribute("article",ArticleWithCommentResponse.from(article));
        model.addAttribute("articleComments", article.getArticleCommentDtos());
        return "/articles/detail";
    }


}
