package com.zhangzlyuyx.easy.mybatis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * mybatis 条件
 * @author zhangzlyuyx
 *
 */
public class Condition implements ICondition, Serializable, Cloneable {

	private static final long serialVersionUID = 7365886177260173253L;
	
	/**
	 * and or
	 */
	private String andOr = "and";
	
	@Override
	public String getAndOr() {
		return andOr;
	}
	
	public void setAndOr(String andOr) {
		this.andOr = andOr;
	}
	
	/**
	 * 字段
	 */
	private String field;
	
	/**
	 * 获取字段名
	 */
	@Override
	public String getField() {
		return this.field;
	}
	
	/**
	 * 设置字段值
	 * @param field
	 */
	public void setField(String field) {
		this.field = field;
		this.columnName = field;
	}
	
	/**
	 * 列名称
	 */
	private String columnName;
	
	public String getColumnName() {
		return this.columnName;
	}
	
	public void setColumnName(String columnName) {
		this.columnName = columnName;
		this.field = columnName;
	}
	
	/**
	 * 运算符（大于号，小于号，等于号 like 等）
	 */
	private String operator;
	
	/**
	 * 获取运算符
	 */
	@Override
	public String getOperator() {
		return this.operator;
	}
	
	/**
	 * 设置运算符
	 * @param operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	/**
	 * 值
	 */
	private Object value;
	
	@Override
	public Object getValue() {
		return this.value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * between firstValue and secondValue
	 */
	private Object secondValue;
	
	@Override
	public Object getSecondValue() {
		return secondValue;
	}
	
	public void setSecondValue(Object secondValue) {
		this.secondValue = secondValue;
	}
	
	/**
	 * 子条件集合
	 */
	private List<Condition> conditions;
	
	@Override
	public List<Condition> getConditions() {
		if(this.conditions == null) {
			this.conditions = new ArrayList<Condition>();
		}
		return this.conditions;
	}
	
	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}
	
	/**
	 * 初始化
	 * @param field
	 * @param value
	 */
	public Condition(String field, Object value) {
		this(field, "=", value, null);
	}
	
	/**
	 * 初始化
	 * @param field
	 * @param operator
	 * @param value
	 */
	public Condition(String field, String operator, Object value) {
		this(field, operator, value, null);
	}
	
	/**
	 * 初始化
	 * @param field
	 * @param operator
	 * @param value
	 * @param secondValue
	 */
	public Condition(String field, String operator, Object value, Object secondValue) {
		this.field = field;
		this.operator = operator;
		this.value = value;
		this.secondValue = secondValue;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Condition desCondition = new Condition(this.getField(), this.getOperator(), this.getValue(), this.getSecondValue());
		copyCondition(this, desCondition);
		return desCondition;
	}
	
	/**
	 * 拷贝 condition 属性
	 * @param srcCondition 源 condition
	 * @param desCondition 目标 condition
	 */
	public static void copyCondition(Condition srcCondition, Condition desCondition) {
		if(srcCondition == null || desCondition == null) {
			return;
		}
		desCondition.setField(srcCondition.getField());
		desCondition.setOperator(srcCondition.getOperator());
		desCondition.setValue(srcCondition.getValue());
		desCondition.setSecondValue(srcCondition.getSecondValue());
		if(srcCondition.getConditions().size() > 0) {
			for(Condition srcSubCondition : srcCondition.getConditions()) {
				Condition subDesCondition = new Condition(srcSubCondition.getField(), srcSubCondition.getValue());
				copyCondition(srcSubCondition, subDesCondition);
				desCondition.getConditions().add(subDesCondition);
			}
		}
	}
	
	/**
	 * Condition 数组 转 List
	 * @param conditions
	 * @return
	 */
	public static List<Condition> asList(Condition... conditions){
		List<Condition> conditionList = new ArrayList<Condition>();
		if(conditions != null && conditions.length > 0) {
			for(Condition condition : conditions) {
				conditionList.add(condition);
			}
		}
		return conditionList;
	}
	
	/**
	 * 解析 json
	 * @param json
	 * @return
	 */
	public static List<Condition> parseArray(String json){
		if(json == null || json.length() == 0) {
			return new ArrayList<>();
		}
		JSONArray array = JSONObject.parseArray(json);
		if(array == null || array.size() == 0) {
			return new ArrayList<>();
		}
		List<Condition> conditionList = new ArrayList<>();
		for(int i = 0; i < array.size(); i++) {
			JSONObject item = array.getJSONObject(i);
			//json转对象
			Condition condition =  item.toJavaObject(Condition.class);
			//重新取值，防止部分字段转换失败问题
			if(item.containsKey("columnName")) {
				condition.setColumnName(item.getString("columnName"));
			}
			if(item.containsKey("field")) {
				condition.setField(item.getString("field"));
			}
			if(item.containsKey("operator")) {
				condition.setOperator(item.getString("operator"));
			}
			if(item.containsKey("value")) {
				condition.setValue(item.get("value"));
			}
			if(item.containsKey("secondValue")) {
				condition.setSecondValue(item.get("secondValue"));
			}
			conditionList.add(condition);
		}
		return conditionList;
	}
}
