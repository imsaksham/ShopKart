package com.shopkart.project.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopkart.project.exceptions.ServiceException;
import com.shopkart.project.payload.CategoryDTO;
import com.shopkart.project.payload.CategoryResponse;
import com.shopkart.project.service.CategoryService;
import com.shopkart.project.util.MessageUtility;
import com.shopkart.project.util.StringUtil;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/api")
public class CategoryController {

	private CategoryService categoryService;
	private Map<String, Object> validationProperties;
	private Map<String, Object> messageProperties;
	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	void init() throws Exception {
		try {
			validationProperties = new ObjectMapper()
					.readValue(new ClassPathResource("api-validation-config.json").getFile(), Map.class);
			messageProperties = new ObjectMapper()
					.readValue(new ClassPathResource("messages.json").getFile(), Map.class);

			MessageUtility.loadMessage((List) messageProperties.get("messages"));
			MessageUtility.loadValidation(validationProperties);
		} catch (Exception ex) {
			throw ex;
		}
	}

	@SuppressWarnings("unchecked")
	private boolean isValidBody(String apiName, Map<String, Object> requestBody) {
		try {
			Map<String, Object> categoryApi = (Map<String, Object>) validationProperties.get(apiName);
			if (categoryApi == null) {
				throw new ServiceException(false, "Validation config not found for API: " +apiName);
			}

			List<String> requiredFields = (List<String>) categoryApi.get("requiredFields");
			if (requiredFields != null && !requiredFields.isEmpty()) {
				Set<String> requestKeys = new HashSet<>();

				for (Map.Entry<String, Object> map: requestBody.entrySet()) {
					requestKeys.add(map.getKey());
				}

				Set<String> missingRequiredFields = requiredFields.stream()
							  .filter(requiredField -> !requestKeys.contains(requiredField))
							  .collect(Collectors.toSet());

				if (!missingRequiredFields.isEmpty()) {
					throw new ServiceException(false, 401, "Missing required fields: " +missingRequiredFields);
				}
			}

			for (Map.Entry<String, Object> entry: requestBody.entrySet()) {
				String field = entry.getKey();
				String value = (String) entry.getValue();

				Map<String, Object> config = (Map<String, Object>) categoryApi.get(field);
				List<String> regEx = (List<String>) config.get("regEx");
				List<String> msg = (List<String>) config.get("msg");
				List<String> code = (List<String>) config.get("code");
				List<Boolean> errorOnMatch = (List<Boolean>) config.get("errorOnMatch");

				StringUtil.validateField(field, value, regEx, msg, code, errorOnMatch);
			}

			return true;
		} catch (ServiceException ex) {
			throw ex;
		}
	}

	@GetMapping("/public/categories")
	public ResponseEntity<CategoryResponse> getAllCategories() {
		try {
			CategoryResponse result = categoryService.getAllCategories();
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (ServiceException ex) {
			throw new ServiceException(true, ex.getErrorCode(), ex.getLocalizedMessage());
		} catch (Exception ex) {
			throw new ServiceException(true, 401, ex.getLocalizedMessage());
		}
	}

	@PostMapping("/public/categories")
	public ResponseEntity<CategoryDTO> createCategory(@RequestBody Map<String, Object> requestBody) {
		logger.debug("requestBody: " +requestBody);
		try {
			isValidBody("category-api", requestBody);
			CategoryDTO savedCategoryDto = categoryService.createCategory(requestBody);

			return new ResponseEntity<>(savedCategoryDto, HttpStatus.CREATED);
		} catch (ServiceException ex) {
			throw new ServiceException(true, ex.getErrorCode(), ex.getLocalizedMessage());
		} catch (Exception ex) {
			throw new ServiceException(true, 401, "Failed to create category: " +ex.getMessage());
		}
	}

	@PutMapping("/public/categories/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@RequestBody Map<String, Object> requestBody, @PathVariable Long categoryId) {
		try {
			isValidBody("category-api", requestBody);
			CategoryDTO updatedCategoryDto = categoryService.updateCategory(requestBody, categoryId);

			return new ResponseEntity<CategoryDTO>(updatedCategoryDto, HttpStatus.OK);
		} catch (ServiceException ex) {
			throw new ServiceException(true, ex.getErrorCode(), ex.getLocalizedMessage());
		} catch (Exception ex) {
			throw new ServiceException(true, 401, "Error while updating category: " +ex.getLocalizedMessage());
		}
	}

	@DeleteMapping("/public/categories/{categoryId}")
	public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
		try {
			CategoryDTO deletedCategoryDto = categoryService.deleteCategory(categoryId);
			return new ResponseEntity<>(deletedCategoryDto, HttpStatus.OK);
		} catch (ServiceException ex) {
			throw new ServiceException(true, ex.getErrorCode(), ex.getLocalizedMessage());
		} catch (Exception ex) {
			throw new ServiceException(true, 401, "Error while deleting category: " +ex.getLocalizedMessage());
		}
	}

}
