package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.util.AuthorModelAssembler;
import com.mjc.school.controller.util.CommentModelAssembler;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
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
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/comments")
@Validated
@Api(tags = "Comment Management", produces = "application/json", value = "Operations for creating, updating, retrieving and deleting news in the application")
public class CommentController implements BaseController<CommentDtoRequest, CommentDtoResponse, Long> {

    @Autowired
    private CommentModelAssembler commentModelAssembler;
    private final BaseService<CommentDtoRequest, CommentDtoResponse, Long> service;

    public CommentController(BaseService<CommentDtoRequest, CommentDtoResponse, Long> service) {
        this.service = service;
    }


    @Override
    @GetMapping
    @ApiOperation(value = "get a list of all comments", response = CommentDtoResponse.class, responseContainer = "Page")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the comments by news ID"),
            @ApiResponse(code = 404, message = "News not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Page<CommentDtoResponse>> readAll(
            @RequestParam(value = "page", defaultValue = "0")
            @ApiParam(value = "Page number of the results", defaultValue = "10") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "content") String sort,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        PageRequest pageRequest = service.buildPageRequest(page, size, sort, direction);

        Page<CommentDtoResponse> commentsPage = service.readAll(pageRequest);

        commentsPage.getContent().forEach(authorDtoResponse -> {
            commentModelAssembler.addLinks(authorDtoResponse);
        });

        return new ResponseEntity<>(commentsPage, HttpStatus.OK);
    }

    @Override
    @GetMapping(value = "/{id:\\d+}")
    @ApiOperation(value = "Retrieve specific comment with the supplied id", response = CommentDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the comment with the supplied id"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    public ResponseEntity<CommentDtoResponse> readById(@Valid @PathVariable Long id) {
        return new ResponseEntity<>(commentModelAssembler.addLinks(service.readById(id)), HttpStatus.OK);
    }


    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new comment", response = NewsDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created a new  news"),
            @ApiResponse(code = 400, message = "Invalid news data"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<CommentDtoResponse> create(@Valid @RequestBody CommentDtoRequest createRequest) {
        CommentDtoResponse commentDtoResponse = service.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentDtoResponse);
    }

    @Override
//    @PutMapping("/{id}")
    @PatchMapping("/{id}")
    @ApiOperation(value = "Update an existing comment", response = NewsDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the news"),
            @ApiResponse(code = 400, message = "Invalid author data"),
            @ApiResponse(code = 404, message = "News not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<CommentDtoResponse> update(@PathVariable Long id, @Valid @RequestBody CommentDtoRequest updateRequest) {
        CommentDtoRequest updatedRequest = new CommentDtoRequest(id, updateRequest.content());
        CommentDtoResponse commentDtoResponse = service.update(updatedRequest);
        return ResponseEntity.status(HttpStatus.OK).body(commentDtoResponse);
    }


    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete an comment by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the comment"),
            @ApiResponse(code = 404, message = "Comment not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }

    @Override
    @GetMapping("/news/{id}/comments")
    @ApiOperation(value = "Retrieve comments by News ID", response = CommentDtoResponse.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the comments for the specified News ID"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    public ResponseEntity<List<CommentDtoResponse>> readCommentsByNewsId(@PathVariable Long id) {
        List<CommentDtoResponse> commentsByNewsId = service.readCommentsByNewsId(id);
        return ResponseEntity.ok(commentsByNewsId);
    }

}
