package com.zhangzlyuyx.easy.mybatis.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.zhangzlyuyx.easy.mybatis.Condition;
import com.zhangzlyuyx.easy.mybatis.IPageQuery;
import com.zhangzlyuyx.easy.mybatis.IPageResult;
import com.zhangzlyuyx.easy.mybatis.util.MapperUtils;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

public abstract class BaseServiceImpl<T> implements BaseService<T> {
	
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
	public int insert(T record) {
		return this.insert(record, true);
	}
	
	@Override
	public int insert(T record, boolean selective) {
		return MapperUtils.insert(this.getMapper(), record, selective);
	}
	
	@Override
	public int insertList(List<T> recordList) {
		return MapperUtils.insertList(this.getMapper(), recordList);
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
	
	@Override
	public int updateByPrimaryKey(T record) {
		return this.updateByPrimaryKey(record, true);
	}
	
	@Override
	public int updateByPrimaryKey(T record, boolean selective) {
		return MapperUtils.updateByPrimaryKey(this.getMapper(), record, selective);
	}
	
	@Override
	public int updateByMap(T entity, Map<String, Object> queryMap) {
		return this.updateByMap(entity, queryMap, true);
	}
	
	@Override
	public int updateByMap(T entity, Map<String, Object> queryMap, boolean selective) {
		return MapperUtils.updateByMap(this.getMapper(), entity, queryMap, selective);
	}
	
	@Override
	public int updateByCondition(T entity, List<Condition> conditions) {
		return this.updateByCondition(entity, conditions, true);
	}
	
	@Override
	public int updateByCondition(T entity, List<Condition> conditions, boolean selective) {
		return MapperUtils.updateByCondition(this.getMapper(), entity, conditions, selective);
	}
	
	@Override
	public int updateByExample(T entity, Example example) {
		return this.updateByExample(entity, example, true);
	}
	
	@Override
	public int updateByExample(T entity, Example example, boolean selective) {
		return MapperUtils.updateByExample(this.getMapper(), entity, example, selective);
	}
	
	public T selectByPrimaryKey(Object key) {
		return MapperUtils.selectByPrimaryKey(this.getMapper(), key);
	}
	
	public List<T> selectByIds(String ids) {
		return MapperUtils.selectByIds(this.getMapper(), ids);
	}
	
	public List<T> selectAll() {
		return MapperUtils.selectAll(this.getMapper());
	}
	
	public int selectCountByEntity(T record) {
		return MapperUtils.selectCountByEntity(this.getMapper(), record);
	}
	
	public List<T> selectByEntity(T record) {
		return MapperUtils.selectByEntity(this.getMapper(), record);
	}
	
	public int selectCountByMap(Map<String, Object> queyMap) {
		return MapperUtils.selectCountByMap(this.getMapper(), this.getEntityClass(), queyMap);
	}
	
	public List<T> selectByMap(Map<String, Object> queryMap) {
		return MapperUtils.selectByMap(this.getMapper(), this.getEntityClass(), queryMap);
	}
	
	public List<T> selectByMap(Map<String, Object> queryMap, Integer pageNo, Integer pageSize, String orderByClause, String... properties) {
		return MapperUtils.selectByMap(this.getMapper(), this.getEntityClass(), queryMap, pageNo, pageSize, orderByClause, properties);
	}
	
	public int selectCountByCondition(List<Condition> conditions) {
		return MapperUtils.selectCountByCondition(this.getMapper(), this.getEntityClass(), conditions);
	}
	
	public List<T> selectByCondition(List<Condition> conditions) {
		return MapperUtils.selectByCondition(this.getMapper(), this.getEntityClass(), conditions);
	}
	
	public List<T> selectByCondition(List<Condition> conditions, Integer pageNo, Integer pageSize, String orderByClause, String... properties) {
		return MapperUtils.selectByCondition(this.getMapper(), this.getEntityClass(), conditions, pageNo, pageSize, orderByClause, properties);
	}
	
	public IPageResult<T> selectByPage(IPageQuery pageQuery) {
		return MapperUtils.selectByPage(this.getMapper(), this.getEntityClass(), pageQuery);
	}
	
	public int selectCountByExample(Example example) {
		return MapperUtils.selectCountByExample(this.getMapper(), example);
	}
	
	public List<T> selectByExample(Example example) {
		return MapperUtils.selectByExample(this.getMapper(), example);
	}
	
	public List<T> selectByExample(Example example, Integer pageNo, Integer pageSize) {
		return MapperUtils.selectByExample(this.getMapper(), example, pageNo, pageSize);
	}
}
