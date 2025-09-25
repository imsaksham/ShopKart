package com.shopkart.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.shopkart.project.model.Category;
import com.shopkart.project.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	private Long nextId = 1L;

	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public void createCategory(Category category) {
		category.setCategoryId(nextId++);
		categoryRepository.save(category);
	}

	@Override
	public String deleteCategory(Long categoryId) {
		List<Category> categories = categoryRepository.findAll();
		Category category = categories.stream()
									  .filter(c -> c.getCategoryId().equals(categoryId))
									  .findFirst()
									  .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

		categoryRepository.delete(category);
		return "Category with categoryId: " +categoryId + " deleted successfully !!";
	}

	@Override
	public Category updateCategpry(Category category, Long categoryId) {
		List<Category> categories = categoryRepository.findAll();
		Optional<Category> optionalCategory = categories.stream()
									   .filter(c -> c.getCategoryId().equals(categoryId))
									   .findFirst();

		if (optionalCategory.isPresent()) {
			Category existingCategory = optionalCategory.get();
			existingCategory.setCategoryName(category.getCategoryName());
			Category savedCategory = categoryRepository.save(existingCategory);
			return savedCategory;
		}
		else {
			throw new ResponseStatusException(HttpStatus.OK, "Category not found");
		}
	}

}
