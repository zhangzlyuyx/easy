package com.zhangzlyuyx.easy.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * map结果
 * @author zhangzlyuyx
 *
 */
public class MapResult extends Result<Map<String, Object>> {

	private static final long serialVersionUID = 2267438629609811155L;

	public MapResult(boolean code, String msg) {
		super(code, msg, null);
	}
	
	public MapResult(String code, String msg) {
		super(code, msg, null);
	}
	
	public MapResult(boolean code, String msg, Map<String, Object> data) {
		super(code, msg, data);
	}
	
	public MapResult(String code, String msg, Map<String, Object> data) {
		super(code, msg, data);
	}
	
	/**
	 * 获取 object 值
	 * @param key key
	 * @return
	 */
	public Object getObject(String key) {
		if(this.getData() == null) {
			return null;
		}
		for(Entry<String, Object> kv : this.getData().entrySet()) {
			if(kv.getKey().equalsIgnoreCase(key)) {
				return kv.getValue();
			}
		}
		return null;
	}
	
	/**
	 * 获取 string 值
	 * @param key key
	 * @return
	 */
	public String getString(String key) {
		Object value = getObject(key);
		if(value == null) {
			return null;
		}
		return value instanceof String ? (String)value : value.toString();
	}
	
	/**
	 * 获取 Long 值
	 * @param key key
	 * @return
	 */
	public Long getLong(String key) {
		Object value = getObject(key);
		if(value == null) {
			return null;
		}
		return value instanceof Long ? (Long)value : Long.parseLong(value.toString());
	}
	
	/**
	 * 获取 Integer 值转
	 * @param key key
	 * @return
	 */
	public Integer getInteger(String key) {
		Object value = getObject(key);
		if(value == null) {
			return null;
		}
		return value instanceof Integer ? (Integer)value : Integer.parseInt(value.toString());
	}
	
	/**
	 * 获取 Float 值
	 * @param key key
	 * @return
	 */
	public Float getFloat(String key) {
		Object value = getObject(key);
		if(value == null) {
			return null;
		}
		return value instanceof Float ? (Float)value : Float.parseFloat(value.toString());
	}
	
	/**
	 * 获取 JsonObject 值
	 * @param key key
	 * @return
	 */
	public JSONObject getJsonObject(String key) {
		Object value = getObject(key);
		if(value == null) {
			return null;
		}
		if(value instanceof JSONObject) {
			return (JSONObject)value;
		}
		try {
			return JSONObject.parseObject(value.toString());
		} catch (Exception e) {
			return null;
		} 
	}
	
	/**
	 * 获取  JsonArray 值
	 * @param key key
	 * @return
	 */
	public JSONArray getJsonArray(String key) {
		Object value = getObject(key);
		if(value == null) {
			return null;
		}
		if(value instanceof JSONArray) {
			return (JSONArray)value;
		}
		try {
			return JSONArray.parseArray(value.toString());
		} catch (Exception e) {
			return null;
		} 
	}
	
	/**
	 * 值转换成对象
	 * @param key
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getObject(String key, Class<T> clazz) {
		Object value = getObject(key);
		if(value == null) {
			return null;
		}
		if(value.getClass() == clazz) {
			return (T)value;
		}
		try {
			return JSONObject.parseObject(value.toString(), clazz);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 值转换成对象集合
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> List<T> getObjectArray(String key, Class<T> clazz){
		Object value = getObject(key);
		if(value == null) {
			return null;
		}
		try {
			return JSONObject.parseArray(value.toString(), clazz);
		} catch (Exception e) {
			return new ArrayList<T>();
		}
	}
	
	/**
	 * 解析json字符
	 * @param jsonString
	 * @return
	 */
	public static MapResult parseJsonString(String jsonString) {
		if(jsonString == null || jsonString.length() == 0) {
			return null;
		}
		return (MapResult)JSONObject.parseObject(jsonString, MapResult.class);
	}
}
