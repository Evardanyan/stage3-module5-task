package com.mjc.school.service.mapper;

import com.mjc.school.repository.model.impl.TagModel;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagModelMapper {
    List<TagDtoResponse> modelListToDtoList(List<TagModel> var1);

    TagDtoResponse modelToDto(TagModel var1);

    @Mapping(target = "newsModel", ignore = true)
    TagModel dtoToModel(TagDtoRequest var1);

}
