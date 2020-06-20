package com.zhangzlyuyx.easy.mybatis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zhangzlyuyx.easy.core.util.StringUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * mybatis 条件
 * @author zhangzlyuyx
 *
 */
public class Condition implements Serializable, Cloneable {

	private static final long serialVersionUID = 7365886177260173253L;
	
	private static final String OPERATOR_LIKE = "LIKE";
	private static final String OPERATOR_IN = "IN";
	private static final String OPERATOR_IS = "IS";
	private static final String OPERATOR_BETWEEN = "BETWEEN";
	private static final List<String> OPERATORS = Arrays.asList("<>", "<=", "<", ">=", ">", "=", "!=", OPERATOR_IN);
	private static final String VALUE_NULL = "NULL";
	
	/**
	 * 字段
	 */
	private String field;
	
	public String getField() {
		return field;
	}
	
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
	
	public String getOperator() {
		return this.operator;
	}
	
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
		Condition condition = new Condition(this.getField(), this.getOperator(), this.getValue(), this.getSecondValue());
		return condition;
	}
	
	/**
	 * 条件操作处理
	 * @param example
	 * @param criteriaNew
	 * @return
	 */
	public boolean criteriaOperator(Example example, Criteria criteriaNew) {
		if(StringUtils.isEmpty(this.getColumnName()) && StringUtils.isEmpty(this.getOperator()) && this.getValue() == null) {
			return false;
		}
		//获取操作类型
		String operator = !StringUtils.isEmpty(this.getOperator()) ? this.getOperator() : "";
		if(operator.equalsIgnoreCase("=")){
			criteriaNew.andEqualTo(this.getColumnName(), this.getValue());
		}else if(operator.equalsIgnoreCase("!=") || operator.equalsIgnoreCase("<>")){
			criteriaNew.andNotEqualTo(this.getColumnName(), this.getValue());
		}else if(operator.equalsIgnoreCase("like")){
			criteriaNew.andLike(this.getColumnName(), this.getValue().toString());
		}else if(operator.equalsIgnoreCase("not like")){
			criteriaNew.andNotLike(this.getColumnName(), this.getValue().toString());
		}else if(operator.equalsIgnoreCase(">")){
			criteriaNew.andGreaterThan(this.getColumnName(), this.getValue());
		}else if(operator.equalsIgnoreCase(">=")){
			criteriaNew.andGreaterThanOrEqualTo(this.getColumnName(), this.getValue());
		}else if(operator.equalsIgnoreCase("<")){
			criteriaNew.andLessThan(this.getColumnName(), this.getValue());
		}else if(operator.equalsIgnoreCase("<=")){
			criteriaNew.andLessThanOrEqualTo(this.getColumnName(), this.getValue());
		}else if(operator.equalsIgnoreCase("in")){
			criteriaNew.andIn(this.getColumnName(), (List<?>)this.getValue());
		}else if(operator.equalsIgnoreCase("not in")){
			criteriaNew.andNotIn(this.getColumnName(), (List<?>)this.getValue());
		}else if(operator.equalsIgnoreCase("isnull") || operator.equalsIgnoreCase("is null")){
			criteriaNew.andIsNull(this.getColumnName());
		}else if(operator.equalsIgnoreCase("is not null")){
			criteriaNew.andIsNotNull(this.getColumnName());
		}else if(operator.equalsIgnoreCase("between")){
			criteriaNew.andBetween(this.getColumnName(), this.getValue(), this.getSecondValue());
		}  else{
			// 其他不支持操作处理
			if(this.getValue() != null){
				criteriaNew.andCondition(this.getColumnName(), this.getValue());
			}else{
				criteriaNew.andCondition(this.getColumnName());
			}
		}
		//递归嵌套条件处理
		if(this.getConditions() != null && this.getConditions().size() > 0) {
			Criteria nextCriteria = example.createCriteria();
			for(Condition nextCondition : this.getConditions()) {
				nextCondition.criteriaOperator(example, nextCriteria);
			}
		}
		return true;
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
