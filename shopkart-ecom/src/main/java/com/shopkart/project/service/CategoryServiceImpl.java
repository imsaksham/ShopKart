package com.shopkart.project.service;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopkart.project.config.Constant;
import com.shopkart.project.exceptions.ServiceException;
import com.shopkart.project.model.Category;
import com.shopkart.project.payload.CategoryDTO;
import com.shopkart.project.payload.CategoryResponse;
import com.shopkart.project.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private final ModelMapper modelMapper;

	public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
		this.categoryRepository = categoryRepository;
		this.modelMapper = new ModelMapper();
	}

	public Category mapToCategory(Map<String, Object> requestBody) {
		Category category = new Category();
		category.setCategoryName((String) requestBody.get("categoryName"));

		return category;
	}

	private CategoryDTO convertToCategoryDto(Category category) {
		return modelMapper.map(category, CategoryDTO.class);
	}

	private CategoryResponse mapToCategoryResponse(List<CategoryDTO> result, Page<Category> categoryPage) {
		CategoryResponse categoryResponse = new CategoryResponse();
		categoryResponse.setContent(result);
		categoryResponse.setPageNumber(categoryPage.getNumber());
		categoryResponse.setPageSize(categoryPage.getSize());
		categoryResponse.setTotalAvailableRecords(categoryPage.getTotalElements());
		categoryResponse.setTotalPages(categoryPage.getTotalPages());
		categoryResponse.setLastPage(categoryPage.isLast());

		return categoryResponse;
	}

	@Override
	public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		try {
			Sort sortByAndOrder = Constant.SORT_DIRECTION.equalsIgnoreCase(sortOrder) 
								? Sort.by(sortBy).ascending() 
								: Sort.by(sortBy).descending();

			Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
			Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

			List<Category> categories = categoryPage.getContent();

			if (categories.isEmpty()) {
				throw new ServiceException(true, 401, "No category created till now");
			}

			List<CategoryDTO> result = categories.stream()
												 .map(category -> this.convertToCategoryDto(category))
												 .toList();

			return mapToCategoryResponse(result, categoryPage);
		} catch (ServiceException ex) {
			throw new ServiceException(true, ex.getErrorCode(), ex.getLocalizedMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	public CategoryDTO createCategory(Map<String, Object> requestBody) {
		try {
			// category.setCategoryId(nextId++);
			Category isCategoryAvailable = categoryRepository.findByCategoryNameIgnoreCase(requestBody.get("categoryName").toString());

			if (isCategoryAvailable != null) {
				throw new ServiceException(true, 401, "Category with the name '" +requestBody.get("categoryName") + "' already exist!!!");
			}
			Category category = mapToCategory(requestBody);
			Category savedCategory = categoryRepository.save(category);

			CategoryDTO savedCategoryDto = this.convertToCategoryDto(savedCategory);
			return savedCategoryDto;
		} catch (ServiceException ex) {
			throw new ServiceException(true, ex.getErrorCode(), ex.getLocalizedMessage());
		}  catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	public CategoryDTO updateCategory(Map<String, Object> requestBody, Long categoryId) {
		try {
			Category savedCategory = categoryRepository.findById(categoryId)
					  .orElseThrow(() -> new ServiceException(true, 404, "Category not found"));

			if (requestBody.get("categoryName") != null) {
				savedCategory.setCategoryName(requestBody.get("categoryName").toString());
			}

			Category updatedCategory = categoryRepository.save(savedCategory);
			CategoryDTO updatedCategoryDto = this.convertToCategoryDto(updatedCategory);

			return updatedCategoryDto;
		} catch (ServiceException ex) {
			throw new ServiceException(true, ex.getErrorCode(), ex.getLocalizedMessage());
		}  catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	public CategoryDTO deleteCategory(Long categoryId) {
		try {
			Category category = categoryRepository.findById(categoryId)
					.orElseThrow(() -> new ServiceException(true, 404, "Category Id not found"));

			categoryRepository.delete(category);
			CategoryDTO deletedCategoryDto = this.convertToCategoryDto(category);
			return deletedCategoryDto;
		} catch (Exception ex) {
			if (ex instanceof ServiceException) {
				throw new ServiceException(true, 404, ex.getLocalizedMessage());
			}

			throw ex;
		}
	}

}
