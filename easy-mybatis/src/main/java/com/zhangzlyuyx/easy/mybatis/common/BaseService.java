package com.zhangzlyuyx.easy.mybatis.common;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zhangzlyuyx.easy.mybatis.Condition;
import com.zhangzlyuyx.easy.mybatis.IPageQuery;
import com.zhangzlyuyx.easy.mybatis.IPageResult;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

/**
 * base service 接口
 * @author zhangzlyuyx
 *
 * @param <T>
 */
public interface BaseService<T> {
	
	/**
	 * 获取 mapper
	 * @return
	 */
	BaseMapper<T> getMapper();
	
	/**
	 * 获取实体 class
	 * @return
	 */
	Class<T> getEntityClass();
	
	/**
	 * 获取实体映射的数据库表
	 * @return
	 */
	String getEntityTable();

	/**
	 * 保存一个实体
	 * @param record 实体
	 * @return
	 */
	int insert(T record);
	
	/**
	 * 保存一个实体
	 * @param record 实体
	 * @param selective 是否选择性保存(true:null的属性不会保存 false:null的属性也会保存)
	 * @return
	 */
	int insert(T record, boolean selective);
	
	/**
	 * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等，另外该接口限制实体包含`id`属性并且必须为自增列
	 * @param recordList 实体集合
	 * @return
	 */
	int insertList(List<T> recordList);
	
	/**
	 * 执行 insert sql 语句
	 * @param sql
	 * @param parameter
	 * @return
	 */
	int insertBySql(String sql, Object parameter);
	
	/**
	 * 根据主键条件删除数据 
	 * @param key 主键查询条件 
	 * @return
	 */
	int deleteByPrimaryKey(Object key);
	
	/**
	 * 根据主键字符串进行删除，类中只有存在一个带有@Id注解的字段
	 * @param ids ids 如 "1,2,3,4"
	 * @return
	 */
	int deleteByIds(String ids);
	
	/**
	 * 根据实体条件删除数据
	 * @param record 实体查询条件
	 * @return
	 */
	int deleteByEntity(Mapper<T> mapper, T record);
	
	/**
	 * 根据map条件删除数据
	 * @param queryMap map查询条件
	 * @return
	 */
	int deleteByMap(Map<String, Object> queryMap);
	
	/**
	 * 根据condition条件删除数据
	 * @param conditions condition查询条件
	 * @return
	 */
	int deleteByCondition(List<Condition> conditions);
	
	/**
	 * 根据example条件删除数据
	 * @param example example查询条件
	 * @return
	 */
	int deleteByExample(Example example);
	
	/**
	 * 执行 delete sql 语句
	 * @param sql
	 * @param parameter
	 * @return
	 */
	int deleteBySql(String sql, Object parameter);
	
	/**
	 * 根据主键条件更新实体
	 * @param record 包含主键的实体
	 * @return
	 */
	int updateByPrimaryKey(T record);
	
	/**
	 * 根据主键条件更新实体
	 * @param record 包含主键的实体
	 * @param selective 是否选择性(true:null的值不更新 false:null值会被更新)
	 * @return
	 */
	int updateByPrimaryKey(T record, boolean selective);
	
	/**
	 * 根据map条件更新实体
	 * @param entity 更新结果
	 * @param queryMap 查询条件
	 * @return
	 */
	int updateByMap(T entity, Map<String, Object> queryMap);
	
	/**
	 * 根据map条件更新实体
	 * @param entity 更新结果
	 * @param queryMap 查询条件
	 * @param selective 是否选择性(true:null的值不更新 false:null值会被更新)
	 * @return
	 */
	int updateByMap(T entity, Map<String, Object> queryMap, boolean selective);
	
	/**
	 *根据condition条件更新实体
	 * @param entity 更新结果
	 * @param conditions 更新查询条件
	 * @return
	 */
	int updateByCondition(T entity, List<Condition> conditions);
	
	/**
	 *根据condition条件更新实体
	 * @param entity 更新结果
	 * @param conditions 更新查询条件
	 * @param selective 是否选择性(true:null的值不更新 false:null值会被更新)
	 * @return
	 */
	int updateByCondition(T entity, List<Condition> conditions, boolean selective);
	
	/**
	 * 根据example条件更新实体
	 * @param entity 实体
	 * @param example example条件
	 * @return
	 */
	int updateByExample(T entity, Example example);
	
	/**
	 * 根据example条件更新实体
	 * @param entity 实体
	 * @param example example条件
	 * @param selective 是否选择性(true:null的值不更新 false:null值会被更新)
	 * @return
	 */
	int updateByExample(T entity, Example example, boolean selective);
	
	/**
	 * 更新实体列表
	 * @param list
	 * @return
	 */
	int updateList(List<T> list);
	
	/**
	 * 执行 update sql 语句
	 * @param sql
	 * @param parameter
	 * @return
	 */
	int updateBySql(String sql, Object parameter);
	
	/**
	 * 根据主键查询实体
	 * @param key 主键值
	 * @return
	 */
	T selectByPrimaryKey(Object key);
	
	/**
	 * 根据主键字符串进行查询，类中只有存在一个带有@Id注解的字段
	 * @param ids ids 如 "1,2,3,4"
	 * @return
	 */
	List<T> selectByIds(String ids);
	
	/**
	 * 查询首个数据
	 * @param queryMap 查询条件
	 * @param orderByClause 排序
	 * @param properties 筛选的属性
	 * @return
	 */
	T selectFirst(Map<String, Object> queryMap, String orderByClause, String... properties);
	
	/**
	 * 查询全部结果
	 * @return
	 */
	List<T> selectAll();
	
	/**
	 * 根据实体中的属性查询总数
	 * @param record 查询的实体条件(等号匹配)
	 * @return
	 */
	int selectCountByEntity(T record);
	
	/**
	 * 根据实体查询结果集合
	 * @param record 查询的实体条件(等号匹配)
	 * @return
	 */
	List<T> selectByEntity(T record);
	
	/**
	 * 根据条件查询总数
	 * @param queryMap map查询条件
	 * @return
	 */
	int selectCountByMap(Map<String, Object> queryMap);
	
	/**
	 * 查询结果集合
	 * @param queryMap 查询条件
	 * @return
	 */
	List<T> selectByMap(Map<String, Object> queryMap);
	
	/**
	 * 查询结果集合
	 * @param queryMap 查询条件
	 * @param pageNo 分页页码(从1开始)
	 * @param pageSize 分页每页记录数
	 * @param orderByClause 排序
	 * @param properties 筛选结果属性
	 * @return
	 */
	List<T> selectByMap(Map<String, Object> queryMap, Integer pageNo, Integer pageSize, String orderByClause, String... properties);
	
	/**
	 * 根据条件查询总数
	 * @param conditions
	 * @return
	 */
	int selectCountByCondition(List<Condition> conditions);
	
	/**
	 * 查询结果集合
	 * @param conditions 查询条件
	 * @return
	 */
	List<T> selectByCondition(List<Condition> conditions);
	
	/**
	 * 查询结果集合
	 * @param conditions 查询条件
	 * @param pageNo 分页页码(从1开始)
	 * @param pageSize 分页每页记录数
	 * @param orderByClause 排序
	 * @param properties 筛选结果属性
	 * @return
	 */
	List<T> selectByCondition(List<Condition> conditions, Integer pageNo, Integer pageSize, String orderByClause, String... properties);
	
	/**
	 * 查询分页结果
	 * @param pageQuery 分页查询条件
	 * @return
	 */
	IPageResult<T> selectByPage(IPageQuery pageQuery);
	
	/**
	 * 查询结果记录数
	 * @param example 查询条件
	 * @return
	 */
	int selectCountByExample(Example example);
	
	/**
	 * 查询结果集合
	 * @param example 查询条件
	 * @return
	 */
	List<T> selectByExample(Example example);
	
	/**
	 * 查询结果集合
	 * @param example 查询条件
	 * @param pageNo 分页页码(从1开始)
	 * @param pageSize 分页每页记录数
	 * @return
	 */
	List<T> selectByExample(Example example, Integer pageNo, Integer pageSize);
	
	/**
	 * 执行 select sql 查询实体列表
	 * @param sql
	 * @param parameter
	 * @return
	 */
	List<T> selectListBySql(String sql, Object parameter);
	
	/**
	 * 执行 select sql 查询map列表
	 * @param sql
	 * @param parameter
	 * @return
	 */
	List<Map<String, Object>> selectMapListBySql(String sql, Object parameter);
	
	/**
	 * 执行 select sql 查询指定类型列表
	 * @param sql
	 * @param parameter
	 * @param resultType
	 * @return
	 */
	<E> List<E> selectListBySql(String sql, Object parameter, Class<E> resultType);
	
	/**
	 * 获取 mysql 服务器时间
	 * @return
	 */
	Date getMySqlDate();
	
	/**
	 * 获取 oracle 服务器时间
	 * @return
	 */
	Date getOracleDate();
	
	/**
	 * 获取 sqlserver 服务器时间
	 * @return
	 */
	Date getSqlServerDate();
}
