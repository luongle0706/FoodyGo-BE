package com.foodygo.service.spec;

import com.foodygo.dto.CategoryDTO;
import com.foodygo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    Category getCategoryById(Integer categoryId);
    CategoryDTO getCategoryDTOById(Integer categoryId);

    Category getCategoryByName(String name);
    CategoryDTO getCategoryDTOByName(String name);

    List<Category> getCategoriesContainsName(String name);
    Page<CategoryDTO> getCategoriesDTOContainsName(String name, Pageable pageable);

    List<Category> getAllCategories();
    Page<CategoryDTO> getAllCategoriesDTO(Pageable pageable);

    List<Category> getAllCategoriesByRestaurantId(Integer restaurantId);
    Page<CategoryDTO> getAllCategoriesDTOByRestaurantId(Integer restaurantId, Pageable pageable);

    Category createCategory(CategoryDTO.CategoryCreateRequest categoryDTO);
    CategoryDTO createCategoryDTO(CategoryDTO.CategoryCreateRequest categoryDTO);

    Category updateCategory(CategoryDTO.CategoryUpdateRequest categoryDTO);
    CategoryDTO updateCategoryDTO(CategoryDTO.CategoryUpdateRequest categoryDTO);

    void deleteCategory(Integer categoryId);

}
