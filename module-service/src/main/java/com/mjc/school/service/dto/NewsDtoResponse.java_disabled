package com.mjc.school.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mjc.school.repository.model.impl.TagModel;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

public record NewsDtoResponse(Long id, String title, String content, @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") OffsetDateTime createDate, @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") OffsetDateTime lastUpdatedDate,
                              Long authorId, List<TagModel> tagList) {
    public NewsDtoResponse(Long id, String title, String content,  OffsetDateTime createDate, Long authorId) {
        this(id, title, content, null, null, authorId, null);
    }
}
