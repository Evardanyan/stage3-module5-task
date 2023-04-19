package com.mjc.school.service.impl;

import com.mjc.school.repository.impl.AuthorRepository;
import com.mjc.school.repository.impl.NewsRepository;
import com.mjc.school.repository.model.impl.AuthorModel;
import com.mjc.school.repository.model.impl.NewsModel;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.exception.NotFoundException;
import com.mjc.school.service.exception.ServiceErrorCodeMessage;
import com.mjc.school.service.mapper.AuthorModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AuthorService implements BaseService<AuthorDtoRequest, AuthorDtoResponse, Long> {

    private final AuthorRepository authorRepository;
    private final NewsRepository newsRepository;
    private final AuthorModelMapper mapper;


    public AuthorService(AuthorRepository authorRepository, NewsRepository newsRepository, AuthorModelMapper mapper) {
        this.authorRepository = authorRepository;
        this.newsRepository = newsRepository;
        this.mapper = mapper;
    }


    @Override
    public Page<AuthorDtoResponse> readAll(Pageable pageable) {
        Page<AuthorModel> authorPage = authorRepository.findAll(pageable);
        return authorPage.map(authorModel -> mapper.modelToDto(authorModel));
    }



    @Override
    public AuthorDtoResponse readById(Long id) {
        AuthorModel authorModel = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ServiceErrorCodeMessage.AUTHOR_ID_DOES_NOT_EXIST.getCodeMsg(), id)));
        return this.mapper.modelToDto(authorModel);
    }



    @Override
    public AuthorDtoResponse readAuthorByNewsId(Long newsId) {
        NewsModel newsModel = newsRepository.findById(newsId)
                .orElseThrow(() -> new NotFoundException(String.format(ServiceErrorCodeMessage.NEWS_ID_DOES_NOT_EXIST.getCodeMsg(), newsId)));
        AuthorModel authorModel = newsModel.getAuthorModel();
        return mapper.modelToDto(authorModel);
    }


    @Override
    public AuthorDtoResponse create(AuthorDtoRequest dtoRequest) {
        AuthorModel model = mapper.dtoToModel(dtoRequest);
        AuthorModel authorModel = authorRepository.save(model);
        return mapper.modelToDto(authorModel);
    }

    @Override
    public AuthorDtoResponse update(AuthorDtoRequest dtoRequest) {
        AuthorModel author = authorRepository.findById(dtoRequest.id())
                .orElseThrow(() -> new NotFoundException(String.format(ServiceErrorCodeMessage.AUTHOR_ID_DOES_NOT_EXIST.getCodeMsg(), dtoRequest.id())));
        author.setName(dtoRequest.name());
        AuthorModel updatedAuthorModel = authorRepository.save(author);
        return mapper.modelToDto(updatedAuthorModel);
    }

    @Override
    public boolean deleteById(Long id) {
        authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ServiceErrorCodeMessage.AUTHOR_ID_DOES_NOT_EXIST.getCodeMsg(), id)));
        authorRepository.deleteById(id);
        return true;
    }

}






