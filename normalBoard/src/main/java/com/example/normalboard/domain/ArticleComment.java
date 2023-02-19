package com.example.normalboard.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt")

})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true) //baseEntity 까지 ToString
public class ArticleComment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Article article;

    @Column(nullable = false,length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserAccount userAccount;



    public static ArticleComment of(UserAccount userAccount , Article article , String content){
        return new ArticleComment(article,content,userAccount);
    }


    public static ArticleComment of(Article article, UserAccount userAccount, String content) {
        return new ArticleComment(article,content,userAccount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment)) return false;
        ArticleComment that = (ArticleComment) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    private ArticleComment(Article article, String content, UserAccount userAccount) {
        this.article = article;
        this.content = content;
        this.userAccount = userAccount;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
