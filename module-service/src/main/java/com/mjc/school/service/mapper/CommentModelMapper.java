package com.mjc.school.service.mapper;

import com.mjc.school.repository.model.impl.CommentModel;
import com.mjc.school.repository.model.impl.TagModel;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import com.mjc.school.service.dto.TagDtoRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentModelMapper {
    List<CommentDtoResponse> modelListToDtoList(List<CommentModel> var1);

    @Mapping(target = "news", source = "news")
    CommentDtoResponse modelToDto(CommentModel var1);

    @Mapping(target = "news.id", source = "newsId")
    CommentModel dtoToModel(CommentDtoRequest var1);

}
