package com.foodygo.service;

import com.foodygo.dto.CategoryDTO;
import com.foodygo.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Override
    public Category getCategoryById(Integer categoryId) {
        return null;
    }

    @Override
    public Category getCategoryByProductId(Integer productId) {
        return null;
    }

    @Override
    public List<Category> getAllCategories() {
        return null;
    }

    @Override
    public List<Category> getAllCategoriesByRestaurantId(Integer restaurantId) {
        return null;
    }

    @Override
    public void createCategory(CategoryDTO categoryDTO) {

    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {

    }

    @Override
    public void deleteCategory(Integer categoryId) {

    }
}
