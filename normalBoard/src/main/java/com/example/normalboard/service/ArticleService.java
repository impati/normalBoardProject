package com.example.normalboard.service;

import com.example.normalboard.domain.Article;
import com.example.normalboard.domain.Hashtag;
import com.example.normalboard.domain.UserAccount;
import com.example.normalboard.domain.constant.SearchType;
import com.example.normalboard.dto.ArticleDto;
import com.example.normalboard.dto.ArticleWithCommentsDto;
import com.example.normalboard.repository.ArticleRepository;
import com.example.normalboard.repository.HashtagRepository;
import com.example.normalboard.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;
    private final HashtagService hashtagService;
    private final HashtagRepository hashtagRepository;

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
                case HASHTAG :
                    return articleRepository.findByHashtagNames(Arrays.stream(searchKeyword.split(" ")).collect(toList()),pageable)
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
    public ArticleWithCommentsDto getArticleWithComments(Long articleId) {
        return articleRepository
                .findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }


    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }


    public void saveArticle(ArticleDto dto) {
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.getUserAccountDto().getUserId());

        Set<Hashtag> hashtags = renewHashtagsFrom(dto.getContent());

        Article article = dto.toEntity(userAccount);

        article.addHashtags(hashtags);

        articleRepository.save(article);
    }

    private Set<Hashtag> renewHashtagsFrom(String content) {

        Set<String> hashtagNamesInContent = hashtagService.parseHashtagNames(content);

        Set<Hashtag> hashtags = hashtagService.findHashtagsByNames(hashtagNamesInContent);

        Set<String> existingHashtagNames = hashtags
                .stream()
                .map(Hashtag::getHashtagName)
                .collect(toSet());

        hashtags.addAll(hashtagNamesInContent
                .stream()
                .filter(ele->!existingHashtagNames.contains(ele))
                .map(Hashtag::of)
                .collect(toList()));

        return hashtags;
    }

    public void updateArticle(Long articleId,ArticleDto dto) {
        try{
            tryToUpdateArticle(articleId,dto);
        }catch (EntityNotFoundException e){
            log.warn("수정할 게시글이 없습니다.");
        }
    }

    private void tryToUpdateArticle(Long articleId,ArticleDto dto){
        Article article = articleRepository.getReferenceById(articleId);
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.getUserAccountDto().getUserId());
        if(article.getUserAccount().equals(userAccount)){

            article.updateContent(dto.getTitle(),dto.getContent());

            Set<Long> hashtagIds = article.getHashtags()
                    .stream().map(Hashtag::getId)
                    .collect(toUnmodifiableSet());

            article.clearHashtag();

            articleRepository.flush();

            hashtagService.deleteAllHashtagWithoutArticles(hashtagIds);

            article.addHashtags(renewHashtagsFrom(dto.getContent()));
        }

    }

    public void deleteArticle(Long articleId , String userId) {
        Article article = articleRepository.getReferenceById(articleId);

        Set<Long> hashtagIds = article.getHashtags()
                .stream().map(Hashtag::getId)
                .collect(toUnmodifiableSet());

        articleRepository.deleteByIdAndUserAccount_UserId(articleId,userId);
        articleRepository.flush();

        hashtagService.deleteAllHashtagWithoutArticles(hashtagIds);
    }

    public long getArticleCount() {
        return articleRepository.count();
    }

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticlesViaHashtag(String hashtagName, Pageable pageable) {

        if (hashtagName == null || hashtagName.isBlank()) {
            return Page.empty(pageable);
        }
        return articleRepository.findByHashtagNames(List.of(hashtagName), pageable).map(ArticleDto::from);
    }


    public List<String> getHashtags() {
        return hashtagRepository.findAllHashtagNames(); //TODO : 해시태그 서비스에 위임하는 것을 고려
    }
}
