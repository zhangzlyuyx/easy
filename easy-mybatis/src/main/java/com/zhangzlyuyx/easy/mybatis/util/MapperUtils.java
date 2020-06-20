package com.zhangzlyuyx.easy.mybatis.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.session.RowBounds;

import com.zhangzlyuyx.easy.mybatis.Condition;
import com.zhangzlyuyx.easy.mybatis.IPageQuery;
import com.zhangzlyuyx.easy.mybatis.IPageResult;
import com.zhangzlyuyx.easy.mybatis.PageResult;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * mapper 工具类
 *
 */
public class MapperUtils {

	/******************** begin insert ********************/
	
	/**
	 * 保存一个实体
	 * @param mapper mapper
	 * @param record 实体
	 * @param selective 是否选择性保存(true:null的属性不会保存 false:null的属性也会保存)
	 * @return
	 */
	public static <T> int insert(Mapper<T> mapper, T record, boolean selective) {
		if(selective) {
			return mapper.insertSelective(record);
		} else {
			return mapper.insert(record);
		}
	}
	
	/**
	 * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等，另外该接口限制实体包含`id`属性并且必须为自增列
	 * @param mapper mapper
	 * @param recordList 实体集合
	 * @return
	 */
	public static <T> int insertList(MySqlMapper<T> mapper, List<T> recordList) {
		return mapper.insertList(recordList);
	}
	
	/******************** end insert ********************/
	
	/******************** begin delete ********************/
	
	/**
	 * 根据主键条件删除数据 
	 * @param mapper mapper
	 * @param key 主键查询条件 
	 * @return
	 */
	public static <T> int deleteByPrimaryKey(Mapper<T> mapper, Object key) {
		return mapper.deleteByPrimaryKey(key);
	}
	
	/**
	 * 根据主键字符串进行删除，类中只有存在一个带有@Id注解的字段
	 * @param mapper mapper
	 * @param ids ids 如 "1,2,3,4"
	 * @return
	 */
	public static <T> int deleteByIds(IdsMapper<T> mapper, String ids) {
		return mapper.deleteByIds(ids);
	}
	
	/**
	 * 根据实体条件删除数据
	 * @param mapper mapper
	 * @param record 实体查询条件
	 * @return
	 */
	public static <T> int deleteByEntity(Mapper<T> mapper, T record) {
		return mapper.delete(record);
	}
	
	/**
	 * 根据map条件删除数据
	 * @param mapper mapper
	 * @param enityClass 实体类型
	 * @param queryMap map查询条件
	 * @return
	 */
	public static <T> int deleteByMap(Mapper<T> mapper, Class<?> enityClass, Map<String, Object> queryMap) {
		//禁止无条件删除
		if(queryMap == null || queryMap.size() == 0) {
			throw new RuntimeException("删除数据查询条件不能为空");
		}
		return mapper.deleteByExample(createExample(enityClass, queryMap));
	}
	
	/**
	 * 根据condition条件删除数据
	 * @param mapper mapper
	 * @param enityClass 实体类型
	 * @param conditions condition查询条件
	 * @return
	 */
	public static <T> int deleteByCondition(Mapper<T> mapper, Class<?> enityClass, List<Condition> conditions) {
		//禁止无条件删除
		if(conditions == null || conditions.size() == 0) {
			throw new RuntimeException("删除数据查询条件不能为空");
		}
		return mapper.deleteByExample(createExample(enityClass, conditions));
	}
	
	/**
	 * 根据example条件删除数据
	 * @param mapper mapper
	 * @param example example查询条件
	 * @return
	 */
	public static <T> int deleteByExample(Mapper<T> mapper, Example example) {
		return mapper.deleteByExample(example);
	}
	
	/******************** end delete ********************/
	
	/******************** begin update ********************/
	
	/**
	 * 根据主键条件更新实体
	 * @param mapper
	 * @param record 包含主键的实体
	 * @param selective 是否选择性(true:null的值不更新 false:null值会被更新)
	 * @return
	 */
	public static <T> int updateByPrimaryKey(Mapper<T> mapper, T record, boolean selective) {
		if(selective) {
			return mapper.updateByPrimaryKeySelective(record);
		} else {
			return mapper.updateByPrimaryKey(record);
		}
	}
	
	/**
	 * 根据map条件更新实体
	 * @param mapper
	 * @param entity 更新结果
	 * @param queryMap 查询条件
	 * @param selective 是否选择性(true:null的值不更新 false:null值会被更新)
	 * @return
	 */
	public static <T> int updateByMap(Mapper<T> mapper, T entity, Map<String, Object> queryMap, boolean selective) {
		//禁止无条件更新
		if(queryMap == null || queryMap.size() == 0) {
			throw new RuntimeException("更新数据查询条件不能为空");
		}
		return updateByExample(mapper, entity, createExample(entity.getClass(), queryMap), selective);
	}
	
	/**
	 *根据condition条件更新实体
	 * @param mapper
	 * @param entity 更新结果
	 * @param conditions 更新查询条件
	 * @param selective 是否选择性(true:null的值不更新 false:null值会被更新)
	 * @return
	 */
	public static <T> int updateByCondition(Mapper<T> mapper, T entity, List<Condition> conditions, boolean selective) {
		//禁止无条件更新
		if(conditions == null || conditions.size() == 0) {
			throw new RuntimeException("更新数据查询条件不能为空");
		}
		return updateByExample(mapper, entity, createExample(entity.getClass(), conditions), selective);
	}
	
	/**
	 * 根据example条件更新实体
	 * @param mapper mapper
	 * @param entity 实体
	 * @param example example条件
	 * @param selective 是否选择性(true:null的值不更新 false:null值会被更新)
	 * @return
	 */
	public static <T> int updateByExample(Mapper<T> mapper, T entity, Example example, boolean selective) {
		return mapper.updateByExample(entity, example);
	}
	
	/******************** end update ********************/
	
	/******************** begin select ********************/
	
	/**
	 * 根据主键查询实体
	 * @param mapper mapper
	 * @param key 主键值
	 * @return
	 */
	public static <T> T selectByPrimaryKey(Mapper<T> mapper, Object key) {
		return mapper.selectByPrimaryKey(key);
	}
	
	/**
	 * 根据主键字符串进行查询，类中只有存在一个带有@Id注解的字段
	 * @param mapper
	 * @param ids ids 如 "1,2,3,4"
	 * @return
	 */
	public static <T> List<T> selectByIds(IdsMapper<T> mapper, String ids) {
		return mapper.selectByIds(ids);
	}
	
	/**
	 * 查询全部结果
	 * @param mapper
	 * @return
	 */
	public static <T> List<T> selectAll(Mapper<T> mapper){
		return mapper.selectAll();
	}
	
	/**
	 * 根据实体中的属性查询总数
	 * @param mapper
	 * @param record 查询的实体条件(等号匹配)
	 * @return
	 */
	public static <T> int selectCountByEntity(Mapper<T> mapper, T record){
		return mapper.selectCount(record);
	}
	
	/**
	 * 根据实体查询结果集合
	 * @param mapper mapper
	 * @param record 查询的实体条件(等号匹配)
	 * @return
	 */
	public static <T> List<T> selectByEntity(Mapper<T> mapper, T record){
		return mapper.select(record);
	}
	
	/**
	 * 根据条件查询总数
	 * @param mapper
	 * @param enityClass
	 * @param queyMap
	 * @return
	 */
	public static <T> int selectCountByMap(Mapper<T> mapper, Class<T> enityClass, Map<String, Object> queyMap) {
		return mapper.selectCountByExample(createExample(enityClass, queyMap));
	}
	
	/**
	 * 查询结果集合
	 * @param mapper
	 * @param enityClass 实体类型
	 * @param queryMap 查询条件
	 * @return
	 */
	public static <T> List<T> selectByMap(Mapper<T> mapper, Class<T> enityClass, Map<String, Object> queryMap) {
		return selectByMap(mapper, enityClass, queryMap, null, null, null, new String[] { });
	}
	
	/**
	 * 查询结果集合
	 * @param mapper
	 * @param enityClass 实体类型
	 * @param queryMap 查询条件
	 * @param pageNo 分页页码(从1开始)
	 * @param pageSize 分页每页记录数
	 * @param orderByClause 排序
	 * @param properties 筛选结果属性
	 * @return
	 */
	public static <T> List<T> selectByMap(Mapper<T> mapper, Class<T> enityClass, Map<String, Object> queryMap, Integer pageNo, Integer pageSize, String orderByClause, String... properties) {
		Example example = createExample(enityClass, queryMap);
		if(orderByClause != null && orderByClause.length() > 0) {
			example.setOrderByClause(orderByClause);
		}
		if(properties != null && properties.length > 0) {
			example.selectProperties(properties);
		}
		return selectByExample(mapper, example, pageNo, pageSize);
	}
	
	/**
	 * 根据条件查询总数
	 * @param mapper
	 * @param enityClass
	 * @param conditions
	 * @return
	 */
	public static <T> int selectCountByCondition(Mapper<T> mapper, Class<T> enityClass, List<Condition> conditions){
		return mapper.selectCountByExample(createExample(enityClass, conditions));
	}
	
	/**
	 * 查询结果集合
	 * @param mapper
	 * @param enityClass 实体类型
	 * @param conditions 查询条件
	 * @return
	 */
	public static <T> List<T> selectByCondition(Mapper<T> mapper, Class<T> enityClass, List<Condition> conditions){
		return selectByCondition(mapper, enityClass, conditions, null, null, null, new String[] { });
	}
	
	/**
	 * 查询结果集合
	 * @param mapper
	 * @param enityClass 实体类型
	 * @param conditions 查询条件
	 * @param pageNo 分页页码(从1开始)
	 * @param pageSize 分页每页记录数
	 * @param orderByClause 排序
	 * @param properties 筛选结果属性
	 * @return
	 */
	public static <T> List<T> selectByCondition(Mapper<T> mapper, Class<T> enityClass, List<Condition> conditions, Integer pageNo, Integer pageSize, String orderByClause, String... properties) {
		Example example = createExample(enityClass, conditions);
		if(orderByClause != null && orderByClause.length() > 0) {
			example.setOrderByClause(orderByClause);
		}
		if(properties != null && properties.length > 0) {
			example.selectProperties(properties);
		}
		return selectByExample(mapper, example, pageNo, pageSize);
	}
	
	/**
	 * 查询分页结果
	 * @param mapper
	 * @param enityClass
	 * @param pageQuery
	 * @return
	 */
	public static <T> IPageResult<T> selectByPage(Mapper<T> mapper, Class<T> enityClass, IPageQuery pageQuery) {
		//分页结果
		PageResult<T> pageResult = new PageResult<T>();
		pageResult.setPageNo(pageQuery.getPageNo());
		pageResult.setPageSize(pageQuery.getPageSize());
		
		//获取总记录数
		int count = selectCountByCondition(mapper, enityClass, pageQuery.getConditions());
		pageResult.setTotal(Long.parseLong(String.valueOf(count)));
		
		//分页查询
		List<T> list = selectByCondition(mapper, enityClass, pageQuery.getConditions(), pageQuery.getPageNo(), pageQuery.getPageSize(), pageQuery.getOrderByClause(), pageQuery.getProperties());
		pageResult.setRows(list);
		
		return pageResult;
	}
	
	/**
	 * 查询结果记录数
	 * @param mapper
	 * @param example 查询条件
	 * @return
	 */
	public static <T> int selectCountByExample(Mapper<T> mapper, Example example) {
		return mapper.selectCountByExample(example);
	}
	
	/**
	 * 查询结果集合
	 * @param mapper
	 * @param example 查询条件
	 * @return
	 */
	public static <T> List<T> selectByExample(Mapper<T> mapper, Example example) {
		return selectByExample(mapper, example, null, null);
	}
	
	/**
	 * 查询结果集合
	 * @param mapper
	 * @param example 查询条件
	 * @param pageNo 分页页码(从1开始)
	 * @param pageSize 分页每页记录数
	 * @return
	 */
	public static <T> List<T> selectByExample(Mapper<T> mapper, Example example, Integer pageNo, Integer pageSize) {
		if (pageNo != null && pageNo != null) {
			int offset = (pageNo - 1) * pageSize;
			RowBounds rowBounds = new RowBounds(offset, pageSize);
			return mapper.selectByExampleAndRowBounds(example, rowBounds);
		} else {
			return mapper.selectByExample(example);
		}
	}
	
	/******************** end select ********************/
	
	/**
	 * 创建 Example
	 * @param enityClass 实体类型
	 * @return
	 */
	public static Example createExample(Class<?> enityClass) {
		Example example = new Example(enityClass);
		return example;
	}
	
	/**
	 * 创建 Example 
	 * @param enityClass 实体类型
	 * @param queryMap 查询参数
	 * @return
	 */
	public static Example createExample(Class<?> enityClass, Map<String, Object> queryMap) {
		Example example = createExample(enityClass);
		if (queryMap != null && queryMap.size() > 0) {
			Criteria criteria = example.createCriteria();
			for (Entry<String, Object> kv : queryMap.entrySet()) {
				if (kv.getValue() == null) {
					criteria.andIsNull(kv.getKey());
				} else {
					criteria.andEqualTo(kv.getKey(), kv.getValue());
				}
			}
		}
		return example;
	}
	
	/**
	 * 创建 Example
	 * @param enityClass
	 * @param conditions
	 * @return
	 */
	public static Example createExample(Class<?> enityClass, List<Condition> conditions) {
		Example example = createExample(enityClass);
		if(conditions != null && conditions.size() > 0) {
			Criteria criteriaNew = example.createCriteria();
			for(int i = 0; i < conditions.size(); i++) {
				Condition condition = conditions.get(i);
				condition.criteriaOperator(example, criteriaNew);
			}
		}
		return example;
	}
}
