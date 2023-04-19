package com.mjc.school.service;


import com.mjc.school.repository.impl.NewsRepository;
import com.mjc.school.repository.impl.TagRepository;
import com.mjc.school.repository.model.impl.AuthorModel;
import com.mjc.school.repository.model.impl.NewsModel;
import com.mjc.school.repository.model.impl.TagModel;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.service.exception.NotFoundException;
import com.mjc.school.service.impl.TagService;
import com.mjc.school.service.mapper.TagModelMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagModelMapper mapper;

    @Mock
    private TagRepository tagRepository;


    @Mock
    private NewsRepository newsRepository;
    @InjectMocks
    private TagService tagService;

    private AuthorModel authorModel;
    private AuthorDtoResponse authorDtoResponse;

    OffsetDateTime now = OffsetDateTime.now();


    @DisplayName("JUnit test for create method")
    @Test
    void shouldCreateAuthorModelSuccessfully() throws NotFoundException {

        TagDtoRequest tagDto = new TagDtoRequest(1L, "Tag Name");

        TagModel tagModel = new TagModel(1L, "Tag Name");

        TagModel savedTagModel = new TagModel(1L, "Tag Name");


        when(mapper.dtoToModel(tagDto)).thenReturn(tagModel);
        when(tagRepository.save(any(TagModel.class))).thenReturn(savedTagModel);
        when(mapper.modelToDto(savedTagModel)).thenReturn(new TagDtoResponse(1L, "Tag Name"));

        TagDtoResponse result = tagService.create(tagDto);

        assertEquals(new TagDtoResponse(1L, "Tag Name"), result);

        verify(mapper, times(1)).dtoToModel(tagDto);
        verify(tagRepository, times(1)).save(tagModel);
        verify(mapper, times(1)).modelToDto(savedTagModel);

    }


    @DisplayName("JUnit test for update method")
    @Test
    void shouldUpdateAuthorModelSuccessFully() throws NotFoundException {
        Long id = 1L;

        TagModel tagModel = new TagModel();
        tagModel.setId(id);
        tagModel.setName("Tag Name");


        TagDtoRequest tagDtoRequest = new TagDtoRequest(id, "Tag Name");

        TagDtoResponse tagDtoResponse = new TagDtoResponse(id, "Tag Name");

        when(tagRepository.findById(id)).thenReturn(Optional.of(tagModel));
        when(mapper.dtoToModel(tagDtoRequest)).thenReturn(tagModel);
        when(tagRepository.save(any(TagModel.class))).thenReturn(tagModel);
        when(mapper.modelToDto(any(TagModel.class))).thenReturn(tagDtoResponse);

        TagDtoResponse result = tagService.update(tagDtoRequest);

        assertEquals(tagDtoResponse, result);
    }


    @DisplayName("JUnit test for readAll method")
    @Test
    void testReadAll() {
        List<TagModel> tagModels = new ArrayList<>();
        tagModels.add(new TagModel(1L, "Tag name 1"));
        tagModels.add(new TagModel(2L, "Tag name 2"));

        List<TagDtoResponse> tagDtoResponses = new ArrayList<>();
        tagDtoResponses.add(new TagDtoResponse(1L, "Tag name 1"));
        tagDtoResponses.add(new TagDtoResponse(2L, "Tag name 2"));

        Pageable pageable = PageRequest.of(0, 10);

        Page<TagModel> tagsPage = new PageImpl<>(tagModels, pageable, tagModels.size());

        when(tagRepository.findAll(pageable)).thenReturn(tagsPage);
        when(mapper.modelToDto(tagModels.get(0))).thenReturn(tagDtoResponses.get(0));
        when(mapper.modelToDto(tagModels.get(1))).thenReturn(tagDtoResponses.get(1));

        Page<TagDtoResponse> result = tagService.readAll(pageable);

        assertEquals(tagDtoResponses, result.getContent());
        assertEquals(tagModels.size(), result.getTotalElements());
        assertEquals(1, result.getTotalPages());
    }


    @DisplayName("JUnit test for readById method")
    @Test
    void readByIdTest() {
        Long id = 1L;

        TagModel tagModel = new TagModel();
        tagModel.setId(id);
        tagModel.setName("Tag Name");

        TagDtoResponse tagDtoResponse = new TagDtoResponse(id, "Tag Name");

        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(tagModel));
        when(mapper.modelToDto(tagModel)).thenReturn(tagDtoResponse);


        TagDtoResponse result = tagService.readById(1L);

        assertEquals(tagDtoResponse, result);
    }


    @DisplayName("JUnit test for deleteById method")
    @Test
    void testDeleteById() {
        Long id = 1L;

        TagModel exampleOne = new TagModel(2L, "test1234");
        TagModel exampleTwo = new TagModel(3L, "test1234");
        TagModel exampleThree = new TagModel(4L, "test1234");

        List<TagModel> tagList = new ArrayList<>();
        tagList.add(exampleOne);
        tagList.add(exampleTwo);
        tagList.add(exampleThree);

        authorModel = new AuthorModel(1L, "TestTest");

        List<NewsModel> newsList = new ArrayList<>();
        NewsModel newsModel = new NewsModel();
        newsModel.setId(1L);
        newsModel.setTitle("Fake News Title");
        newsModel.setContent("Fake News Content");
        newsModel.setCreateDate(now);
        newsModel.setLastUpdatedDate(now);
        newsModel.setAuthorModel(authorModel);
        newsModel.setTagModels(tagList);
        newsList.add(newsModel);


        TagModel tagModel = new TagModel();
        tagModel.setId(1L);
        tagModel.setName("Tag Name");
        tagModel.setNewsModel(newsList);

        when(tagRepository.findById(id)).thenReturn(Optional.of(tagModel));
        when(newsRepository.save(any())).thenReturn(newsModel);
        doNothing().when(tagRepository).deleteById(id);


        boolean deleted = tagService.deleteById(id);

        assertTrue(deleted);
        verify(tagRepository, times(1)).findById(id);
        verify(tagRepository, times(1)).deleteById(id);
    }







    @DisplayName("JUnit test for deleteByIdNonExisting")
    @Test
    void testDeleteByIdNonExisting() {

        Long id = 1L;

        when(tagRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> tagService.deleteById(id));
        verify(tagRepository, never()).deleteById(id);
    }

}
