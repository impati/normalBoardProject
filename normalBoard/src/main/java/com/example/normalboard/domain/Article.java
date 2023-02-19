package com.example.normalboard.domain;

import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "createdAt")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Article extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String title;
    @Column(nullable = false,length = 10000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @Setter
    private UserAccount userAccount;

    @ToString.Exclude
    @JoinTable(
            name = "article_hashtag",
            joinColumns = @JoinColumn(name = "articleId"),
            inverseJoinColumns = @JoinColumn(name = "hashtagId")
    )
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Set<Hashtag> hashtags = new LinkedHashSet<>();

    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "article",cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private Set<ArticleComment> articleComments = new LinkedHashSet<>();


    public void addHashtag(Hashtag hashtag){
        this.getHashtags().add(hashtag);
    }

    public void addHashtags(Collection<Hashtag> hashtags){
        this.getHashtags().addAll(hashtags);
    }

    public void clearHashtag(){
        this.getHashtags().clear();
    }

    private Article(String title, String content, UserAccount userAccount) {
        this.title = title;
        this.content = content;
        this.userAccount = userAccount;
    }

    public void updateContent(String title, String content){
        if(StringUtils.hasText(title)) this.title = title;
        if(StringUtils.hasText(content)) this.content = content;
    }


    public static Article of(UserAccount userAccount, String title, String content) {
        return new Article(title, content, userAccount);
    }

    public static Article of(String title , String content){
        Article article = new Article();
        article.title = title;
        article.content = content;
        return article;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article that = (Article) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

}
