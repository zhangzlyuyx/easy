package com.zhangzlyuyx.easy.mybatis;

import java.util.List;

public interface IPageQuery {

	Integer getPageNo();
	
	Integer getPageSize();
	
	String getOrderByClause();
	
	String[] getProperties();
	
	List<Condition> getConditions();
}
