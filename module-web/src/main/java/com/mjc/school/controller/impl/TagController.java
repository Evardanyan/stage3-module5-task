package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.util.NewsModelAssembler;
import com.mjc.school.controller.util.TagModelAssembler;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
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
@RequestMapping(value = "/api/v1/tags")
@Validated
@Api(tags = "Tag Management", produces = "application/json", value = "Operations for creating, updating, retrieving and deleting tag in the application")
public class TagController implements BaseController<TagDtoRequest, TagDtoResponse, Long> {


    @Autowired
    private TagModelAssembler tagModelAssembler;
    private final BaseService<TagDtoRequest, TagDtoResponse, Long> service;

    public TagController(BaseService<TagDtoRequest, TagDtoResponse, Long> service) {
        this.service = service;
    }


    @Override
    @GetMapping
    @ApiOperation(value = "Get a list of all tags", response = TagDtoResponse.class, responseContainer = "Page")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the list of tags"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Page<TagDtoResponse>> readAll(
            @RequestParam(value = "page", defaultValue = "0")
            @ApiParam(value = "Page number of the results", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10")
            @ApiParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "name")
            @ApiParam(value = "Sort field", defaultValue = "name") String sort,
            @RequestParam(value = "direction", defaultValue = "asc")
            @ApiParam(value = "Sort direction", defaultValue = "asc") String direction) {

        PageRequest pageRequest = service.buildPageRequest(page, size, sort, direction);

        Page<TagDtoResponse> tagsPage = service.readAll(pageRequest);

        tagsPage.getContent().forEach(authorDtoResponse -> {
            tagModelAssembler.addLinks(authorDtoResponse);
        });
        return new ResponseEntity<>(tagsPage, HttpStatus.OK);
    }

    @Override
    @GetMapping(value = "/{id:\\d+}")
    @ApiOperation(value = "Retrieve specific tag with the supplied id", response = TagDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the news with the supplied id"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    public ResponseEntity<TagDtoResponse> readById(@Valid @PathVariable Long id) {
        return new ResponseEntity<>(tagModelAssembler.addLinks(service.readById(id)), HttpStatus.OK);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new tag", response = TagDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created a new tag"),
            @ApiResponse(code = 400, message = "Invalid tag data"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<TagDtoResponse> create(
            @Valid @RequestBody
            @ApiParam(value = "Tag data", readOnly = true) TagDtoRequest createRequest) {
        TagDtoResponse tagDtoResponse = service.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(tagDtoResponse);
    }

    @Override
    @PatchMapping("/{id}")
    @ApiOperation(value = "Update an existing tag", response = TagDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the tag"),
            @ApiResponse(code = 400, message = "Invalid tag data"),
            @ApiResponse(code = 404, message = "Tag not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<TagDtoResponse> update(
            @PathVariable
            @ApiParam(value = "News ID", readOnly = true) Long id,
            @Valid @RequestBody
            @ApiParam(value = "Updated news data", required = true) TagDtoRequest updateRequest) {
        TagDtoRequest updatedRequest = new TagDtoRequest(id, updateRequest.name());
        TagDtoResponse tagDtoResponse = service.update(updatedRequest);
        return ResponseEntity.status(HttpStatus.OK).body(tagDtoResponse);
    }


    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a tag by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the tag"),
            @ApiResponse(code = 404, message = "tag not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }

    @GetMapping("/news/{id}/tag")
    @ApiOperation(value = "Get tag by news Id", response = TagDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved tag"),
            @ApiResponse(code = 404, message = "No tag articles found")
    })
    public ResponseEntity<List<TagDtoResponse>> readTagsByNewsId(@Valid @PathVariable Long id) {
        List<TagDtoResponse> tagsByNewsId = service.readTagsByNewsId(id);
        return ResponseEntity.ok(tagsByNewsId);
    }

}
