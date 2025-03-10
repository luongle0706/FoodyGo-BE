package com.foodygo.service.impl;

import com.foodygo.dto.CategoryDTO;
import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.dto.paging.CategoryPagingResponse;
import com.foodygo.entity.Category;
import com.foodygo.entity.Restaurant;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.mapper.CategoryMapper;
import com.foodygo.repository.CategoryRepository;
import com.foodygo.repository.RestaurantRepository;
import com.foodygo.service.spec.CategoryService;
import com.foodygo.utils.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public MappingJacksonValue getAllCategories(PagingRequest request) {
        Pageable pageable = PaginationUtil.getPageable(request);
        Specification<Category> specification = CategoryPagingResponse.filterByFields(request.getFilters());
        Page<Category> page = categoryRepository.findAll(specification, pageable);
        List<CategoryPagingResponse> mappedDTO = page.getContent().stream().map(CategoryPagingResponse::fromEntity).toList();
        return PaginationUtil.getPagedMappingJacksonValue(request, page, mappedDTO, "Get all categories");
    }

    @Override
    public Category getCategoryById(Integer categoryId) {
        return categoryRepository
                .findByIdAndDeletedFalse(categoryId)
                .orElse(null);
    }

    @Override
    public CategoryDTO getCategoryDTOById(Integer categoryId) {
        Category category = getCategoryById(categoryId);
        if (category == null) {
            throw new ElementNotFoundException("Category not found with id " + categoryId);
        }
        return CategoryMapper.INSTANCE.toDTO(category);
    }

    @Override
    public Page<CategoryDTO> getAllCategoriesDTO(Pageable pageable) {
        return categoryRepository.findByDeletedFalse(pageable).map(CategoryMapper.INSTANCE::toDTO);
    }

    @Override
    public Page<CategoryDTO> getAllCategoriesDTOByRestaurantId(Integer restaurantId, Pageable pageable) {
        return categoryRepository.findByRestaurantIdAndDeletedFalse(restaurantId, pageable).map(CategoryMapper.INSTANCE::toDTO);
    }

    @Override
    @Transactional
    public Category createCategory(CategoryDTO.CategoryCreateRequest categoryDTO) {
        Restaurant restaurant = restaurantRepository.findByIdAndDeletedFalse(categoryDTO.restaurantId())
                .orElseThrow(() -> new ElementNotFoundException("Restaurant not found with id " + categoryDTO.restaurantId()));
        Category category = Category.builder()
                .name(categoryDTO.name())
                .description(categoryDTO.description())
                .restaurant(restaurant)
                .build();
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public CategoryDTO createCategoryDTO(CategoryDTO.CategoryCreateRequest categoryDTO) {
        return CategoryMapper.INSTANCE.toDTO(createCategory(categoryDTO));
    }

    @Override
    @Transactional
    public Category updateCategory(CategoryDTO.CategoryUpdateRequest categoryDTO) {
        Category category = getCategoryById(categoryDTO.id());
        if (category == null) {
            throw new ElementNotFoundException("Category not found with id " + categoryDTO.id());
        }
        category.setName(categoryDTO.name());
        category.setDescription(categoryDTO.description());
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategoryDTO(CategoryDTO.CategoryUpdateRequest categoryDTO) {
        return CategoryMapper.INSTANCE.toDTO(updateCategory(categoryDTO));
    }

    @Override
    @Transactional
    public void deleteCategory(Integer categoryId) {
        Category category = getCategoryById(categoryId);
        if (category == null) {
            throw new ElementNotFoundException("Category not found with id " + categoryId);
        }
        category.setDeleted(true);
        categoryRepository.save(category);
    }

}
