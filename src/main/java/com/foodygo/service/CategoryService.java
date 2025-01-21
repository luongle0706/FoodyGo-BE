package com.foodygo.service;

import com.foodygo.dto.CategoryDTO;
import com.foodygo.entity.Category;

import java.util.List;

public interface CategoryService {
    Category getCategoryById(Integer categoryId);
    Category getCategoryByProductId(Integer productId);
    List<Category> getAllCategories();
    List<Category> getAllCategoriesByRestaurantId(Integer restaurantId);
    void createCategory(CategoryDTO categoryDTO);
    void updateCategory(CategoryDTO categoryDTO);
    void deleteCategory(Integer categoryId);
}
