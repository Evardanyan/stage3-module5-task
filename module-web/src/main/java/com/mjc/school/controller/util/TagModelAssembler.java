package com.mjc.school.controller.util;

import com.mjc.school.controller.impl.CommentController;
import com.mjc.school.controller.impl.TagController;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.dto.TagDtoResponse;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class TagModelAssembler {

    public TagDtoResponse addLinks(TagDtoResponse tagDtoResponse) {
        Long tagId = tagDtoResponse.getId();
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class).readById(tagId)).withSelfRel();
        tagDtoResponse.add(selfLink);
        return tagDtoResponse;
    }

}
