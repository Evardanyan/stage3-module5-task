package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.util.AuthorModelAssembler;
import com.mjc.school.controller.util.NewsModelAssembler;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.CommentDtoResponse;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/news")
@Validated
@Api(tags = "News Management", produces = "application/json", value = "Operations for creating, updating, retrieving and deleting news in the application")
public class NewsController implements BaseController<NewsDtoRequest, NewsDtoResponse, Long> {

    @Autowired
    private NewsModelAssembler newsModelAssembler;

    private final BaseService<NewsDtoRequest, NewsDtoResponse, Long> service;

    public NewsController(BaseService<NewsDtoRequest, NewsDtoResponse, Long> service) {
        this.service = service;
    }


    @Override
    @GetMapping
    @ApiOperation(value = "Get a list of all news", response = NewsDtoResponse.class, responseContainer = "Page")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the list of news"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Page<NewsDtoResponse>> readAll(
            @RequestParam(value = "page", defaultValue = "0")
            @ApiParam(value = "Page number of the results", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10")
            @ApiParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "title")
            @ApiParam(value = "Sort field", defaultValue = "title") String sort,
            @RequestParam(value = "direction", defaultValue = "asc")
            @ApiParam(value = "Sort direction", defaultValue = "asc") String direction) {


        PageRequest pageRequest = service.buildPageRequest(page, size, sort, direction);

//        Page<NewsDtoResponse> newsPage = service.readAll(pageRequest);
//
//        return new ResponseEntity<>(newsPage, HttpStatus.OK);
        Page<NewsDtoResponse> newsPage = service.readAll(pageRequest);

        newsPage.getContent().forEach(authorDtoResponse -> {
            newsModelAssembler.addLinks(authorDtoResponse);
        });
        return new ResponseEntity<>(newsPage, HttpStatus.OK);

    }

    @Override
    @GetMapping(value = "/{id:\\d+}")
    @ApiOperation(value = "Retrieve specific news with the supplied id", response = NewsDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the news with the supplied id"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    public ResponseEntity<NewsDtoResponse> readById(@PathVariable Long id) {
        return new ResponseEntity<>(newsModelAssembler.addLinks(service.readById(id)), HttpStatus.OK);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new news", response = NewsDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created a new news"),
            @ApiResponse(code = 400, message = "Invalid news data"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<NewsDtoResponse> create(
            @Valid @RequestBody
            @ApiParam(value = "News data", readOnly = true) NewsDtoRequest createRequest) {
        NewsDtoResponse newsDtoResponse = service.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newsDtoResponse);
    }

    @Override
    @PutMapping("/{id}")
    @ApiOperation(value = "Update an existing news", response = NewsDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the news"),
            @ApiResponse(code = 400, message = "Invalid news data"),
            @ApiResponse(code = 404, message = "News not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<NewsDtoResponse> update(
            @PathVariable
            @ApiParam(value = "News ID", readOnly = true) Long id,
            @Valid @RequestBody
            @ApiParam(value = "Updated news data", required = true) NewsDtoRequest updateRequest) {
        NewsDtoRequest updatedRequest = new NewsDtoRequest(id, updateRequest.title(), updateRequest.content(), updateRequest.authorId(), updateRequest.tagId());
        NewsDtoResponse newsDtoResponse = service.update(updatedRequest);
        return ResponseEntity.status(HttpStatus.OK).body(newsDtoResponse);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a news article by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the news article"),
            @ApiResponse(code = 404, message = "News not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }

    @GetMapping("/search")
    @ApiOperation(value = "Get news articles by query parameters", response = NewsDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved news articles"),
            @ApiResponse(code = 404, message = "No news articles found")
    })
    public ResponseEntity<NewsDtoResponse> getNewsByParams(
            @Valid @RequestParam(required = false) @ApiParam(value = "Tag name") String tagName,
            @Valid @RequestParam(required = false) @ApiParam(value = "Tag ID") Long tagId,
            @Valid @RequestParam(required = false) @ApiParam(value = "Author name") String authorName,
            @Valid @RequestParam(required = false) @ApiParam(value = "News title") String title,
            @Valid @RequestParam(required = false) @ApiParam(value = "News content") String content) {
        NewsDtoResponse response = service.getNewsByParams(tagName, tagId, authorName, title, content);
        HttpStatus status = response != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

}
