package com.test.demo.mapStruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(uses = DateFormtUtil.class)
public interface PersonTestMapper {

    PersonTestMapper INSTANCT = Mappers.getMapper(PersonTestMapper.class);

    @Mapping(target = "personName", source = "name")
    @Mapping(target = "id", ignore = true)
    //  @Mapping(target = "createTime", source = "createTime", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "createTime", source = "createTime", qualifiedBy = DateFormtUtil.DateFormat.class)
    PersonTestDto conver(PersonTest person);

}
