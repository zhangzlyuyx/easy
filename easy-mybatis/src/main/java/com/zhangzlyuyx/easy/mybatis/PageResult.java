package com.zhangzlyuyx.easy.mybatis;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页结果
 * @author zhangzlyuyx
 *
 * @param <T>
 */
public class PageResult<T> implements IPageResult<T> {

	/**
	 * 分页序号
	 */
	private Integer pageNo;
	
	public Integer getPageNo() {
		return pageNo;
	}
	
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	
	/**
	 * 每页记录数
	 */
	private Integer pageSize;
	
	public Integer getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	/**
	 * 页数
	 */
	private Integer pages;
	
	public Integer getPages() {
		if(this.pages == null) {
			if(this.pageSize != null && this.pageSize.intValue() > 0 
					&& this.total != null && this.total.longValue() > 0) {
				this.pages = (this.total.intValue() % pageSize == 0) ? (this.total.intValue() / pageSize) : (this.total.intValue() / pageSize + 1);
			}
		}
		return this.pages;
	}
	
	
	public void setPages(Integer pages) {
		this.pages = pages;
	}
	
	/**
	 * 总计录数
	 */
	private Long total;
	
	@Override
	public Long getTotal() {
		return this.total;
	}

	@Override
	public void setTotal(Long total) {
		this.total = total;
	}
	
	private List<T> rows;
	
	@Override
	public List<T> getRows() {
		if(this.rows == null) {
			this.rows = new ArrayList<>();
		}
		return this.rows;
	}

	@Override
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
	/**
	 * PageResult
	 */
	public PageResult() {
		
	}
	
	/**
	 * PageResult
	 * @param rows
	 */
	public PageResult(List<T> rows) {
		this.rows = rows;
	}
}
