package com.example.normalboard.controller;

import com.example.normalboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping()
    public String returnArticlesView(Model model){
        model.addAttribute("articles",new ArrayList<>());
        return "/articles/index";
    }


}
