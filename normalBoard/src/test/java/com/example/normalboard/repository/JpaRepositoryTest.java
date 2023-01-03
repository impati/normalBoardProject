package com.example.normalboard.repository;

import com.example.normalboard.JpaConfig;
import com.example.normalboard.domain.Article;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("testdb")
@DisplayName("Jpa 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleCommentRepository articleCommentRepository;


    @DisplayName("select Test")
    @Test
    public void givenTestData_whenSelecting_thenWorksFine() throws Exception{
        // build

        // operate
        List<Article> articles = articleRepository.findAll();

        // check

        Assertions.assertThat(articles)
                .isNotNull()
                .hasSize(100);

    }
    @DisplayName("insert Test")
    @Test
    public void givenTestData_whenInserting_thenWorksFine() throws Exception{
        // build

        long previousCount = articleRepository.count();

        Article article = Article.of("new Article","hello world");
        articleRepository.save(article);

        Assertions.assertThat(articleRepository.count())
                .isEqualTo(previousCount + 1);

    }

    @DisplayName("update Test")
    @Test
    public void givenTestData_whenUpdating_thenWorksFine() throws Exception{
        // build
        String newContent = "HELLO WORLD";

        Article article = Article.of("new Article","hello world");
        articleRepository.save(article);

        article.updateContent(newContent);

        articleRepository.flush();


        Optional<Article> OptionalFindArticle = articleRepository.findById(article.getId());
        Assertions.assertThat(OptionalFindArticle).isPresent();
        Article findArticle = OptionalFindArticle.get();

        Assertions.assertThat(findArticle.getContent()).isEqualTo(newContent);
    }
    @DisplayName("delete Test")
    @Test
    public void givenTestData_whenDeleting_thenWorksFine() throws Exception{
        // build
        long previousCount = articleRepository.count();

        Article article = articleRepository.findById(1L).get();
        articleRepository.delete(article);

        Assertions.assertThat(articleRepository.count())
                .isEqualTo(previousCount - 1);

    }

}