package com.shopkart.project.service;

import java.util.Map;

import com.shopkart.project.payload.CategoryDTO;
import com.shopkart.project.payload.CategoryResponse;

public interface CategoryService {

	CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

	CategoryDTO createCategory(Map<String, Object> requestBody);

	CategoryDTO updateCategory(Map<String, Object> requestBody, Long categoryId);

	CategoryDTO deleteCategory(Long categoryId);
}
