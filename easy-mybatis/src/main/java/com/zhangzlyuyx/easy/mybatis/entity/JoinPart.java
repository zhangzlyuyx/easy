package com.zhangzlyuyx.easy.mybatis.entity;

import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

/**
 * JoinPart
 * @author zhangzlyuyx
 *
 */
public class JoinPart {

	private String joinConnect = "left join";
	
	public String getJoinConnect() {
		return this.joinConnect;
	}
	
	public void setJoinConnect(String joinConnect) {
		this.joinConnect = joinConnect;
	}
	
	/**
	 * join 实体类
	 */
	private Class<?> joinEntityClass;
	
	public Class<?> getJoinEntityClass() {
		return this.joinEntityClass;
	}
	
	/**
	 * join table
	 */
	private String joinTable;
	
	public String getJoinTable() {
		return this.joinTable;
	}
	
	public void setJoinTable(String joinTable) {
		this.joinTable = joinTable;
	}
	
	/**
	 * join on 语句
	 */
	private String joinOnClause;
	
	public String getJoinOnClause() {
		return joinOnClause;
	}
	
	public void setJoinOnClause(String joinOnClause) {
		this.joinOnClause = joinOnClause;
	}
	
	public JoinPart(String joinTable, String joinOnClause) {
		this.joinTable = joinTable;
		this.joinOnClause = joinOnClause;
	}
	
	public JoinPart(Class<?> joinEntityClass, String joinOnClause) {
		this.joinEntityClass = joinEntityClass;
		EntityTable entityTable = EntityHelper.getEntityTable(this.joinEntityClass);
		this.joinTable = entityTable != null ? entityTable.getName() : "";
		this.joinOnClause = joinOnClause;
	}
}
