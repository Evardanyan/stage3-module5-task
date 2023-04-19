package com.mjc.school.controller.util;

import com.mjc.school.controller.impl.NewsController;
import com.mjc.school.service.dto.NewsDtoResponse;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class NewsModelAssembler {

    public NewsDtoResponse addLinks(NewsDtoResponse newsDtoResponse) {
        Long newsId = newsDtoResponse.getId();
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(NewsController.class).readById(newsId)).withSelfRel();
        newsDtoResponse.add(selfLink);
        return newsDtoResponse;
    }

}
