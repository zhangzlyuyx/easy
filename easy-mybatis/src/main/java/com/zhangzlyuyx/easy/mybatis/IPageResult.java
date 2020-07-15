package com.zhangzlyuyx.easy.mybatis;

import java.util.List;

public interface IPageResult<T> {

	Long getTotal();
	
	void setTotal(Long total);
	
	List<T> getRows();
	
	void setRows(List<T> rows);
}
