package com.example.normalboard.controller;

import com.example.normalboard.domain.constant.FormStatus;
import com.example.normalboard.domain.constant.SearchType;
import com.example.normalboard.dto.ArticleDto;
import com.example.normalboard.dto.UserAccountDto;
import com.example.normalboard.dto.request.ArticleRequest;
import com.example.normalboard.dto.response.ArticleResponse;
import com.example.normalboard.dto.response.ArticleWithCommentResponse;
import com.example.normalboard.dto.security.BoardPrincipal;
import com.example.normalboard.service.ArticleService;
import com.example.normalboard.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final PaginationService paginationService;

    @GetMapping
    public String returnArticlesView(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable,
            Model model){

        Page<ArticleDto> articleResponse = articleService.searchArticles(searchType, searchValue, pageable);
        List<Integer> barNumber = paginationService.getPaginationBarNumbers(pageable.getPageNumber(),articleResponse.getTotalPages());

        model.addAttribute("articles",articleResponse.map(ArticleResponse :: from));
        model.addAttribute("searchTypes",SearchType.values());
        model.addAttribute("paginationBarNumbers",barNumber);
        model.addAttribute("searchTypeHashtag",SearchType.HASHTAG);

        return "/articles/index";
    }

    @GetMapping("/{articleId}")
    public String returnArticleView(@PathVariable Long articleId, Model model){
        ArticleWithCommentResponse article = ArticleWithCommentResponse.from(articleService.getArticleWithComments(articleId));

        model.addAttribute("article",article);
        model.addAttribute("articleComments",article.getArticleCommentResponses());
        model.addAttribute("totalCount", articleService.getArticleCount());
        model.addAttribute("searchTypeHashtag",SearchType.HASHTAG);
        return "/articles/detail";
    }

    @GetMapping("/search-hashtag")
    public String searchByHashtag(@RequestParam(required = false) String searchValue,
                                  @PageableDefault(size = 10, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable,
                                  Model model){
        Page<ArticleDto> articleResponse = articleService.searchArticlesViaHashtag(searchValue, pageable);
        List<Integer> barNumber = paginationService.getPaginationBarNumbers(pageable.getPageNumber(),articleResponse.getTotalPages());

        model.addAttribute("articles",articleResponse.map(ArticleResponse :: from));
        model.addAttribute("hashtags",articleService.getHashtags());
        model.addAttribute("paginationBarNumbers",barNumber);
        model.addAttribute("searchType",SearchType.HASHTAG);
        return "/articles/search-hashtag";
    }

    @GetMapping("/form")
    public String articleForm(ModelMap map) {
        map.addAttribute("formStatus", FormStatus.CREATE);

        return "articles/form";
    }

    @PostMapping("/form")
    public String postNewArticle(
            ArticleRequest articleRequest,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal) {

        articleService.saveArticle(articleRequest.toDto(boardPrincipal.toDto()));

        return "redirect:/articles";
    }

    @GetMapping("/{articleId}/form")
    public String updateArticleForm(@PathVariable Long articleId, ModelMap map) {
        ArticleResponse article = ArticleResponse.from(articleService.getArticle(articleId));

        map.addAttribute("article", article);
        map.addAttribute("formStatus", FormStatus.UPDATE);

        return "articles/form";
    }

    @PostMapping ("/{articleId}/form")
    public String updateArticle(
            @PathVariable Long articleId,
            ArticleRequest articleRequest,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal
            ) {
        articleService.updateArticle(articleId, articleRequest.toDto(boardPrincipal.toDto()));

        return "redirect:/articles/" + articleId;
    }

    @PostMapping ("/{articleId}/delete")
    public String deleteArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        articleService.deleteArticle(articleId,boardPrincipal.getUsername());
        return "redirect:/articles";
    }
}
