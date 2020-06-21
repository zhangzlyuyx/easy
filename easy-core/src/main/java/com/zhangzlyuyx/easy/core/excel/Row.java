package com.zhangzlyuyx.easy.core.excel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * excel 数据行
 * @author zhangzlyuyx
 *
 */
public class Row implements Serializable  {

	private static final long serialVersionUID = -671516216587865534L;

	/**
	 * 单元格集合
	 */
	private List<Cell> cells;
	
	public List<Cell> getCells() {
		if(this.cells == null) {
			this.cells = new ArrayList<>();
		}
		return this.cells;
	}
	
	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}
}
