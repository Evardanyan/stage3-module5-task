package com.mjc.school.service.dto;

import org.springframework.hateoas.RepresentationModel;

public class TagDtoResponse extends RepresentationModel<TagDtoResponse> {

    private Long id;
    private String name;

    public TagDtoResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
