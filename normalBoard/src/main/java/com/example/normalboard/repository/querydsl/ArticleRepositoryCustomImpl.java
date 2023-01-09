package com.example.normalboard.repository.querydsl;

import com.example.normalboard.domain.QArticle;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.normalboard.domain.QArticle.article;

@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findAllDistinctHashtags() {
        return queryFactory
                .selectDistinct(article.hashtag)
                .from(article)
                .fetch();
    }

}
