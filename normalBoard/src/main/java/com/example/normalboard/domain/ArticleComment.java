package com.example.normalboard.domain;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt")

})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
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

    @Setter
    @Column
    private Long parentCommentId;

    @ToString.Exclude
    @OrderBy("createdAt ASC")
    @OneToMany(mappedBy = "parentCommentId",cascade = CascadeType.ALL) // 부모 댓글이 지워진다면 자식 댓글을 모두 삭제
    private Set<ArticleComment> childComments = new LinkedHashSet<>();


    public void addChildComment(ArticleComment child){
        child.setParentCommentId(this.getId());
        this.getChildComments().add(child);
    }

    public static ArticleComment of(UserAccount userAccount , Article article , String content){
        return new ArticleComment(article,content,userAccount,null);
    }

    private ArticleComment(Article article, String content, UserAccount userAccount, Long parentCommentId) {
        this.article = article;
        this.content = content;
        this.userAccount = userAccount;
        this.parentCommentId = parentCommentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment)) return false;
        ArticleComment that = (ArticleComment) o;
        return this.getId() != null && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
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
