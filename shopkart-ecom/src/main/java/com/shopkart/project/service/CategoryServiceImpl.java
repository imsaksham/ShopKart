package com.shopkart.project.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.shopkart.project.exceptions.ServiceException;
import com.shopkart.project.model.Category;
import com.shopkart.project.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	// private Long nextId = 1L;

	public Category mapToCategory(Map<String, Object> requestBody) {
		Category category = new Category();
		category.setCategoryName((String) requestBody.get("categoryName"));

		return category;
	}

	@Override
	public List<Category> getAllCategories() {
		try {
			return categoryRepository.findAll();
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	public void createCategory(Map<String, Object> requestBody) {
		try {
			// category.setCategoryId(nextId++);
			Category category = mapToCategory(requestBody);
			categoryRepository.save(category);
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	public String deleteCategory(Long categoryId) {
		try {
			Category category = categoryRepository.findById(categoryId)
					.orElseThrow(() -> new ServiceException(true, 404, "Category Id not found"));

			categoryRepository.delete(category);
			return "Category with categoryId: " +categoryId + " deleted successfully !!";
		} catch (Exception ex) {
			if (ex instanceof ServiceException) {
				throw new ServiceException(true, 404, ex.getLocalizedMessage());
			}

			throw ex;
		}
	}

	@Override
	public Category updateCategpry(Map<String, Object> requestBody, Long categoryId) {
		try {
			Category savedCategory = categoryRepository.findById(categoryId)
					  .orElseThrow(() -> new ServiceException(true, 404, "Category not found"));

			if (requestBody.get("categoryName") != null) {
				savedCategory.setCategoryName(requestBody.get("categoryName").toString());
			}

			return categoryRepository.save(savedCategory);
		} catch (Exception ex) {
			if (ex instanceof ServiceException) {
				throw new ServiceException(true, 404, ex.getLocalizedMessage());
			}
			throw ex;
		}
	}

}
