package com.foodygo.service;

import com.foodygo.dto.CategoryDTO;
import com.foodygo.entity.Category;
import com.foodygo.entity.Restaurant;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.mapper.CategoryMapper;
import com.foodygo.repository.CategoryRepository;
import com.foodygo.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final RestaurantRepository restaurantRepository;

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
            throw new ElementNotFoundException("Category not found witht id " + categoryId);
        }
        return CategoryMapper.INSTANCE.toDTO(category);
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository
                .findByNameIgnoreCaseAndDeletedFalse(name)
                .orElse(null);
    }

    @Override
    public CategoryDTO getCategoryDTOByName(String name) {
        Category category = getCategoryByName(name);
        if (category == null) {
            throw new ElementNotFoundException("Category not found witht name " + name);
        }
        return CategoryMapper.INSTANCE.toDTO(category);
    }

    @Override
    public List<Category> getCategoriesContainsName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Page<CategoryDTO> getCategoriesDTOContainsName(String name, Pageable pageable) {
        return categoryRepository.findByNameContainingIgnoreCase(name, pageable).map(CategoryMapper.INSTANCE::toDTO);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findByDeletedFalse();
    }

    @Override
    public Page<CategoryDTO> getAllCategoriesDTO(Pageable pageable) {
        return categoryRepository.findByDeletedFalse(pageable).map(CategoryMapper.INSTANCE::toDTO);
    }

    @Override
    public List<Category> getAllCategoriesByRestaurantId(Integer restaurantId) {
        return categoryRepository.findByRestaurantIdAndDeletedFalse(restaurantId);
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
