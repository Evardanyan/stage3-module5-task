package com.mjc.school.service;


import com.mjc.school.repository.impl.CommentRepository;
import com.mjc.school.repository.impl.NewsRepository;
import com.mjc.school.repository.model.impl.CommentModel;
import com.mjc.school.repository.model.impl.NewsModel;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import com.mjc.school.service.exception.NotFoundException;
import com.mjc.school.service.impl.CommentService;
import com.mjc.school.service.mapper.CommentModelMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentModelMapper mapper;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private CommentService commentService;


    @DisplayName("JUnit test for create method")
    @Test
    void shouldCreateCommentModelSuccessfully() throws NotFoundException {

        NewsModel newsModel = new NewsModel();
        CommentDtoRequest commentDtoRequest = new CommentDtoRequest(1L, "New Comment", 1L);
        CommentModel commentModel = new CommentModel(1L, "New Comment", newsModel);
        CommentModel savedCommentModel = new CommentModel(1L, "New Comment", newsModel);

        when(mapper.dtoToModel(commentDtoRequest)).thenReturn(commentModel);
        when(newsRepository.findById(1L)).thenReturn(Optional.of(newsModel));
        when(commentRepository.save(commentModel)).thenReturn(savedCommentModel);
        when(mapper.modelToDto(savedCommentModel)).thenReturn(new CommentDtoResponse(1L, "New Comment", newsModel));

        CommentDtoResponse result = commentService.create(commentDtoRequest);

        assertEquals(new CommentDtoResponse(1L, "New Comment", newsModel), result);
        verify(mapper, times(1)).dtoToModel(commentDtoRequest);
        verify(commentRepository, times(1)).save(commentModel);
        verify(mapper, times(1)).modelToDto(savedCommentModel);
    }


    @DisplayName("JUnit test for update method")
    @Test
    void shouldUpdateAuthorModelSuccessFully() throws NotFoundException {
        Long id = 1L;

        NewsModel newsModel = new NewsModel();

        CommentModel commentModel = new CommentModel();
        commentModel.setId(id);
        commentModel.setContent("News Comments");
        commentModel.setNews(newsModel);

        System.out.println(commentModel);

        CommentDtoRequest commentDtoRequest = new CommentDtoRequest(id, "News Comments", id);

        CommentDtoResponse commentDtoResponse = new CommentDtoResponse(id, "News Comments", newsModel);

        when(commentRepository.findById(id)).thenReturn(Optional.of(commentModel));
        when(mapper.dtoToModel(commentDtoRequest)).thenReturn(commentModel);
        when(commentRepository.save(any(CommentModel.class))).thenReturn(commentModel);
        when(mapper.modelToDto(any(CommentModel.class))).thenReturn(commentDtoResponse);

        CommentDtoResponse result = commentService.update(commentDtoRequest);

        assertEquals(commentDtoResponse, result);
    }

    @DisplayName("JUnit test for readAll method")
    @Test
    void testReadAll() {

        NewsModel newsModel = new NewsModel();
        List<CommentModel> comments = new ArrayList<>();
        comments.add(new CommentModel(1L, "Comment Name 1", newsModel));
        comments.add(new CommentModel(2L, "Comment Name 2", newsModel));

        List<CommentDtoResponse> commentDtoResponses = new ArrayList<>();
        commentDtoResponses.add(new CommentDtoResponse(1L, "Comment Name 1", newsModel));
        commentDtoResponses.add(new CommentDtoResponse(2L, "Comment Name 2", newsModel));

        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<CommentModel> commentPage = new PageImpl<>(comments, pageable, comments.size());
        when(commentRepository.findAll(pageable)).thenReturn(commentPage);
        when(mapper.modelToDto(comments.get(0))).thenReturn(commentDtoResponses.get(0));
        when(mapper.modelToDto(comments.get(1))).thenReturn(commentDtoResponses.get(1));

        Page<CommentDtoResponse> result = commentService.readAll(pageable);

        assertEquals(commentDtoResponses, result.getContent());
    }


    @DisplayName("JUnit test for readById method")
    @Test
    void readByIdTest() {
        Long id = 1L;

        NewsModel newsModel = new NewsModel();

        CommentModel commentModel = new CommentModel();
        commentModel.setId(id);
        commentModel.setContent("Comment Name");
        commentModel.setNews(newsModel);


        CommentDtoResponse commentDtoResponse = new CommentDtoResponse(id, "Comment Name", newsModel);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentModel));
        when(mapper.modelToDto(commentModel)).thenReturn(commentDtoResponse);

        CommentDtoResponse result = commentService.readById(1L);

        verify(commentRepository, times(1)).findById(id);
        assertEquals(commentDtoResponse, result);
    }


    @DisplayName("JUnit test for deleteById method")
    @Test
    void testDeleteById() {
        Long id = 1L;
        CommentModel commentModel = new CommentModel();
        commentModel.setId(id);

        when(commentRepository.findById(id)).thenReturn(Optional.of(commentModel));
        doNothing().when(commentRepository).deleteById(id);

        boolean deleted = commentService.deleteById(id);

        assertTrue(deleted);
        verify(commentRepository, times(1)).findById(id);
        verify(commentRepository, times(1)).deleteById(id);
    }

    @DisplayName("JUnit test for deleteByIdNonExisting")
    @Test
    void testDeleteByIdNonExisting() {
        Long id = 1L;
        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.deleteById(id));
        verify(commentRepository, times(1)).findById(id);
        verify(commentRepository, never()).deleteById(id);
    }


}
