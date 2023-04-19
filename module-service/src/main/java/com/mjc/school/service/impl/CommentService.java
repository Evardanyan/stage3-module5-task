package com.mjc.school.service.impl;

import com.mjc.school.repository.impl.CommentRepository;
import com.mjc.school.repository.impl.NewsRepository;
import com.mjc.school.repository.model.impl.CommentModel;
import com.mjc.school.repository.model.impl.NewsModel;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import com.mjc.school.service.exception.NotFoundException;
import com.mjc.school.service.exception.ServiceErrorCodeMessage;
import com.mjc.school.service.mapper.CommentModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService implements BaseService<CommentDtoRequest, CommentDtoResponse, Long> {
    private final CommentRepository commentRepository;

    private NewsRepository newsRepository;

    private CommentModelMapper mapper;

    @Autowired
    public CommentService(CommentRepository commentRepository, NewsRepository newsRepository, CommentModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.newsRepository = newsRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CommentDtoResponse> readCommentsByNewsId(Long newsId) {
        NewsModel newsModel = newsRepository.findById(newsId)
                .orElseThrow(() -> new NotFoundException(String.format(ServiceErrorCodeMessage.NEWS_ID_DOES_NOT_EXIST.getCodeMsg(), newsId)));
        List<CommentModel> commentsModels = newsModel.getComments();
        return mapper.modelListToDtoList(commentsModels);
    }


    @Override
    public Page<CommentDtoResponse> readAll(Pageable pageable) {
        Page<CommentModel> commentsPage = commentRepository.findAll(pageable);
        return commentsPage.map(commentsModel -> mapper.modelToDto(commentsModel));
    }

    @Override
    public CommentDtoResponse readById(Long id) {
        CommentModel commentModel = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ServiceErrorCodeMessage.COMMENT_ID_DOES_NOT_EXIST.getCodeMsg(), id)));
        return mapper.modelToDto(commentModel);
    }

    @Override
    public CommentDtoResponse create(CommentDtoRequest dtoRequest) {
        NewsModel news = newsRepository.findById(dtoRequest.newsId())
                .orElseThrow(() -> new NotFoundException(String.format(ServiceErrorCodeMessage.NEWS_ID_DOES_NOT_EXIST.getCodeMsg(), dtoRequest.newsId())));

        CommentModel model = mapper.dtoToModel(dtoRequest);
        model.setNews(news);

        CommentModel savedModel = commentRepository.save(model);
        return mapper.modelToDto(savedModel);
    }

    @Override
    public CommentDtoResponse update(CommentDtoRequest dtoRequest) {
        commentRepository.findById(dtoRequest.id())
                .orElseThrow(() -> new NotFoundException(String.format(ServiceErrorCodeMessage.COMMENT_ID_DOES_NOT_EXIST.getCodeMsg(), dtoRequest.id())));
        CommentModel updatedCommentModel = mapper.dtoToModel(dtoRequest);
        CommentModel savedCommentModel = commentRepository.save(updatedCommentModel);
        return mapper.modelToDto(savedCommentModel);
    }

    @Override
    public boolean deleteById(Long id) {
        commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ServiceErrorCodeMessage.COMMENT_ID_DOES_NOT_EXIST.getCodeMsg(), id)));
        commentRepository.deleteById(id);
        return true;
    }
}

