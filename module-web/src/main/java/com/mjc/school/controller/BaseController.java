package com.mjc.school.controller;

import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.dto.TagDtoResponse;
import org.springframework.data.domain.*;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;



public interface BaseController<T, R, K> {


    default ResponseEntity<Page<R>> readAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        Sort sortable = Sort.by(sort);
        if (direction.equalsIgnoreCase("desc")) {
            sortable = sortable.descending();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(sort));
        Page<R> pageResult = new PageImpl<>(Collections.emptyList(), pageable, 0);
        return ResponseEntity.ok(pageResult);
    }


    ResponseEntity<R> readById(@Valid K id);

   default ResponseEntity<List<R>> readTagsByNewsId(@Valid K id) {
       return ResponseEntity.notFound().build();
   }
   default ResponseEntity<List<R>> readCommentsByNewsId(@Valid K id) {
       return ResponseEntity.notFound().build();
   }

   default ResponseEntity<R> readAuthorByNewsId(@Valid K id) {
        return ResponseEntity.notFound().build();
    }

    default ResponseEntity<R> getNewsByParams(@Valid String tagName,@Valid Long tagId,@Valid String authorName,@Valid String title,@Valid String content) {
        return ResponseEntity.notFound().build();
    }

    ResponseEntity<R> create(@Valid T createRequest);

    ResponseEntity<R> update(@PathVariable Long id, @Valid T updateRequest);

    void deleteById(@Valid K id);

}

