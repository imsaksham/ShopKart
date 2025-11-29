package com.shopkart.project.service;

import java.util.List;
import java.util.Map;

import com.shopkart.project.model.Category;

public interface CategoryService {

	List<Category> getAllCategories();

	void createCategory(Map<String, Object> requestBody);

	String deleteCategory(Long categoryId);

	Category updateCategpry(Category category, Long categoryId);
}
