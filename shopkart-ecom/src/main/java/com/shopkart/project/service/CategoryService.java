package com.shopkart.project.service;

import java.util.List;

import com.shopkart.project.model.Category;

public interface CategoryService {

	List<Category> getAllCategories();

	void createCategory(Category category);

	String deleteCategory(Long categoryId);

	Category updateCategpry(Category category, Long categoryId);
}
