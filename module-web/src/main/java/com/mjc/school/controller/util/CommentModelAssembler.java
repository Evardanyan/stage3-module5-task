package com.mjc.school.controller.util;

import com.mjc.school.controller.impl.CommentController;
import com.mjc.school.service.dto.CommentDtoResponse;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class CommentModelAssembler {
    public CommentDtoResponse addLinks(CommentDtoResponse commentDtoResponse) {
        Long commentId = commentDtoResponse.getId();
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CommentController.class).readById(commentId)).withSelfRel();
        commentDtoResponse.add(selfLink);
        return commentDtoResponse;
    }
}
