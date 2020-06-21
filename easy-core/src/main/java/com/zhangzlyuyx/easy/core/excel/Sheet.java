package com.zhangzlyuyx.easy.core.excel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * excel表格
 * @author zhangzlyuyx
 *
 */
public class Sheet implements Serializable {
	
	private static final long serialVersionUID = -7057659769899647956L;
	
	private String sheetName;
	
	public String getSheetName() {
		return sheetName;
	}
	
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	/**
	 * 数据行集合
	 */
	private List<Row> rows;
	
	public List<Row> getRows() {
		if(this.rows == null) {
			this.rows = new ArrayList<>();
		}
		return this.rows;
	}
	
	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
}
