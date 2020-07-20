package com.zhangzlyuyx.easy.media.zlmediakit.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

/**
 * 负载和延迟
 * @author zhangzlyuyx
 *
 */
public class LoadAndDeplay implements Serializable {

	private static final long serialVersionUID = 7427533632760629198L;

	/**
	 * 该线程负载，0 ~ 100
	 */
	private Integer load;
	
	public Integer getLoad() {
		return load;
	}
	
	public void setLoad(Integer load) {
		this.load = load;
	}
	
	/**
	 * 该线程延时
	 */
	private Integer delay;
	
	public Integer getDelay() {
		return this.delay;
	}
	
	public void setDelay(Integer delay) {
		this.delay = delay;
	}
	
	public static List<LoadAndDeplay> parse(JSONArray jsonArray){
		if(jsonArray == null) {
			return new ArrayList<>();
		}
		return jsonArray.toJavaList(LoadAndDeplay.class);
	}
}
