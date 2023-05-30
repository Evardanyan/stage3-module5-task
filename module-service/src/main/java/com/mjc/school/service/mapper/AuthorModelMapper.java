package com.mjc.school.service.mapper;


import com.mjc.school.repository.model.impl.AuthorModel;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;


@Mapper(componentModel = "spring")
public interface AuthorModelMapper {

    List<AuthorDtoResponse> modelListToDtoList(List<AuthorModel> var1);

    AuthorDtoResponse modelToDto(AuthorModel var1);

    @Mappings(value = {@Mapping(target = "createDate", ignore = true), @Mapping(target = "lastUpdatedDate", ignore = true),
            @Mapping(target = "id", ignore = true), @Mapping(target = "name", source = "name")})
    @Named("modelAuthorNameToDto")
    AuthorDtoResponse modelAuthorNameToDto(AuthorModel var1);

    @Mappings(value = {@Mapping(target = "createDate", ignore = true), @Mapping(target = "lastUpdatedDate", ignore = true)})
    AuthorModel dtoToModel(AuthorDtoRequest var1);


}
