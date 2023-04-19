package com.mjc.school.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mjc.school.repository.model.impl.TagModel;
import org.springframework.hateoas.RepresentationModel;

import java.time.OffsetDateTime;
import java.util.List;

public class NewsDtoResponse extends RepresentationModel<NewsDtoResponse> {

    private Long id;
    private String title;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime createDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime lastUpdatedDate;
    private Long authorId;
    private List<TagModel> tagList;


    public NewsDtoResponse() {
    }

    public NewsDtoResponse(Long id, String title, String content, OffsetDateTime createDate, OffsetDateTime lastUpdatedDate, Long authorId, List<TagModel> tagList) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.authorId = authorId;
        this.tagList = tagList;
    }

    public NewsDtoResponse(Long id, String title, String content, OffsetDateTime createDate, Long authorId) {
        this(id, title, content, createDate, null, authorId, null);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public OffsetDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public List<TagModel> getTagList() {
        return tagList;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
    }

    public void setLastUpdatedDate(OffsetDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public void setTagList(List<TagModel> tagList) {
        this.tagList = tagList;
    }
}

