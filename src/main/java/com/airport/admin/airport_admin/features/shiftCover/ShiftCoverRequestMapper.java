package com.airport.admin.airport_admin.features.shiftCover;

import com.airport.admin.airport_admin.features.shiftCover.dto.ShiftCoverRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShiftCoverRequestMapper {

    @Mapping(source = "originalUser.id", target = "originalUserId")
    @Mapping(source = "coveringUser.id", target = "coveringUserId")
    ShiftCoverRequestDto toDto(ShiftCoverRequest request);

    @Mapping(source = "originalUserId", target = "originalUser.id")
    @Mapping(source = "coveringUserId", target = "coveringUser.id")
    ShiftCoverRequest toEntity(ShiftCoverRequestDto dto);

    List<ShiftCoverRequestDto> toDtoList(List<ShiftCoverRequest> requests);
}
