package com.mjc.school.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

public interface BaseService<T, R, K> {
//    List<R> readAll();


    Page<R> readAll(Pageable pageable);
    R readById(K id);

    R create(T createRequest);

    R update(T updateRequest);

    boolean deleteById(K id);

    default PageRequest buildPageRequest(int page, int size, String sort, String direction) {
        Sort sortable = Sort.by(sort);
        if (direction.equalsIgnoreCase("desc")) {
            sortable = sortable.descending();
        }
        return PageRequest.of(page, size, sortable);
    }

   default List<R> readTagsByNewsId(K id) {
       return Collections.emptyList();
   }
   default List<R> readCommentsByNewsId(K id) {
       return Collections.emptyList();
   }

   default R readAuthorByNewsId(K id) {
       return null;
    }

   default R getNewsByParams(String tagName, Long tagId, String authorName, String title, String content) {
       return null;
   }
}
