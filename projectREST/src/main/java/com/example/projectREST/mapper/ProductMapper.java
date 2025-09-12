package com.example.projectREST.mapper;

import com.example.projectREST.dto.ProductDto;
import com.example.projectREST.model.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductEntity toEntity(ProductDto dto) {
        return new ProductEntity(dto.getId(), dto.getName(), dto.getSlug(), dto.getDescription(), dto.getPrice(), dto.getCurrency(), dto.getStock(), dto.getActive(), dto.getCreatedAt(), dto.getUpdatedAt(), dto.getCategoryId());
    }

}
