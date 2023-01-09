package com.example.normalboard.domain;

import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
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
    private UserAccount userAccount;

    private String hashtag;


    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "article",cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private Set<ArticleComment> articleComments = new LinkedHashSet<>();


    public void updateContent(String title, String content, String hashtag){
        if(StringUtils.hasText(title)) this.title = title;
        if(StringUtils.hasText(content)) this.content = content;
        this.hashtag = hashtag;
    }


    public static Article of(UserAccount userAccount, String title, String content, String hashtag) {
        return new Article(userAccount, title, content, hashtag);
    }

    public static Article of(String title , String content){
        Article article = new Article();
        article.title = title;
        article.content = content;
        return article;
    }

    public static Article of(String title , String content,String hashtag){
        Article article = of(title, content);
        article.hashtag = hashtag;
        return article;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private Article (UserAccount userAccount ,String title,String content,String hashtag){
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }
}
