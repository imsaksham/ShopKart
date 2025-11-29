package com.shopkart.project.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shopkart.project.model.MessageDetail;

public class MessageUtility {

	private static Map<String, MessageDetail> map;
	private static Map<String, Object> validationProperties;
	private static final Logger logger = LoggerFactory.getLogger(MessageUtility.class);

	public static MessageDetail getMessage(String msgKey) {
		return map.get(msgKey) != null ? map.get(msgKey) : new MessageDetail(101,"");
	}

	public static void loadMessage(List<List> lists) {
		map = new HashMap<>();
		if(lists != null) {
			for (List list: lists) {
				try {
					MessageDetail messageDetail = new MessageDetail((int) list.get(1), (String) list.get(2));
					map.put((String) list.get(0), messageDetail);
				} catch (Exception ex) {
					logger.error("Error while loading messages " +ex + "\n" +1);
				}
			}
		}
	}

	public static void loadValidation(Map<String, Object> map) {
		validationProperties = map;
	}

	public static Map<String, Object> getValidationProps() {
		return validationProperties;
	}

	public static Map<String, Object> getValidationProps(String key) {
		return (Map<String, Object>) validationProperties.get(key);
	}

}
