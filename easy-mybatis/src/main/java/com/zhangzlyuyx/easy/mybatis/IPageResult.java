package com.zhangzlyuyx.easy.mybatis;

import java.util.List;

public interface IPageResult<T> {

	Long getTotal();
	
	List<T> getRows();
}
