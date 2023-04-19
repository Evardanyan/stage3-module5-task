package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.util.AuthorModelAssembler;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/authors")
@Validated
@Api(tags = "Author Management", produces = "application/json", value = "Operations for creating, updating, retrieving and deleting news in the application")
public class AuthorController implements BaseController<AuthorDtoRequest, AuthorDtoResponse, Long> {

    @Autowired
    private AuthorModelAssembler authorModelAssembler;

    private final BaseService<AuthorDtoRequest, AuthorDtoResponse, Long> service;

    public AuthorController(BaseService<AuthorDtoRequest, AuthorDtoResponse, Long> service) {
        this.service = service;
    }


    @GetMapping
    @ApiOperation(value = "Get a list of all authors", response = AuthorDtoResponse.class, responseContainer = "Page")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the list of authors"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Page<AuthorDtoResponse>> readAll(
            @RequestParam(value = "page", defaultValue = "0")
            @ApiParam(value = "Page number of the results", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10")
            @ApiParam(value = "Number of results per page", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "name")
            @ApiParam(value = "Sort field", defaultValue = "name") String sort,
            @RequestParam(value = "direction", defaultValue = "asc")
            @ApiParam(value = "Sort direction", defaultValue = "asc") String direction) {

        PageRequest pageRequest = service.buildPageRequest(page, size, sort, direction);

        Page<AuthorDtoResponse> authorsPage = service.readAll(pageRequest);

        authorsPage.getContent().forEach(authorDtoResponse -> {
            authorModelAssembler.addLinks(authorDtoResponse);
        });

        return new ResponseEntity<>(authorsPage, HttpStatus.OK);
    }


    @Override
    @GetMapping(value = "/{id:\\d+}")
    @ApiOperation(value = "Retrieve specific author with the supplied id", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the author with the supplied id"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    public ResponseEntity<AuthorDtoResponse> readById(@PathVariable Long id) {
        return new ResponseEntity<>(authorModelAssembler.addLinks(service.readById(id)), HttpStatus.OK);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new author", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created a new author"),
            @ApiResponse(code = 400, message = "Invalid author data"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<AuthorDtoResponse> create(
            @Valid @RequestBody
            @ApiParam(value = "Author data", required = true) AuthorDtoRequest createRequest) {
        AuthorDtoResponse authorDtoResponse = service.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(authorDtoResponse);
    }

    @Override
    @PutMapping("/{id}")
    @ApiOperation(value = "Update an existing author", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the author"),
            @ApiResponse(code = 400, message = "Invalid author data"),
            @ApiResponse(code = 404, message = "Author not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<AuthorDtoResponse> update(
            @PathVariable
            @ApiParam(value = "Author ID", required = true) Long id,
            @Valid @RequestBody
            @ApiParam(value = "Updated author data", required = true) AuthorDtoRequest updateRequest) {
        AuthorDtoRequest updatedRequest = new AuthorDtoRequest(id, updateRequest.name());
        AuthorDtoResponse authorDtoResponse = service.update(updatedRequest);
        return ResponseEntity.status(HttpStatus.OK).body(authorDtoResponse);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete an author by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the author"),
            @ApiResponse(code = 404, message = "Author not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public void deleteById(
            @PathVariable
            @ApiParam(value = "Author ID", required = true) Long id) {
        service.deleteById(id);
    }

    @GetMapping("/news/{id}/author")
    @ApiOperation(value = "Get an author by news ID", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the author"),
            @ApiResponse(code = 404, message = "Author not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<AuthorDtoResponse> readAuthorByNewsId(
            @Valid @PathVariable
            @ApiParam(value = "News ID", required = true) Long id) {
        return ResponseEntity.ok(service.readAuthorByNewsId(id));
    }
}
