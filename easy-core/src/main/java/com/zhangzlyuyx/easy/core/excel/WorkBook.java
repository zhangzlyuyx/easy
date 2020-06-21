package com.zhangzlyuyx.easy.core.excel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * excel 工作簿
 * @author zhangzlyuyx
 *
 */
public class WorkBook implements Serializable, Cloneable {

	private static final long serialVersionUID = -878784249787371749L;
	
	/**
	 * 表格集合
	 */
	private List<Sheet> sheets;
	
	public List<Sheet> getSheets() {
		if(this.sheets == null) {
			this.sheets = new ArrayList<>();
		}
		return this.sheets;
	}
	
	public void setSheets(List<Sheet> sheets) {
		this.sheets = sheets;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
