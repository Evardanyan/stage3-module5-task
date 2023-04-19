package com.mjc.school.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.RepresentationModel;

import java.time.OffsetDateTime;

public class AuthorDtoResponse extends RepresentationModel<AuthorDtoResponse> {

    private Long id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime createDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime lastUpdatedDate;

    public AuthorDtoResponse(Long id, String name, OffsetDateTime createDate, OffsetDateTime lastUpdatedDate) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public OffsetDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    @Override
    public String toString() {
        if (id == null && createDate == null && lastUpdatedDate == null) {
            return "AuthorDtoResponse{name='" + name + '\'' +
                    '}';
        } else {
            return "AuthorDtoResponse{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", createDate=" + createDate +
                    ", lastUpdatedDate=" + lastUpdatedDate +
                    '}';
        }
    }
}

