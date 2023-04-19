package com.mjc.school.service;


import com.mjc.school.repository.impl.AuthorRepository;
import com.mjc.school.repository.impl.NewsRepository;
import com.mjc.school.repository.model.impl.AuthorModel;
import com.mjc.school.repository.model.impl.NewsModel;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.exception.NotFoundException;
import com.mjc.school.service.impl.NewsService;
import com.mjc.school.service.mapper.NewsModelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private NewsModelMapper mapper;

    @InjectMocks
//    @Mock
    private NewsService newsService;

    private NewsDtoRequest newsDtoRequest;

    private NewsDtoResponse newsDtoResponse;

    private NewsModel newsModel;

    private AuthorModel authorModel;

    private List<NewsModel> newsModels;

    private List<NewsDtoResponse> expectedDtos;

    OffsetDateTime now = OffsetDateTime.now();

    @BeforeEach
    void setUp() {
        System.out.println("Executing setUp()...");

        newsDtoRequest = new NewsDtoRequest(1L, "Test News Title", "Test News Content", 1L);

        newsDtoResponse = new NewsDtoResponse(1L, "Test News Title", "Test News Content", now, 1L);

        authorModel = new AuthorModel();
        authorModel.setId(1L);
        authorModel.setName("Author name");
        authorModel.setCreateDate(now);

        AuthorModel authorSecondModel = new AuthorModel();
        authorSecondModel.setId(2L);
        authorSecondModel.setName("Author name 2");
        authorSecondModel.setCreateDate(now);


        newsModel = new NewsModel();
        newsModel.setId(1L);
        newsModel.setTitle("Test News Title");
        newsModel.setContent("Test News Content");
        newsModel.setAuthorModel(authorModel);
        newsModel.setCreateDate(now);


        newsModels = Arrays.asList(
                new NewsModel(1L, "News 1", "This is news 1", authorModel),
                new NewsModel(2L, "News 2", "This is news 2", authorSecondModel)
        );
        expectedDtos = Arrays.asList(
                new NewsDtoResponse(1L, "Test News Title", "Test News Content", now, 1L),
                new NewsDtoResponse(2L, "Test News Title 2", "Test News Content 2", now, 2L)
        );
    }

    @DisplayName("JUnit test for create method")
    @Test
    void createNews_Success() {
        when(mapper.dtoToModel(any(NewsDtoRequest.class))).thenReturn(newsModel);
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(authorModel));
        when(newsRepository.save(any(NewsModel.class))).thenReturn(newsModel);
        when(mapper.modelToDto(any(NewsModel.class))).thenReturn(newsDtoResponse);

        NewsDtoResponse result = newsService.create(newsDtoRequest);

        assertNotNull(result);
        assertEquals(newsDtoResponse.getId(), result.getId());
        assertEquals(newsDtoResponse.getTitle(), result.getTitle());
        assertEquals(newsDtoResponse.getContent(), result.getContent());
        assertEquals(newsDtoResponse.getAuthorId(), result.getAuthorId());
        assertEquals(newsDtoResponse.getCreateDate(), result.getCreateDate());

        verify(authorRepository, times(1)).findById(anyLong());
    }


    @DisplayName("JUnit test for update method")
    @Test
    void updateNews_Success() {
        Long newsId = 1L;
        Long authorId = 1L;

        when(newsRepository.findById(newsId)).thenReturn(Optional.of(newsModel));
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(authorModel));
        when(newsRepository.save(newsModel)).thenReturn(newsModel);
        when(mapper.modelToDto(newsModel)).thenReturn(newsDtoResponse);

        NewsDtoResponse result = newsService.update(newsDtoRequest);

        assertNotNull(result);
        assertEquals(newsDtoResponse.getId(), result.getId());
        assertEquals(newsDtoResponse.getTitle(), result.getTitle());
        assertEquals(newsDtoResponse.getContent(), result.getContent());
        assertEquals(newsDtoResponse.getAuthorId(), result.getAuthorId());
        assertEquals(newsDtoResponse.getCreateDate(), result.getCreateDate());

        verify(newsRepository, times(1)).findById(newsId);
        verify(authorRepository, times(1)).findById(authorId);
        verify(newsRepository, times(1)).save(newsModel);
        verify(mapper, times(1)).modelToDto(newsModel);
    }


    @Test
    @DisplayName("Should return a page of news DTOs when readAll is called with valid page request")
    void testReadAll() {
        // Arrange
        int page = 0;
        int size = 2;
        String sort = "id";
        String direction = "asc";
        Sort sortable = Sort.by(sort);
        if (direction.equalsIgnoreCase("desc")) {
            sortable = sortable.descending();
        }

        PageRequest pageRequest = PageRequest.of(page, size, sortable);

        Page<NewsModel> newsModelPage = new PageImpl<>(newsModels, pageRequest, newsModels.size());

        Page<NewsDtoResponse> expectedDtoPage = new PageImpl<>(expectedDtos, pageRequest, newsModels.size());

        when(newsRepository.findAll(pageRequest)).thenReturn(newsModelPage);
        when(mapper.modelToDto(newsModels.get(0))).thenReturn(expectedDtos.get(0));
        when(mapper.modelToDto(newsModels.get(1))).thenReturn(expectedDtos.get(1));

        Page<NewsDtoResponse> actualDtoPage = newsService.readAll(pageRequest);

        assertEquals(expectedDtoPage, actualDtoPage);
        verify(newsRepository).findAll(pageRequest);
        verify(mapper, times(2)).modelToDto(any(NewsModel.class));
    }


    @DisplayName("JUnit test for readById method")
    @Test
    void testReadById() {
        Long id = 1L;

        NewsDtoResponse expectedDto = new NewsDtoResponse(1L, "Test News Title", "Test News Content", now, 1L);
        when(newsRepository.findById(id)).thenReturn(Optional.of(newsModel));
        when(mapper.modelToDto(newsModel)).thenReturn(expectedDto);

        NewsDtoResponse actualDto = newsService.readById(id);

        assertEquals(expectedDto, actualDto);
        verify(newsRepository).findById(id);
        verify(mapper).modelToDto(newsModel);
    }

    @DisplayName("JUnit test for deleteById method")
    @Test
    void testDeleteById() {
        Long id = 1L;

        when(newsRepository.findById(id)).thenReturn(Optional.of(newsModel));
        doNothing().when(newsRepository).deleteById(id);

        boolean deleted = newsService.deleteById(id);

        assertTrue(deleted);
        verify(newsRepository, times(1)).findById(id);
        verify(newsRepository, times(1)).deleteById(id);
    }

    @DisplayName("JUnit test for deleteByIdNonExisting")
    @Test
    void testDeleteByIdNonExisting() {

        Long id = 1L;
        when(newsRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> newsService.deleteById(id));
        verify(newsRepository, never()).deleteById(id);
    }
}
