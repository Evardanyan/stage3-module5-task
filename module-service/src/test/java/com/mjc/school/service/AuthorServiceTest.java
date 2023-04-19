package com.mjc.school.service;


import com.mjc.school.repository.impl.AuthorRepository;
import com.mjc.school.repository.model.impl.AuthorModel;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.exception.NotFoundException;
import com.mjc.school.service.impl.AuthorService;
import com.mjc.school.service.mapper.AuthorModelMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorModelMapper mapper;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private AuthorModel authorModel;
    private AuthorDtoResponse authorDtoResponse;

    OffsetDateTime now = OffsetDateTime.now();


    @DisplayName("JUnit test for create method")
    @Test
    void shouldCreateAuthorModelSuccessfully() throws NotFoundException {

        AuthorDtoRequest authorDto = new AuthorDtoRequest(1L, "Author Name");

        AuthorModel authorModel = new AuthorModel("Author Name");

        AuthorModel savedAuthorModel = new AuthorModel(1L, "Author Name");
        savedAuthorModel.setCreateDate(now);
        savedAuthorModel.setLastUpdatedDate(now);

        when(mapper.dtoToModel(authorDto)).thenReturn(authorModel);

        when(authorRepository.save(any(AuthorModel.class))).thenReturn(savedAuthorModel);

        when(mapper.modelToDto(savedAuthorModel)).thenReturn(new AuthorDtoResponse(1L, "Author Name", now, now));

        AuthorDtoResponse result = authorService.create(authorDto);

        assertEquals(new AuthorDtoResponse(1L, "Author Name", now, now), result);

        verify(mapper, times(1)).dtoToModel(authorDto);
        verify(authorRepository, times(1)).save(authorModel);
        verify(mapper, times(1)).modelToDto(savedAuthorModel);

    }

    @DisplayName("JUnit test for update method")
    @Test
    void shouldUpdateAuthorModelSuccessFully() throws NotFoundException {
        Long id = 1L;

        AuthorModel authorModel = new AuthorModel();
        authorModel.setId(id);
        authorModel.setName("Author Name");
        authorModel.setCreateDate(now);
        authorModel.setLastUpdatedDate(now);

        AuthorDtoRequest authorDtoRequest = new AuthorDtoRequest(id, "Author Name");

        AuthorDtoResponse authorDtoResponse = new AuthorDtoResponse(id, "Author Name", now, now);

        when(authorRepository.findById(id)).thenReturn(Optional.of(authorModel));
        when(authorRepository.save(any(AuthorModel.class))).thenReturn(authorModel);
        when(mapper.modelToDto(any(AuthorModel.class))).thenReturn(authorDtoResponse);

        AuthorDtoResponse result = authorService.update(authorDtoRequest);

        assertEquals(authorDtoResponse, result);
    }

    @DisplayName("JUnit test for readAll method")
    @Test
    void testReadAll() {
        List<AuthorModel> authorModels = new ArrayList<>();
        authorModels.add(new AuthorModel(1L, "Author Name 1"));
        authorModels.add(new AuthorModel(2L, "Author Name 2"));

        List<AuthorDtoResponse> authorDtoResponses = new ArrayList<>();
        authorDtoResponses.add(new AuthorDtoResponse(1L, "Author Name 1", now, now));
        authorDtoResponses.add(new AuthorDtoResponse(2L, "Author Name 2", now, now));

        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<AuthorModel> authorPage = new PageImpl<>(authorModels, pageable, authorModels.size());
        when(authorRepository.findAll(pageable)).thenReturn(authorPage);
        when(mapper.modelToDto(authorModels.get(0))).thenReturn(authorDtoResponses.get(0));
        when(mapper.modelToDto(authorModels.get(1))).thenReturn(authorDtoResponses.get(1));

        Page<AuthorDtoResponse> result = authorService.readAll(pageable);

        assertEquals(authorDtoResponses, result.getContent());
    }


    @DisplayName("JUnit test for readById method")
    @Test
    void readByIdTest() {
        Long id = 1L;

        AuthorModel authorModel = new AuthorModel();
        authorModel.setId(id);
        authorModel.setName("Author Name");
        authorModel.setCreateDate(now);
        authorModel.setLastUpdatedDate(now);

        AuthorDtoResponse authorDtoResponse = new AuthorDtoResponse(id, "Author Name", now, now);

        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(authorModel));
        when(mapper.modelToDto(authorModel)).thenReturn(authorDtoResponse);

        AuthorDtoResponse result = authorService.readById(1L);

        assertEquals(authorDtoResponse, result);
    }


    @DisplayName("JUnit test for deleteById method")
    @Test
    void testDeleteById() {
        Long id = 1L;
        AuthorModel authorModel = new AuthorModel();
        authorModel.setId(id);

        when(authorRepository.findById(id)).thenReturn(Optional.of(authorModel));
        doNothing().when(authorRepository).deleteById(id);

        boolean deleted = authorService.deleteById(id);

        assertTrue(deleted);
        verify(authorRepository, times(1)).findById(id);
        verify(authorRepository, times(1)).deleteById(id);
    }

    @DisplayName("JUnit test for deleteByIdNonExisting")
    @Test
    void testDeleteByIdNonExisting() {
        Long id = 1L;
        when(authorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authorService.deleteById(id));
        verify(authorRepository, times(1)).findById(id);
        verify(authorRepository, never()).deleteById(id);
    }


}
