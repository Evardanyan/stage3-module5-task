package com.mjc.school.service.impl;

import com.mjc.school.repository.impl.AuthorRepository;
import com.mjc.school.repository.impl.NewsRepository;
import com.mjc.school.repository.impl.TagRepository;
import com.mjc.school.repository.model.impl.AuthorModel;
import com.mjc.school.repository.model.impl.NewsModel;
import com.mjc.school.repository.model.impl.TagModel;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.exception.NotFoundException;
import com.mjc.school.service.exception.ServiceErrorCodeMessage;
import com.mjc.school.service.mapper.NewsModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class NewsService implements BaseService<NewsDtoRequest, NewsDtoResponse, Long> {

    private final NewsRepository newsRepository;

    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;
    private final NewsModelMapper mapper;

    public NewsService(NewsRepository newsRepository, AuthorRepository authorRepository, TagRepository tagRepository, NewsModelMapper mapper) {
        this.newsRepository = newsRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<NewsDtoResponse> readAll(Pageable pageable) {
        Page<NewsModel> newsPage = newsRepository.findAll(pageable);
        return newsPage.map(newsModel -> mapper.modelToDto(newsModel));
    }


    @Override
    public NewsDtoResponse readById(Long id) {
        NewsModel newsModel = newsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(ServiceErrorCodeMessage.NEWS_ID_DOES_NOT_EXIST.getCodeMsg(), id)
                ));
        return mapper.modelToDto(newsModel);
    }

    @Override
    public NewsDtoResponse create(NewsDtoRequest dtoRequest) {
        authorRepository.findById(dtoRequest.authorId())
                .orElseThrow(() -> new NotFoundException(
                        String.format(ServiceErrorCodeMessage.AUTHOR_ID_DOES_NOT_EXIST.getCodeMsg(), dtoRequest.authorId())
                ));

        NewsModel model = mapper.dtoToModel(dtoRequest);

        if (dtoRequest.tagId() != null) {
            TagModel tagModel = tagRepository.findById(dtoRequest.tagId())
                    .orElseThrow(() -> new NotFoundException("Tag not found with the given ID"));
            model.setTagModels(Collections.singletonList(tagModel));
        }

        NewsModel newsModel = newsRepository.save(model);
        return mapper.modelToDto(newsModel);
    }

//    @Override
//    public NewsDtoResponse update(NewsDtoRequest dtoRequest) {
//        NewsModel existingNewsModel = newsRepository.findById(dtoRequest.id())
//                .orElseThrow(() -> new NotFoundException(
//                        String.format(ServiceErrorCodeMessage.NEWS_ID_DOES_NOT_EXIST.getCodeMsg(), dtoRequest.id())
//                ));
//        existingNewsModel.setTitle(dtoRequest.title());
//        existingNewsModel.setContent(dtoRequest.content());
//
//        if (dtoRequest.authorId() != null) {
//            AuthorModel authorModel = authorRepository.findById(dtoRequest.authorId())
//                    .orElseThrow(() -> new NotFoundException(
//                            String.format(ServiceErrorCodeMessage.AUTHOR_ID_DOES_NOT_EXIST.getCodeMsg(), dtoRequest.authorId())
//                    ));
//            existingNewsModel.setAuthorModel(authorModel);
//        }
//        if (dtoRequest.tagId() != null) {
//            TagModel tagModel = tagRepository.findById(dtoRequest.tagId())
//                    .orElseThrow(() -> new NotFoundException(
//                            String.format(ServiceErrorCodeMessage.TAG_ID_DOES_NOT_EXIST.getCodeMsg(), dtoRequest.tagId())
//                    ));
//            List<TagModel> tagModels = existingNewsModel.getTagModels();
//            tagModels.add(tagModel);
//            existingNewsModel.setTagModels(tagModels);
//        }
//        NewsModel updatedNewsModel = newsRepository.save(existingNewsModel);
//        return mapper.modelToDto(updatedNewsModel);
//    }

    @Override
    public NewsDtoResponse update(NewsDtoRequest dtoRequest) {
        NewsModel existingNewsModel = newsRepository.findById(dtoRequest.id())
                .orElseThrow(() -> new NotFoundException(
                        String.format(ServiceErrorCodeMessage.NEWS_ID_DOES_NOT_EXIST.getCodeMsg(), dtoRequest.id())
                ));
        existingNewsModel.setTitle(dtoRequest.title());
        existingNewsModel.setContent(dtoRequest.content());

        if (dtoRequest.authorId() != null) {
            AuthorModel authorModel = authorRepository.findById(dtoRequest.authorId())
                    .orElseThrow(() -> new NotFoundException(
                            String.format(ServiceErrorCodeMessage.AUTHOR_ID_DOES_NOT_EXIST.getCodeMsg(), dtoRequest.authorId())
                    ));
            existingNewsModel.setAuthorModel(authorModel);
        }
        if (dtoRequest.tagId() != null) {
            TagModel tagModel = tagRepository.findById(dtoRequest.tagId())
                    .orElseThrow(() -> new NotFoundException(
                            String.format(ServiceErrorCodeMessage.TAG_ID_DOES_NOT_EXIST.getCodeMsg(), dtoRequest.tagId())
                    ));
            List<TagModel> tagModels = existingNewsModel.getTagModels();
            boolean tagAlreadyPresent = tagModels.stream().anyMatch(existingTag -> existingTag.getId().equals(tagModel.getId()));
            if (!tagAlreadyPresent) {
                tagModels.add(tagModel);
            }

            existingNewsModel.setTagModels(tagModels);
        }
        NewsModel updatedNewsModel = newsRepository.save(existingNewsModel);
        return mapper.modelToDto(updatedNewsModel);
    }



    @Override
    public boolean deleteById(Long newsId) {
        newsRepository.findById(newsId)
                .orElseThrow(() -> new NotFoundException(String.format(ServiceErrorCodeMessage.NEWS_ID_DOES_NOT_EXIST.getCodeMsg(), newsId)));
        newsRepository.deleteById(newsId);
        return true;
    }

    public NewsDtoResponse getNewsByParams(String tagNames, Long tagIds, String authorName, String title, String content) {
        List<NewsModel> newsModels = newsRepository.getNewsByParams(
                tagNames == null ? null : Arrays.asList(tagNames.split(",")),
                tagIds == null ? null : Collections.singletonList(tagIds),
                authorName,
                title,
                content);
        if (newsModels.isEmpty()) {
            throw new NotFoundException("No news items found with the specified criteria.");
        }
        return this.mapper.modelToDto(newsModels.get(0));
    }

}
