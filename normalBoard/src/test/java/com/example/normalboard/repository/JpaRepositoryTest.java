package com.example.normalboard.repository;

import com.example.normalboard.config.JpaConfig;
import com.example.normalboard.domain.Article;
import com.example.normalboard.domain.UserAccount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@ActiveProfiles("testdb")
@DisplayName("Jpa 연결 테스트")
@Import({JpaRepositoryTest.TestJpaConfig.class})
@DataJpaTest
class JpaRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleCommentRepository articleCommentRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @DisplayName("select Test")
    @Test
    public void givenTestData_whenSelecting_thenWorksFine() throws Exception{
        // build

        // operate
        List<Article> articles = articleRepository.findAll();

        // check

        Assertions.assertThat(articles)
                .isNotNull()
                .hasSize(123);

    }
    @DisplayName("insert Test")
    @Test
    public void givenTestData_whenInserting_thenWorksFine() throws Exception{
        // build

        long previousCount = articleRepository.count();
        UserAccount userAccount = userAccountRepository.save(UserAccount.of("newUno", "pw", null, null, null));
        Article article = Article.of(userAccount, "new article", "new content", "#spring");

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

        article.updateContent(newContent,newContent,null);

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


    @EnableJpaAuditing
    @TestConfiguration
    static class TestJpaConfig{
        @Bean
        public AuditorAware<String> auditorAware(){
            return () ->Optional.of("impati");
        }
        @Bean
        public JPAQueryFactory jpaQueryFactory(EntityManager entityManager){
            return new JPAQueryFactory(entityManager);
        }
    }

}