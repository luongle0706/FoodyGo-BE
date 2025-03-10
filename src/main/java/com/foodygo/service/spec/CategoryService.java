package com.foodygo.service.spec;

import com.foodygo.dto.CategoryDTO;
import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.util.List;

public interface CategoryService {

    MappingJacksonValue getAllCategories(PagingRequest request);

    Category getCategoryById(Integer categoryId);

    CategoryDTO getCategoryDTOById(Integer categoryId);

    Page<CategoryDTO> getAllCategoriesDTO(Pageable pageable);

    Page<CategoryDTO> getAllCategoriesDTOByRestaurantId(Integer restaurantId, Pageable pageable);

    Category createCategory(CategoryDTO.CategoryCreateRequest categoryDTO);

    CategoryDTO createCategoryDTO(CategoryDTO.CategoryCreateRequest categoryDTO);

    Category updateCategory(CategoryDTO.CategoryUpdateRequest categoryDTO);

    CategoryDTO updateCategoryDTO(CategoryDTO.CategoryUpdateRequest categoryDTO);

    void deleteCategory(Integer categoryId);
}
