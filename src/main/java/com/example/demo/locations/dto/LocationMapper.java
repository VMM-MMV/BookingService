package com.example.demo.locations.dto;

import com.example.demo.locations.Location;

public class LocationMapper {

    public static LocationDTO toDto(Location location) {
        LocationDTO dto = new LocationDTO();
        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setAddress(location.getAddress());
        return dto;
    }

    public static Location toEntity(LocationDTO dto) {
        Location entity = new Location();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        return entity;
    }

    public static void updateEntityFromDto(LocationDTO dto, Location entity) {
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
    }
}

