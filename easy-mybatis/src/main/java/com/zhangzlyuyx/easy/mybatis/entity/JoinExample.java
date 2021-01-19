package com.zhangzlyuyx.easy.mybatis.entity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import com.zhangzlyuyx.easy.mybatis.util.EntityUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

/**
 * JoinExample
 * 
 * @author zhangzlyuyx
 *
 */
public class JoinExample extends Example {

	/**
	 * 表关联部分集合
	 */
	protected List<JoinPart> joinParts;

	public List<JoinPart> getJoinParts() {
		if (this.joinParts == null) {
			this.joinParts = new ArrayList<>();
		}
		return this.joinParts;
	}

	public void setJoinParts(List<JoinPart> joinParts) {
		this.joinParts = joinParts;
	}

	/**
	 * 查询字段是否包含表名
	 */
	protected boolean selectColumnsIncludeTable = true;

	public boolean isSelectColumnsIncludeTable() {
		return selectColumnsIncludeTable;
	}

	public void setSelectColumnsIncludeTable(boolean selectColumnsIncludeTable) {
		this.selectColumnsIncludeTable = selectColumnsIncludeTable;
	}

	/**
	 * where 字段是否包含表名称
	 */
	protected boolean whereColumnsIncludeTable = true;

	public boolean isWhereColumnsIncludeTable() {
		return whereColumnsIncludeTable;
	}

	public void setWhereColumnsIncludeTable(boolean whereColumnsIncludeTable) {
		this.whereColumnsIncludeTable = whereColumnsIncludeTable;
	}

	/**
	 * 排序字段是否包含表名
	 */
	protected boolean orderByColumnsIncludeTable = false;

	public boolean isOrderByColumnsIncludeTable() {
		return orderByColumnsIncludeTable;
	}

	public void setOrderByColumnsIncludeTable(boolean orderByColumnsIncludeTable) {
		this.orderByColumnsIncludeTable = orderByColumnsIncludeTable;
	}
	
	/**
	 * group 语句
	 */
	private String groupByClause;
	
	public String getGroupByClause() {
		return groupByClause;
	}
	
	public void setGroupByClause(String groupByClause) {
		this.groupByClause = groupByClause;
	}

	/**
	 * 排序字段是否包含表名
	 */
	protected boolean groupByColumnsIncludeTable = false;
	
	public boolean isGroupByColumnsIncludeTable() {
		return groupByColumnsIncludeTable;
	}
	
	public void setGroupByColumnsIncludeTable(boolean groupByColumnsIncludeTable) {
		this.groupByColumnsIncludeTable = groupByColumnsIncludeTable;
	}
	
	/**
	 * 默认exists为true
	 * 
	 * @param entityClass
	 */
	public JoinExample(Class<?> entityClass) {
		super(entityClass);
	}

	/**
	 * 带exists参数的构造方法，默认notNull为false，允许为空
	 * 
	 * @param entityClass
	 * @param exists      - true时，如果字段不存在就抛出异常，false时，如果不存在就不使用该字段的条件
	 */
	public JoinExample(Class<?> entityClass, boolean exists) {
		super(entityClass, exists);
	}

	/**
	 * 带exists参数的构造方法
	 * 
	 * @param entityClass
	 * @param exists      - true时，如果字段不存在就抛出异常，false时，如果不存在就不使用该字段的条件
	 * @param notNull     - true时，如果值为空，就会抛出异常，false时，如果为空就不使用该字段的条件
	 */
	public JoinExample(Class<?> entityClass, boolean exists, boolean notNull) {
		super(entityClass, exists, notNull);
	}

	/**
	 * 添加 join 部分
	 * 
	 * @param joinEntityClass
	 * @param joinOnClause
	 * @return
	 */
	public JoinExample addJoinPart(Class<?> joinEntityClass, String joinOnClause) {
		this.getJoinParts().add(new JoinPart(joinEntityClass, joinOnClause));
		return this;
	}

	/**
	 * 添加 join 部分
	 * 
	 * @param joinConnect
	 * @param joinEntityClass
	 * @param joinOnClause
	 * @return
	 */
	public JoinExample addJoinPart(String joinConnect, Class<?> joinEntityClass, String joinOnClause) {
		JoinPart joinPart = new JoinPart(joinEntityClass, joinOnClause);
		joinPart.setJoinConnect(joinConnect);
		this.getJoinParts().add(joinPart);
		return this;
	}

	@Override
	public Example selectProperties(String... properties) {
		if (properties != null && properties.length > 0) {
			if (this.selectColumns == null) {
				this.selectColumns = new LinkedHashSet<String>();
			}
			for (String property : properties) {
				this.selectColumns.add(property);
			}
		}
		return this;
	}

	/**
	 * 添加属性
	 * 
	 * @param propertity
	 * @return
	 */
	public Example addPropertity(String propertity) {
		if (propertity == null || propertity.length() == 0) {
			return this;
		}
		if (this.selectColumns == null) {
			this.selectColumns = new LinkedHashSet<String>();
		}
		this.selectColumns.add(propertity);
		return this;
	}
	
	/**
	 * 获取实体表名
	 * @param entityClass
	 * @return
	 */
	public  String getTableName(Class<?> entityClass) {
		return getTableName(entityClass, null);
	}
	
	/**
	 * 获取实体表名
	 * @param entityClass
	 * @param mapperHelper
	 * @return
	 */
	public static String getTableName(Class<?> entityClass, MapperHelper mapperHelper) {
        return EntityUtils.getTableName(entityClass, mapperHelper);
	}
	
	/**
	 * 获取实体字段集合
	 * @param entityClass
	 * @return
	 */
	public static List<String> getColumns(Class<?> entityClass) {
		return EntityUtils.getColumns(entityClass);
	}

	/**
	 * 创建默认 JoinExample 实例
	 * 
	 * @param entityClass
	 * @return
	 */
	public static JoinExample create(Class<?> entityClass) {
		return new JoinExample(entityClass, false);
	}
	
	/************** begin and条件 **************/
	
	public Criteria andIsNull(Criteria criteria, String property) {
        return criteria.andCondition(property + " IS NULL");
    }

    public Criteria andIsNotNull(Criteria criteria, String property) {
    	return criteria.andCondition(property + " IS NOT NULL");
    }

    public Criteria andEqualTo(Criteria criteria, String property, Object value) {
        return criteria.andCondition(property + " =",  value);
    }

    public Criteria andNotEqualTo(Criteria criteria, String property, Object value) {
        return criteria.andCondition(property + " <>", value);
    }

    public Criteria andGreaterThan(Criteria criteria, String property, Object value) {
        return criteria.andCondition(property + " >", value);
    }

    public Criteria andGreaterThanOrEqualTo(Criteria criteria, String property, Object value) {
        return criteria.andCondition(property + " >=", value);
    }

    public Criteria andLessThan(Criteria criteria, String property, Object value) {
        return criteria.andCondition(property + " <", value);
    }

    public Criteria andLessThanOrEqualTo(Criteria criteria, String property, Object value) {
        return criteria.andCondition(property + " <=", value);
    }

    public Criteria andIn(Criteria criteria, String property, Iterable value) {
        return criteria.andCondition(property + " IN", value);
    }

    public Criteria andNotIn(Criteria criteria, String property, Iterable value) {
        return criteria.andCondition(property + " NOT IN", value);
    }

    public Criteria andLike(Criteria criteria, String property, String value) {
        return criteria.andCondition(property + " LIKE", value);
    }

    public Criteria andNotLike(Criteria criteria, String property, String value) {
        return criteria.andCondition(property + " NOT LIKE", value);
    }
    
    public Criteria andCondition(Criteria criteria, String condition) {
    	return criteria.andCondition(condition);
    }
    
    public Criteria andCondition(Criteria criteria, String condition, Object value) {
    	return criteria.andCondition(condition, value);
    }
    
    /************** end and条件 **************/
    
	/************** begin or条件 **************/
	
	public Criteria orIsNull(Criteria criteria, String property) {
        return criteria.orCondition(property + " IS NULL");
    }

    public Criteria orIsNotNull(Criteria criteria, String property) {
    	return criteria.orCondition(property + " IS NOT NULL");
    }

    public Criteria orEqualTo(Criteria criteria, String property, Object value) {
        return criteria.orCondition(property + " =",  value);
    }

    public Criteria orNotEqualTo(Criteria criteria, String property, Object value) {
        return criteria.orCondition(property + " <>", value);
    }

    public Criteria orGreaterThan(Criteria criteria, String property, Object value) {
        return criteria.orCondition(property + " >", value);
    }

    public Criteria orGreaterThanOrEqualTo(Criteria criteria, String property, Object value) {
        return criteria.orCondition(property + " >=", value);
    }

    public Criteria orLessThan(Criteria criteria, String property, Object value) {
        return criteria.orCondition(property + " <", value);
    }

    public Criteria orLessThanOrEqualTo(Criteria criteria, String property, Object value) {
        return criteria.orCondition(property + " <=", value);
    }

    public Criteria orIn(Criteria criteria, String property, Iterable value) {
        return criteria.orCondition(property + " IN", value);
    }

    public Criteria orNotIn(Criteria criteria, String property, Iterable value) {
        return criteria.orCondition(property + " NOT IN", value);
    }

    public Criteria orLike(Criteria criteria, String property, String value) {
        return criteria.orCondition(property + " LIKE", value);
    }

    public Criteria orNotLike(Criteria criteria, String property, String value) {
        return criteria.orCondition(property + " NOT LIKE", value);
    }
    
    public Criteria orCondition(Criteria criteria, String condition) {
    	return criteria.orCondition(condition);
    }
    
    public Criteria orCondition(Criteria criteria, String condition, Object value) {
    	return criteria.orCondition(condition, value);
    }
    
    /************** end or条件 **************/
}
