package com.example.normalboard.domain;

import lombok.*;

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

    private String hashtag;


    @OneToMany(mappedBy = "article",cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private Set<ArticleComment> articleComments = new LinkedHashSet<>();


    public void updateContent(String content){
        this.content = content ;
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
}
