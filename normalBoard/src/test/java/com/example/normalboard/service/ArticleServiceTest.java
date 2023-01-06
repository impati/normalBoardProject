package com.example.normalboard.service;

import com.example.normalboard.domain.Article;
import com.example.normalboard.domain.type.SearchType;
import com.example.normalboard.dto.ArticleDto;
import com.example.normalboard.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("비즈니스 로직 - 게시글 ")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    // System under Test : test Target
    @InjectMocks private ArticleService sut;
    @Mock private ArticleRepository articleRepository;

    /**
     * - 검색
     * - 각 게시글 페이지로 이동
     * - 페이지 네이션
     * - 홈 버튼 -> 게시판 페이지로 리다이렉션
     * - 정렬 기능
     *
     */

    @Test
    @DisplayName("게시글을 검색하면 게시글 리스트를 반환한다")
    public void givenSearchParam_whenSearchingArticles_thenReturnArticleList() throws Exception{
        // Given

        // when
        // 제목  , 본문 , ID , 닉네임 , 해시태그
        Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE,"search keyword");

        //then
        assertThat(articles).isNotNull();

    }

    @Test
    @DisplayName("게시글을 클릭하면 게시글  반환한다")
    public void givenArticleId_whenClickArticle_thenReturnArticle() throws Exception{
        // build


        // operate
        ArticleDto article = sut.searchArticle(1L);

        // check
        assertThat(article).isNotNull();
    }

    @Test
    @DisplayName("게시글 정보를 입력하면 게시글을 생성한다")
    public void givenArticleInfo_whenSavingArticle_thenSaveArticle() throws Exception{
        // build
        ArticleDto dto = ArticleDto.of("새 글","안녕하세요.","#java");


        BDDMockito.given(articleRepository.save(any(Article.class))).willReturn(null);

        // operate

        sut.saveArticle(dto);

        // check
        BDDMockito.then(articleRepository).should().save(any(Article.class));
    }

    @Test
    @DisplayName("게시글의 ID 와 수정 정보를 입력하면 게시글을 수정한다.")
    public void givenIDAndContentInfo_whenUpdatingArticle_thenUpdateArticle() throws Exception{
        // given

        BDDMockito.given(articleRepository.save(any(Article.class))).willReturn(null);
        // when

        sut.updateArticle(1L,ArticleDto.of("","",""));
        // then

        BDDMockito.then(articleRepository).should().save(any(Article.class));
    }

    @Test
    @DisplayName("게시글의 ID 입력하면 게시글을 삭제한다")
    public void givenId_whenDeletingArticle_thenDeleteArticle() throws Exception{
        // given

        BDDMockito.willDoNothing().given(articleRepository).delete(any(Article.class));
        // when

        sut.deleteArticle(1L);
        // then

        BDDMockito.then(articleRepository).should().delete(any(Article.class));
    }




}