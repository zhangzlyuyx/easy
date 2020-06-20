package com.zhangzlyuyx.easy.mybatis;

import java.util.ArrayList;
import java.util.List;

public class PageQuery implements IPageQuery {
	
	public static final int DEFAULT_PAGE_NO = 1;
	
	public static final int DEFAULT_PAGE_SIZE = 20;

	private Integer pageNo;
	
	@Override
	public Integer getPageNo() {
		return this.pageNo;
	}
	
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	
	private Integer pageSize;
	
	@Override
	public Integer getPageSize() {
		return this.pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	private String orderByClause;
	
	@Override
	public String getOrderByClause() {
		return this.orderByClause;
	}
	
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}
	
	private String[] properties;
	
	@Override
	public String[] getProperties() {
		return properties;
	}
	
	public void setProperties(String[] properties) {
		this.properties = properties;
	}
	
	private List<Condition> conditions;
	
	@Override
	public List<Condition> getConditions() {
		if(this.conditions == null) {
			this.conditions = new ArrayList<>();
		}
		return this.conditions;
	}
	
	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}
	
	public PageQuery() {
		this.pageNo = DEFAULT_PAGE_NO;
		this.pageSize = DEFAULT_PAGE_SIZE;
		this.conditions = new ArrayList<>();
	}
}
