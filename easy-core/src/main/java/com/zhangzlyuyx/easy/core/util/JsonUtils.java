package com.zhangzlyuyx.easy.core.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

/**
 * json 工具类
 *
 */
public class JsonUtils {
	
	private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);
	
	/**
	 * 解析 fastjson 对象
	 * @param json
	 * @return
	 */
	public static JSONObject parseObject(String json) {
		try {
			return JSON.parseObject(json);
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}
	
	/**
	 * 解析 fastjson 对象
	 * @param json
	 * @param features
	 * @return
	 */
	public static JSONObject parseObject(String json, Feature... features) {
		try {
			return JSON.parseObject(json, features);
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}
	
	/**
	 * 解析 json 为对象
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T parseObject(String json, Class<T> clazz) {
		try {
			return JSON.parseObject(json, clazz);
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}
	
	/**
	 * 解析 fastjson 对象数组
	 * @param json
	 * @return
	 */
	public static JSONArray parseArray(String json) {
		try {
			return JSON.parseArray(json);
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}
	
	/**
	 * 解析 fastjson 对象数组
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> parseArray(String json, Class<T> clazz) {
		try {
			return JSON.parseArray(json, clazz);
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}
	
	/**
	 * 解析 fastjson 字符串数组
	 * @param json
	 * @return
	 */
	public static String[] parseStringArray(String json) {
		JSONArray array = parseArray(json);
		String[] strArray = new String[array.size()];
		for(int i = 0; i < array.size(); i++){
			 String str = array.getString(i);
			 strArray[i] = str;
		}
		return strArray;
	}
	
	/**
	 * 解析 fastjson 整型数组
	 * @param json
	 * @return
	 */
	public static Integer[] parseIntegerArray(String json){
		JSONArray array = parseArray(json);
		Integer[] strArray = new Integer[array.size()];
		for(int i = 0; i < array.size(); i++){
			 Integer str = array.getInteger(i);
			 strArray[i] = str;
		}
		return strArray;
	}
	
	/**
	 * 解析 fastjson 长整型数组
	 * @param json
	 * @return
	 */
	public static Long[] parseLongArray(String json){
		JSONArray array = parseArray(json);
		Long[] strArray = new Long[array.size()];
		for(int i = 0; i < array.size(); i++){
			 Long str = array.getLong(i);
			 strArray[i] = str;
		}
		return strArray;
	}
}
