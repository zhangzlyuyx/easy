package com.zhangzlyuyx.easy.mybatis.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhangzlyuyx.easy.mybatis.Condition;
import com.zhangzlyuyx.easy.mybatis.IPageQuery;
import com.zhangzlyuyx.easy.mybatis.IPageResult;
import com.zhangzlyuyx.easy.mybatis.util.MapperUtils;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

public abstract class BaseServiceImpl<T> implements BaseService<T> {
	
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
	 * 保存数据前处理
	 * @param record
	 */
	protected void beforeInsert(T record) {
		
	}
	
	@Override
	public int insert(T record) {
		this.beforeInsert(record);
		return MapperUtils.insert(this.getMapper(), record, true);
	}
	
	@Override
	public int insert(T record, boolean selective) {
		this.beforeInsert(record);
		return MapperUtils.insert(this.getMapper(), record, selective);
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
	public int deleteByEntity(Mapper<T> mapper, T record) {
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
	
	@Override
	public T selectByPrimaryKey(Object key) {
		return MapperUtils.selectByPrimaryKey(this.getMapper(), key);
	}
	
	@Override
	public List<T> selectByIds(String ids) {
		return MapperUtils.selectByIds(this.getMapper(), ids);
	}
	
	@Override
	public T selectFirst(Map<String, Object> queryMap, String orderByClause, String... properties) {
		this.beforeSelect(queryMap);
		List<T> list = MapperUtils.selectByMap(this.getMapper(), this.getEntityClass(), queryMap, 1, 1, orderByClause, properties);
		return list.size() > 0 ? list.get(0) : null;
	}
	
	@Override
	public List<T> selectAll() {
		return MapperUtils.selectAll(this.getMapper());
	}
	
	@Override
	public int selectCountByEntity(T record) {
		this.beforeSelect(record);
		return MapperUtils.selectCountByEntity(this.getMapper(), record);
	}
	
	@Override
	public List<T> selectByEntity(T record) {
		this.beforeSelect(record);
		return MapperUtils.selectByEntity(this.getMapper(), record);
	}
	
	@Override
	public int selectCountByMap(Map<String, Object> queryMap) {
		this.beforeSelect(queryMap);
		return MapperUtils.selectCountByMap(this.getMapper(), this.getEntityClass(), queryMap);
	}
	
	@Override
	public List<T> selectByMap(Map<String, Object> queryMap) {
		this.beforeSelect(queryMap);
		return MapperUtils.selectByMap(this.getMapper(), this.getEntityClass(), queryMap);
	}
	
	@Override
	public List<T> selectByMap(Map<String, Object> queryMap, Integer pageNo, Integer pageSize, String orderByClause, String... properties) {
		this.beforeSelect(queryMap);
		return MapperUtils.selectByMap(this.getMapper(), this.getEntityClass(), queryMap, pageNo, pageSize, orderByClause, properties);
	}
	
	@Override
	public int selectCountByCondition(List<Condition> conditions) {
		this.beforeSelect(conditions);
		return MapperUtils.selectCountByCondition(this.getMapper(), this.getEntityClass(), conditions);
	}
	
	@Override
	public List<T> selectByCondition(List<Condition> conditions) {
		this.beforeSelect(conditions);
		return MapperUtils.selectByCondition(this.getMapper(), this.getEntityClass(), conditions);
	}
	
	@Override
	public List<T> selectByCondition(List<Condition> conditions, Integer pageNo, Integer pageSize, String orderByClause, String... properties) {
		this.beforeSelect(conditions);
		return MapperUtils.selectByCondition(this.getMapper(), this.getEntityClass(), conditions, pageNo, pageSize, orderByClause, properties);
	}
	
	@Override
	public IPageResult<T> selectByPage(IPageQuery pageQuery) {
		this.beforeSelect(pageQuery);
		return MapperUtils.selectByPage(this.getMapper(), this.getEntityClass(), pageQuery);
	}
	
	@Override
	public int selectCountByExample(Example example) {
		return MapperUtils.selectCountByExample(this.getMapper(), example);
	}
	
	@Override
	public List<T> selectByExample(Example example) {
		return MapperUtils.selectByExample(this.getMapper(), example);
	}
	
	@Override
	public List<T> selectByExample(Example example, Integer pageNo, Integer pageSize) {
		return MapperUtils.selectByExample(this.getMapper(), example, pageNo, pageSize);
	}
	
	@Override
	public List<T> selectListBySql(String sql, Object parameter) {
		SqlSession sqlSession = this.getSqlSession();
		String msId = MapperUtils.getMappedStatementId(sqlSession, SqlCommandType.SELECT, sql, (parameter != null ? parameter.getClass() : null), this.getEntityClass());
		return sqlSession.selectList(msId, parameter);
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
