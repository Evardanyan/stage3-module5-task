package com.mjc.school.service.dto;

import com.mjc.school.repository.model.impl.NewsModel;
import org.springframework.hateoas.RepresentationModel;

public class CommentDtoResponse extends RepresentationModel<CommentDtoResponse> {
    private final Long id;
    private final String content;
    private final NewsModel news;

    public CommentDtoResponse(Long id, String content, NewsModel news) {
        this.id = id;
        this.content = content;
        this.news = news;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public NewsModel getNews() {
        return news;
    }
}
