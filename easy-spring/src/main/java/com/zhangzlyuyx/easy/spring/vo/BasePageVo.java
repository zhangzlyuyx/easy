package com.zhangzlyuyx.easy.spring.vo;

import java.util.ArrayList;
import java.util.List;

import com.zhangzlyuyx.easy.core.Result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("分页Vo")
public class BasePageVo<T> extends BaseVo {

	private static final long serialVersionUID = -2833972261662172652L;

	@ApiModelProperty(value = "总记录数")
	private Long total;
	
	public Long getTotal() {
		return this.total;
	}
	
	public BasePageVo<T> setTotal(Long total) {
		this.total = total;
		return this;
	}
	
	@ApiModelProperty(value = "分页序号")
	private Integer pageNo;
	
	public Integer getPageNo() {
		return this.pageNo;
	}
	
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	
	@ApiModelProperty(value = "每页记录数")
	private Integer pageSize;
	
	public Integer getPageSize() {
		return this.pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	@ApiModelProperty(value = "数据行")
	private List<T> rows;
	
	public List<T> getRows() {
		if(this.rows == null) {
			this.rows = new ArrayList<>();
		}
		return this.rows;
	}
	
	public BasePageVo<T> setRows(List<T> rows) {
		this.rows = rows;
		return this;
	}
	
	/**
	 * 初始化
	 */
	public BasePageVo() {
		super.success("");
	}
	
	/**
	 * 初始化
	 * @param success
	 */
	public BasePageVo(boolean success) {
		super(success);
	}
	
	/**
	 * 初始化
	 * @param success
	 * @param msg
	 */
	public BasePageVo(boolean success, String msg) {
		super(success, msg);
	}
	
	/**
	 * 初始化
	 * @param success
	 * @param msg
	 * @param rows
	 */
	public BasePageVo(boolean success, String msg, List<T> rows) {
		super(success, msg);
		this.rows = rows;
		this.total = (rows != null && rows.size() > 0) ? Long.parseLong(String.valueOf(rows.size())) : 0L;
	}
	
	/**
	 * 初始化
	 * @param success
	 * @param msg
	 * @param rows
	 * @param total
	 */
	public BasePageVo(boolean success, String msg, List<T> rows, Long total) {
		super(success, msg);
		this.total = total;
		this.rows = rows;
	}
	
	@Override
	public BasePageVo<T> success(String msg) {
		super.success(msg);
		return this;
	}
	
	public BasePageVo<T> success(String msg, List<T> rows, Long total) {
		super.success(msg);
		this.rows = rows;
		this.total = total;
		return this;
	}
	
	@Override
	public BasePageVo<T> error(String msg) {
		super.error(msg);
		return this;
	}
	
	@Override
	public BasePageVo<T> denied(String msg) {
		super.denied(msg);
		return this;
	}
	
	@Override
	public BasePageVo<T> load(Result<?> result) {
		super.load(result);
		return this;
	}
}
