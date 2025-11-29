package com.shopkart.project.util;

import java.util.List;
import java.util.regex.Pattern;

import com.shopkart.project.exceptions.ServiceException;

public class StringUtil {

	public static void validateField(String field, String value, List<String> regEx, List<String> msg, List<String> code, List<Boolean> errorOnMatch) {
		if (field == null || field.trim().isEmpty()) {
			return;
		}

		for (int i = 0; i < regEx.size(); i++) {
			Pattern pattern = Pattern.compile(regEx.get(i));
			boolean matches = pattern.matcher(value).matches();
			boolean shouldErrorOnMatch = errorOnMatch != null && errorOnMatch.get(i);

			if ((shouldErrorOnMatch && matches) || (!shouldErrorOnMatch && !matches)) {
				throw new ServiceException(true, Integer.parseInt(code.get(i)), msg.get(i));
			}
		}
	}

}
