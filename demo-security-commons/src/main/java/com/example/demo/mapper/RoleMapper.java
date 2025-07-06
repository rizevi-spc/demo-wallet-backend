package com.example.demo.mapper;

import com.example.demo.dto.RoleDto;
import com.example.demo.entity.Role;
import org.mapstruct.Mapper;

/**
 * role mapper for dto entity mapping
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDto toDTO(Role role);
    Role toEntity(RoleDto roleDto);
}