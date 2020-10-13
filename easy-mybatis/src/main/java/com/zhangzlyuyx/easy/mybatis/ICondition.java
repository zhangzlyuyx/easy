package com.zhangzlyuyx.easy.mybatis;

import java.util.List;

public interface ICondition {

	String getAndOr();
	
	String getField();
	
	String getOperator();
	
	Object getValue();
	
	Object getSecondValue();
	
	List<Condition> getConditions();
}
