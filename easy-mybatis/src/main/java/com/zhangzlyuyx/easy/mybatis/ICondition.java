package com.zhangzlyuyx.easy.mybatis;

import java.util.List;

public interface ICondition {

	String getField();
	
	String getOperator();
	
	Object getValue();
	
	Object getSecondValue();
	
	List<Condition> getConditions();
}
