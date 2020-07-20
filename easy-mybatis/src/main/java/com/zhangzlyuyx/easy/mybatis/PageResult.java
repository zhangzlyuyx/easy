package com.zhangzlyuyx.easy.mybatis;

import java.util.ArrayList;
import java.util.List;

import com.zhangzlyuyx.easy.core.util.ConvertUtils;

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
	
	/**
	 * 内存分页
	 * @param rows 记录列表
	 * @param pageNo 页码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public static <E> PageResult<E> getPageResult(List<E> rows, Integer pageNo, Integer pageSize){
		if(rows == null || rows.size() == 0) {
			return new PageResult<>();
		}
		if(pageNo == null || pageNo.intValue() < 1) {
			pageNo = 1;
		}
		if(pageSize == null || pageSize.intValue() < 1) {
			pageSize = 1;
		}
		PageResult<E> result = new PageResult<>();
		result.setPageNo(pageNo);
		result.setPageSize(pageSize);
		result.setTotal(ConvertUtils.toLong(rows.size(), 0L));
		
		//计算分页数
		Integer pageCount = (rows.size() % pageSize == 0) ? (rows.size() / pageSize) : (rows.size() / pageSize + 1);
		result.setPages(pageCount);
		
		//开始索引
		int fromIndex = 0;
		//结束索引
        int toIndex = 0;
 
        if(pageNo > pageCount){
        	pageNo = pageCount;
        }
        if (!pageNo.equals(pageCount)) {
            fromIndex = (pageNo - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNo - 1) * pageSize;
            toIndex = rows.size();
        }
        //截取列表
        List<E> list = rows.subList(fromIndex, toIndex);
		
        result.setRows(list);
		
		return result;
	}
}
