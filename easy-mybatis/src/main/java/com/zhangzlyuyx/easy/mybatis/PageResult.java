package com.zhangzlyuyx.easy.mybatis;

import java.util.List;

public class PageResult<T> implements IPageResult<T> {

	private Integer pageNo;
	
	public Integer getPageNo() {
		return pageNo;
	}
	
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	
	private Integer pageSize;
	
	public Integer getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	private Integer pages;
	
	public Integer getPages() {
		return pages;
	}
	
	
	public void setPages(Integer pages) {
		this.pages = pages;
	}
	
	private Long total;
	
	@Override
	public Long getTotal() {
		return this.total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
	
	private List<T> rows;
	
	@Override
	public List<T> getRows() {
		return this.rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}
