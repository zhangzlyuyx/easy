package com.zhangzlyuyx.easy.mybatis.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhangzlyuyx.easy.mybatis.Condition;
import com.zhangzlyuyx.easy.mybatis.IPageQuery;
import com.zhangzlyuyx.easy.mybatis.IPageResult;
import com.zhangzlyuyx.easy.mybatis.PageResult;
import com.zhangzlyuyx.easy.mybatis.entity.JoinExample;
import com.zhangzlyuyx.easy.mybatis.enums.DbType;
import com.zhangzlyuyx.easy.mybatis.util.MapperUtils;

import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

public abstract class BaseServiceImpl<T> implements BaseService<T> {
	
	private static final Logger log = LoggerFactory.getLogger(BaseServiceImpl.class);
	
	@Autowired(required = false)
	private SqlSessionFactory sqlSessionFactory;
	
	public abstract BaseMapper<T> getMapper();
	
	protected Class<T> entityClass;
	
	/**
	 * 获取实体类型
	 * 
	 * @return 返回实体类型
	 */
	@SuppressWarnings("unchecked")
	@Override
	public synchronized Class<T> getEntityClass() {
		if(this.entityClass == null && this.getClass().getGenericSuperclass() instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType)this.getClass().getGenericSuperclass();
			Type argType = paramType.getActualTypeArguments().length > 0 ? paramType.getActualTypeArguments()[0] : null;
			if(argType != null && argType instanceof Class) {
				this.entityClass = (Class<T>)argType;
			}
		}
		return this.entityClass;
	}
	
	@Override
	public String getEntityTable() {
		Class<T> entityClass = this.getEntityClass();
		EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
		return entityTable != null ? entityTable.getName() : null;
	}
	
	/**
	 * 获取 sql 会话
	 * @return
	 */
	public SqlSession getSqlSession() {
		SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
		if(sqlSession == null) {
			sqlSession = sqlSessionFactory.openSession();
		}
		return sqlSession;
	}
	
	/**
	 * 获取 sql 数据库连接
	 * @return
	 */
	public Connection getSqlConnection() {
		return this.getSqlSession().getConnection();
	}
	
	@Override
	public DbType getDbType() {
		try {
			String driverName = this.getSqlConnection().getMetaData().getDriverName();
			//if(driverName.toUpperCase().indexOf("MySQL") > -1) {
			if(driverName.toUpperCase().indexOf("MYSQL") > -1) {
				return DbType.mysql;
			} else if(driverName.toUpperCase().indexOf("SQL SERVER") > -1 || driverName.toUpperCase().indexOf("SQLSERVER") > -1) {
				return DbType.sqlserver;
			} else if(driverName.toUpperCase().indexOf("ORACLE") > -1) {
				return DbType.oracle;
			} else {
				return DbType.unkown;
			}
		} catch (Exception e) {
			log.error("", e);
			return DbType.unkown;
		}
	}
	
	/**
	 * 保存数据前处理
	 * @param record
	 */
	protected void beforeInsert(T record) {
		
	}
	
	/**
	 * 保存数据后处理
	 * @param insertCount
	 * @param record
	 * @return
	 */
	protected int afterInsert(int insertCount, T... records) {
		
		return insertCount;
	}
	
	@Override
	public int insert(T record) {
		this.beforeInsert(record);
		int insertCount = MapperUtils.insert(this.getMapper(), record, true);
		return this.afterInsert(insertCount, record);
	}
	
	@Override
	public int insert(T record, boolean selective) {
		this.beforeInsert(record);
		int insertCount = MapperUtils.insert(this.getMapper(), record, selective);
		return this.afterInsert(insertCount, record);
	}
	
	@Override
	public int insertList(List<T> recordList) {
		if(recordList != null && recordList.size() > 0) {
			for(T record : recordList) {
				this.beforeInsert(record);
			}
		}
		return MapperUtils.insertList(this.getMapper(), recordList);
	}
	
	@Override
	public int insertBySql(String sql, Object parameter) {
		SqlSession sqlSession = this.getSqlSession();
		String msId = MapperUtils.getMappedStatementId(sqlSession, SqlCommandType.INSERT, sql, (parameter != null ? parameter.getClass() : null), int.class);
		return sqlSession.insert(msId, parameter);
	}
	
	@Override
	public int deleteByPrimaryKey(Object key) {
		return MapperUtils.deleteByPrimaryKey(this.getMapper(), key);
	}
	
	@Override
	public int deleteByIds(String ids) {
		return MapperUtils.deleteByIds(this.getMapper(), ids);
	}
	
	@Override
	public int deleteByEntity(T record) {
		return MapperUtils.deleteByEntity(this.getMapper(), record);
	}
	
	@Override
	public int deleteByMap(Map<String, Object> queryMap) {
		return MapperUtils.deleteByMap(this.getMapper(), this.getEntityClass(), queryMap);
	}
	
	@Override
	public int deleteByCondition(List<Condition> conditions) {
		return MapperUtils.deleteByCondition(this.getMapper(), this.getEntityClass(), conditions);
	}
	
	@Override
	public int deleteByExample(Example example) {
		return MapperUtils.deleteByExample(this.getMapper(), example);
	}
	
	@Override
	public int deleteBySql(String sql, Object parameter) {
		SqlSession sqlSession = this.getSqlSession();
		String msId = MapperUtils.getMappedStatementId(sqlSession, SqlCommandType.DELETE, sql, (parameter != null ? parameter.getClass() : null), int.class);
		return sqlSession.delete(msId, parameter);
	}
	
	/**
	 * 更新前处理
	 * @param record
	 */
	protected void beforeUpdate(T record) {
		
	}
	
	@Override
	public int updateByPrimaryKey(T record) {
		this.beforeUpdate(record);
		return MapperUtils.updateByPrimaryKey(this.getMapper(), record, true);
	}
	
	@Override
	public int updateByPrimaryKey(T record, boolean selective) {
		this.beforeUpdate(record);
		return MapperUtils.updateByPrimaryKey(this.getMapper(), record, selective);
	}
	
	@Override
	public int updateByMap(T entity, Map<String, Object> queryMap) {
		this.beforeUpdate(entity);
		return MapperUtils.updateByMap(this.getMapper(), entity, queryMap, true);
	}
	
	@Override
	public int updateByMap(T entity, Map<String, Object> queryMap, boolean selective) {
		this.beforeUpdate(entity);
		return MapperUtils.updateByMap(this.getMapper(), entity, queryMap, selective);
	}
	
	@Override
	public int updateByCondition(T entity, List<Condition> conditions) {
		this.beforeUpdate(entity);
		return MapperUtils.updateByCondition(this.getMapper(), entity, conditions, true);
	}
	
	@Override
	public int updateByCondition(T entity, List<Condition> conditions, boolean selective) {
		this.beforeUpdate(entity);
		return MapperUtils.updateByCondition(this.getMapper(), entity, conditions, selective);
	}
	
	@Override
	public int updateByExample(T entity, Example example) {
		this.beforeUpdate(entity);
		return MapperUtils.updateByExample(this.getMapper(), entity, example, true);
	}
	
	@Override
	public int updateByExample(T entity, Example example, boolean selective) {
		this.beforeUpdate(entity);
		return MapperUtils.updateByExample(this.getMapper(), entity, example, selective);
	}
	
	@Override
	public int updateList(List<T> list) {
		int total = 0;
		for(T entity : list) {
			int count = this.updateByPrimaryKey(entity);
			if(count <= 0) {
				throw new RuntimeException("更新实体失败!");
			}
			total += count;
		}
		return total;
	}
	
	@Override
	public int updateBySql(String sql, Object parameter) {
		SqlSession sqlSession = this.getSqlSession();
		String msId = MapperUtils.getMappedStatementId(sqlSession, SqlCommandType.UPDATE, sql, (parameter != null ? parameter.getClass() : null), int.class);
		return sqlSession.update(msId, parameter);
	}
	
	/**
	 * 查询前处理
	 * @param queryMap
	 */
	protected void beforeSelect(Map<String, Object> queryMap) {
		
	}
	
	/**
	 * 查询前处理
	 * @param record
	 */
	protected void beforeSelect(T record) {
		
	}
	
	/**
	 * 查询前处理
	 * @param conditions
	 */
	protected void beforeSelect(List<Condition> conditions) {
		
	}
	
	/**
	 * 查询前处理
	 * @param pageQuery
	 */
	protected void beforeSelect(IPageQuery pageQuery) {
		
	}
	
	/**
	 * 查询后处理
	 * @param entity
	 * @return
	 */
	protected T afterSelect(T entity) {
		return entity;
	}
	
	/**
	 * 查询后处理
	 * @param list
	 * @return
	 */
	protected List<T> afterSelect(List<T> list) {
		return list;
	}
	
	@Override
	public T selectByPrimaryKey(Object key) {
		return this.afterSelect(MapperUtils.selectByPrimaryKey(this.getMapper(), key));
	}
	
	@Override
	public List<T> selectByIds(String ids) {
		return this.afterSelect(MapperUtils.selectByIds(this.getMapper(), ids));
	}
	
	@Override
	public T selectFirst(Map<String, Object> queryMap, String orderByClause, String... properties) {
		this.beforeSelect(queryMap);
		List<T> list = MapperUtils.selectByMap(this.getMapper(), this.getEntityClass(), queryMap, 1, 1, orderByClause, properties);
		return this.afterSelect(list.size() > 0 ? list.get(0) : null);
	}
	
	@Override
	public T selectByUnique(String column, Object value, String... properties) {
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put(column, value);
		return this.selectFirst(queryMap, null, properties);
	}
	
	@Override
	public int selectCountByUnique(String column, Object value) {
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put(column, value);
		return MapperUtils.selectCountByMap(this.getMapper(), this.getEntityClass(), queryMap);
	}
	
	@Override
	public List<T> selectAll() {
		return this.afterSelect(MapperUtils.selectAll(this.getMapper()));
	}
	
	@Override
	public int selectCountByEntity(T record) {
		this.beforeSelect(record);
		return MapperUtils.selectCountByEntity(this.getMapper(), record);
	}
	
	@Override
	public List<T> selectByEntity(T record) {
		this.beforeSelect(record);
		return this.afterSelect(MapperUtils.selectByEntity(this.getMapper(), record));
	}
	
	@Override
	public int selectCountByMap(Map<String, Object> queryMap) {
		this.beforeSelect(queryMap);
		return MapperUtils.selectCountByMap(this.getMapper(), this.getEntityClass(), queryMap);
	}
	
	@Override
	public List<T> selectByMap(Map<String, Object> queryMap) {
		this.beforeSelect(queryMap);
		return this.afterSelect(MapperUtils.selectByMap(this.getMapper(), this.getEntityClass(), queryMap));
	}
	
	@Override
	public List<T> selectByMap(Map<String, Object> queryMap, Integer pageNo, Integer pageSize, String orderByClause, String... properties) {
		this.beforeSelect(queryMap);
		return this.afterSelect(MapperUtils.selectByMap(this.getMapper(), this.getEntityClass(), queryMap, pageNo, pageSize, orderByClause, properties));
	}
	
	@Override
	public int selectCountByCondition(List<Condition> conditions) {
		this.beforeSelect(conditions);
		return MapperUtils.selectCountByCondition(this.getMapper(), this.getEntityClass(), conditions);
	}
	
	@Override
	public List<T> selectByCondition(List<Condition> conditions) {
		this.beforeSelect(conditions);
		return this.afterSelect(MapperUtils.selectByCondition(this.getMapper(), this.getEntityClass(), conditions));
	}
	
	@Override
	public List<T> selectByCondition(List<Condition> conditions, Integer pageNo, Integer pageSize, String orderByClause, String... properties) {
		this.beforeSelect(conditions);
		return this.afterSelect(MapperUtils.selectByCondition(this.getMapper(), this.getEntityClass(), conditions, pageNo, pageSize, orderByClause, properties));
	}
	
	@Override
	public IPageResult<T> selectByPage(IPageQuery pageQuery) {
		this.beforeSelect(pageQuery);
		IPageResult<T> pageResult = MapperUtils.selectByPage(this.getMapper(), this.getEntityClass(), pageQuery);
		pageResult.setRows(this.afterSelect(pageResult.getRows()));
		return pageResult;
	}
	
	@Override
	public int selectCountByExample(Example example) {
		return MapperUtils.selectCountByExample(this.getMapper(), example);
	}
	
	@Override
	public List<T> selectByExample(Example example) {
		return this.afterSelect(MapperUtils.selectByExample(this.getMapper(), example));
	}
	
	@Override
	public List<T> selectByExample(Example example, Integer pageNo, Integer pageSize) {
		return this.afterSelect(MapperUtils.selectByExample(this.getMapper(), example, pageNo, pageSize));
	}
	
	@Override
	public List<T> selectListBySql(String sql, Object parameter) {
		SqlSession sqlSession = this.getSqlSession();
		String msId = MapperUtils.getMappedStatementId(sqlSession, SqlCommandType.SELECT, sql, (parameter != null ? parameter.getClass() : null), this.getEntityClass());
		List<T> list = sqlSession.selectList(msId, parameter);	
		return this.afterSelect(list);
	}
	
	@Override
	public List<Map<String, Object>> selectMapListBySql(String sql, Object parameter) {
		SqlSession sqlSession = this.getSqlSession();
		String msId = MapperUtils.getMappedStatementId(sqlSession, SqlCommandType.SELECT, sql, (parameter != null ? parameter.getClass() : null), Map.class);
		return sqlSession.selectList(msId, parameter);
	}
	
	@Override
	public <E> List<E> selectListBySql(String sql, Object parameter, Class<E> resultType) {
		SqlSession sqlSession = this.getSqlSession();
		String msId = MapperUtils.getMappedStatementId(sqlSession, SqlCommandType.SELECT, sql, (parameter != null ? parameter.getClass() : null), resultType);
		return sqlSession.selectList(msId, parameter);
	}
	
	@Override
	public Integer selectCountByJoinExample(JoinExample joinExample) {
		return this.getMapper().selectCountByJoinExample(joinExample);
	}
	
	@Override
	public List<T> selectByJoinExample(JoinExample joinExample, Integer pageNo, Integer pageSize) {
		if(pageNo != null && pageSize != null) {
			int offset = (pageNo - 1) * pageSize;
			RowBounds rowBounds = new RowBounds(offset, pageSize);
			return this.getMapper().selectByJoinExampleAndRowBounds(joinExample, rowBounds);
		} else {
			return this.getMapper().selectByJoinExample(joinExample);
		}
	}
	
	@Override
	public List<Map<String, Object>> selectMapByJoinExample(JoinExample joinExample, Integer pageNo, Integer pageSize) {
		if(pageNo != null && pageSize != null) {
			int offset = (pageNo - 1) * pageSize;
			RowBounds rowBounds = new RowBounds(offset, pageSize);
			return this.getMapper().selectMapByJoinExampleAndRowBounds(joinExample, rowBounds);
		} else {
			return this.getMapper().selectMapByJoinExample(joinExample);
		}
	}
	
	@Override
	public <P> IPageResult<P> selectByPage(Class<P> clazz, JoinExample joinExample, Integer pageNo, Integer pageSize) {
    	List<Map<String, Object>> list = null;
    	if (pageNo != null && pageNo != null) {
			int offset = (pageNo - 1) * pageSize;
			RowBounds rowBounds = new RowBounds(offset, pageSize);
			list = this.getMapper().selectMapByJoinExampleAndRowBounds(joinExample, rowBounds);
		} else {
			list = this.getMapper().selectMapByJoinExample(joinExample);
		}
    	Long total = MapperUtils.getPageHelperTotal(list);
    	if(total == null) {
    		total = Long.valueOf(this.getMapper().selectCountByJoinExample(joinExample));
    	}
    	//int count = this.getMapper().selectCountByJoinExample(joinExample);
    	PageResult<P> pageResult = new PageResult<>();
    	pageResult.setPageNo(pageNo);
    	pageResult.setPageSize(pageSize);
    	//pageResult.setTotal(Long.parseLong(String.valueOf(count)));
    	pageResult.setTotal(total);
    	for(Map<String, Object> item : list) {
    		pageResult.getRows().add(JSON.toJavaObject((JSON)JSONObject.toJSON(item), clazz));
    	}
    	return pageResult;
	}
	
	@Override
	public Date getDate() {
		DbType dbType = this.getDbType();
		if(dbType.equals(DbType.mysql)) {
			return this.getMySqlDate();
		} else if(dbType.equals(DbType.sqlserver)) {
			return this.getSqlServerDate();
		} else if(dbType.equals(DbType.oracle)) {
			return this.getOracleDate();
		} else {
			return null;
		}
	}
	
	@Override
	public Date getMySqlDate() {
		List<Date> list = this.selectListBySql("select sysdate()", null, Date.class);
		return (list != null && list.size() > 0) ? list.get(0) : null;
	}
	
	@Override
	public Date getOracleDate() {
		List<Date> list = this.selectListBySql("select sysdate from dual", null, Date.class);
		return (list != null && list.size() > 0) ? list.get(0) : null;
	}
	
	@Override
	public Date getSqlServerDate() {
		List<Date> list = this.selectListBySql("select getdate()", null, Date.class);
		return (list != null && list.size() > 0) ? list.get(0) : null;
	}
}
