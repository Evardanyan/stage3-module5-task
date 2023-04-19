package com.mjc.school.controller.util;

import com.mjc.school.controller.impl.AuthorController;
import com.mjc.school.service.dto.AuthorDtoResponse;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class AuthorModelAssembler {

    public AuthorDtoResponse addLinks(AuthorDtoResponse authorDtoResponse) {
        Long authorId = authorDtoResponse.getId();
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AuthorController.class).readById(authorId)).withSelfRel();
        authorDtoResponse.add(selfLink);
        return authorDtoResponse;
    }
}
