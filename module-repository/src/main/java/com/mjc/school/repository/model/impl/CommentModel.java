package com.mjc.school.repository.model.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mjc.school.repository.model.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
//@Scope("prototype")
@Table(name = "comments")
public class CommentModel implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "news_id", nullable = false)
    private NewsModel news;



    public CommentModel() {
    }

    public CommentModel(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public CommentModel(Long id) {
        this.id = id;
    }

    public CommentModel(Long id, String content, NewsModel news) {
        this.id = id;
        this.content = content;
        this.news = news;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NewsModel getNews() {
        return news;
    }

    public void setNews(NewsModel news) {
        this.news = news;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentModel that = (CommentModel) o;
        return Objects.equals(id, that.id) && Objects.equals(content, that.content) && Objects.equals(news, that.news);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, news);
    }

    @Override
    public String toString() {
        return "CommentModel{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", news=" + news +
                '}';
    }
}
