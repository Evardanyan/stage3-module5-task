package com.mjc.school.service.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public record CommentDtoRequest(
        Long id,
        @NotBlank(message = "Comments Content cannot be blank")
        @Size(min = 3, max = 15, message = "Comments Content must be between 3 and 15 characters")
        String content,
        Long newsId) {

    public CommentDtoRequest(Long id, String content) {
        this(id, content, null);
    }
}

