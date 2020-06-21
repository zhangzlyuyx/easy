package com.zhangzlyuyx.easy.mybatis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * mybatis 条件
 * @author zhangzlyuyx
 *
 */
public class Condition implements ICondition, Serializable, Cloneable {

	private static final long serialVersionUID = 7365886177260173253L;
	
	/**
	 * 字段
	 */
	private String field;
	
	/**
	 * 获取字段名
	 */
	public String getField() {
		return this.field;
	}
	
	/**
	 * 设置字段值
	 * @param field
	 */
	public void setField(String field) {
		this.field = field;
	}
	
	public String getColumnName() {
		return this.field;
	}
	
	public void setColumnName(String columnName) {
		this.field = columnName;
	}
	
	/**
	 * 运算符（大于号，小于号，等于号 like 等）
	 */
	private String operator;
	
	/**
	 * 获取运算符
	 */
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
}
