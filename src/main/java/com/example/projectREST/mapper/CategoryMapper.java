package com.example.projectREST.mapper;

import com.example.projectREST.dto.CategoryDto;
import com.example.projectREST.model.CategoryEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CategoryMapper {

    public CategoryEntity toEntity(CategoryDto dto) {
        return new CategoryEntity(dto.getId(), dto.getName(), dto.getSlug(), dto.getDescription(), Instant.now(), Instant.now());
    }

}
