package com.zhangzlyuyx.easy.mybatis.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhangzlyuyx.easy.core.util.ReflectUtils;
import com.zhangzlyuyx.easy.core.util.StringUtils;
import com.zhangzlyuyx.easy.mybatis.Condition;
import com.zhangzlyuyx.easy.mybatis.ICondition;
import com.zhangzlyuyx.easy.mybatis.IPageQuery;
import com.zhangzlyuyx.easy.mybatis.IPageResult;
import com.zhangzlyuyx.easy.mybatis.PageResult;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.util.StringUtil;

/**
 * mapper 工具类
 *
 */
public class MapperUtils {
	
	private static final Logger log = LoggerFactory.getLogger(MapperUtils.class);
	
	/**
	 * 获取实体表名
	 * @param entityClass
	 * @return
	 */
	public static String tableName(Class<?> entityClass) {
		EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
		if(entityTable == null) {
			return null;
		}
		String prefix = entityTable.getPrefix();
		if (StringUtil.isNotEmpty(prefix)) {
            return prefix + "." + entityTable.getName();
        }
        return entityTable.getName();
	}

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
		if(selective) {
			return mapper.updateByExampleSelective(entity, example);
		} else {
			return mapper.updateByExample(entity, example);
		}
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
		//查询条件
		Example example = createExample(enityClass, pageQuery);
		List<T> list = null;
		//判断是否需要分页
		if (pageQuery.getPageNo() != null && pageQuery.getPageSize() != null) {
			//查询数据列表
			list = selectByExample(mapper, example, pageQuery.getPageNo(), pageQuery.getPageSize());
			//获取总记录数
			Long total = getPageHelperTotal(list);
			if(total == null) {
				total = Long.valueOf(selectCountByCondition(mapper, enityClass, pageQuery.getConditions()));
			}
			//int count = selectCountByCondition(mapper, enityClass, pageQuery.getConditions());
			//pageResult.setTotal(Long.parseLong(String.valueOf(count)));
			pageResult.setTotal(total);
			pageResult.setPageNo(pageQuery.getPageNo());
			pageResult.setPageSize(pageQuery.getPageSize());
		} else {
			//查询数据列表
			list = selectByExample(mapper, example);
			pageResult.setTotal(Long.parseLong(String.valueOf(list.size())));
			pageResult.setPageNo(1);
			pageResult.setPageSize(list.size());
		}
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
	
	public static <T> boolean existsWithPrimaryKey(Mapper<T> mapper, Object key) {
		return mapper.existsWithPrimaryKey(key);
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
			for(int i = 0; i < conditions.size(); i++) {
				Criteria criteriaNew = example.and();
				Condition condition = conditions.get(i);
				criteriaOperator(condition, example, criteriaNew); 
			}
		}
		return example;
	}
	
	/**
	 * 创建 Example
	 * @param enityClass
	 * @param pageQuery
	 * @return
	 */
	public static Example createExample(Class<?> enityClass, IPageQuery pageQuery) {
		Example example = createExample(enityClass);
		if(pageQuery.getConditions().size() > 0) {
			Criteria criteriaNew = example.createCriteria();
			for(int i = 0; i < pageQuery.getConditions().size(); i++) {
				Condition condition = pageQuery.getConditions().get(i);
				criteriaOperator(condition, example, criteriaNew); 
			}
		}
		if(pageQuery.getOrderByClause() != null && pageQuery.getOrderByClause().length() > 0) {
			example.setOrderByClause(pageQuery.getOrderByClause());
		}
		if(pageQuery.getProperties() != null && pageQuery.getProperties().length > 0) {
			example.selectProperties(pageQuery.getProperties());
		}
		return example;
	}
	
	/**
	 * 操作符 map
	 */
	private static Map<String, String> operatorMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 5683714999942954274L;
	{
		put("=", " =");
		put("==", " =");
		put("<=>", " <=>");
		put("!=", " <>");
		put("<>", " <>");
		put(">", " >");
		put(">=", " >=");
		put("<", " <");
		put("<=", " <=");
		put("like", " LIKE");
		put("not like", " NOT LIKE");
		put("in", " IN");
		put("not in", " NOT IN");
		put("isnull", " IS NULL");
		put("is null", " IS NULL");
		put("is not null", " IS NOT NULL");
	}};
	
	/**
	 * example 条件操作递归处理
	 * @param condition
	 * @param example
	 * @param criteriaNew
	 * @return
	 */
	public static boolean criteriaOperator(ICondition condition, Example example, Criteria criteriaNew) {
		
		//是否为空条件
		if(StringUtils.isEmpty(condition.getField()) && StringUtils.isEmpty(condition.getOperator()) && condition.getValue() == null && (condition.getConditions() == null || condition.getConditions().size() == 0)) {
			return false;
		}
		
		//operator
		String operator = !StringUtils.isEmpty(condition.getOperator()) ? condition.getOperator().toLowerCase() : "";
		//trim
		operator = StringUtils.trim(operator);
		
		//prefix
		boolean hasPrefix = !StringUtils.isEmpty(condition.getField()) && condition.getField().contains(".");
		
		//and or
		boolean isAnd = "and".equalsIgnoreCase(condition.getAndOr());
		
		//字段是否效
		if(!StringUtils.isEmpty(condition.getField())) {
			
			//字段是否包含前缀
			if(hasPrefix) {
				if(operatorMap.containsKey(operator)) {
					String opt = operatorMap.get(operator);
					if(isAnd) {
						if(condition.getValue() == null) {
							criteriaNew.andCondition(condition.getField() + " " + opt);
						} else {
							criteriaNew.andCondition(condition.getField() + " " + opt, condition.getValue());
						}
					} else {
						if(condition.getValue() == null) {
							criteriaNew.orCondition(condition.getField() + " " + opt);
						} else {
							criteriaNew.orCondition(condition.getField() + " " + opt, condition.getValue());
						}
					}
				} else {
					if(isAnd) {
						if(condition.getValue() == null) {
							criteriaNew.andCondition(condition.getField());
						} else {
							criteriaNew.andCondition(condition.getField(), condition.getValue());
						}
					} else {
						if(condition.getValue() == null) {
							criteriaNew.orCondition(condition.getField());
						} else {
							criteriaNew.orCondition(condition.getField(), condition.getValue());
						}
					}
				}
			} else {
				
				if(operator.equalsIgnoreCase("=")){
					if(isAnd) {
						criteriaNew.andEqualTo(condition.getField(), condition.getValue());
					} else {
						criteriaNew.orEqualTo(condition.getField(), condition.getValue());
					}
				}else if(operator.equalsIgnoreCase("!=") || operator.equalsIgnoreCase("<>")){
					if(isAnd) {
						criteriaNew.andNotEqualTo(condition.getField(), condition.getValue());
					} else {
						criteriaNew.orNotEqualTo(condition.getField(), condition.getValue());
					}
				}else if(operator.equalsIgnoreCase("like")){
					if(isAnd) {
						criteriaNew.andLike(condition.getField(), condition.getValue().toString());
					} else {
						criteriaNew.orLike(condition.getField(), condition.getValue().toString());
					}
				}else if(operator.equalsIgnoreCase("not like")){
					if(isAnd) {
						criteriaNew.andNotLike(condition.getField(), condition.getValue().toString());
					} else {
						criteriaNew.orNotLike(condition.getField(), condition.getValue().toString());
					}
				}else if(operator.equalsIgnoreCase(">")){
					if(isAnd) {
						criteriaNew.andGreaterThan(condition.getField(), condition.getValue());
					} else {
						criteriaNew.orGreaterThan(condition.getField(), condition.getValue());
					}
				}else if(operator.equalsIgnoreCase(">=")){
					if(isAnd) {
						criteriaNew.andGreaterThanOrEqualTo(condition.getField(), condition.getValue());
					} else {
						criteriaNew.orGreaterThanOrEqualTo(condition.getField(), condition.getValue());
					}
				}else if(operator.equalsIgnoreCase("<")){
					if(isAnd) {
						criteriaNew.andLessThan(condition.getField(), condition.getValue());
					} else {
						criteriaNew.orLessThan(condition.getField(), condition.getValue());
					}
				}else if(operator.equalsIgnoreCase("<=")){
					if(isAnd) {
						criteriaNew.andLessThanOrEqualTo(condition.getField(), condition.getValue());
					} else {
						criteriaNew.orLessThanOrEqualTo(condition.getField(), condition.getValue());
					}
				}else if(operator.equalsIgnoreCase("in")){
					if(isAnd) {
						criteriaNew.andIn(condition.getField(), (List<?>)condition.getValue());
					} else {
						criteriaNew.orIn(condition.getField(), (List<?>)condition.getValue());
					}
					
				}else if(operator.equalsIgnoreCase("not in")){
					if(isAnd) {
						criteriaNew.andNotIn(condition.getField(), (List<?>)condition.getValue());
					} else {
						criteriaNew.orNotIn(condition.getField(), (List<?>)condition.getValue());
					}
				}else if(operator.equalsIgnoreCase("isnull") || operator.equalsIgnoreCase("is null")){
					if(isAnd) {
						criteriaNew.andIsNull(condition.getField());
					} else {
						criteriaNew.orIsNull(condition.getField());
					}
				}else if(operator.equalsIgnoreCase("is not null")){
					if(isAnd) {
						criteriaNew.andIsNotNull(condition.getField());
					} else {
						criteriaNew.orIsNotNull(condition.getField());
					}
				}else if(operator.equalsIgnoreCase("between")){
					if(isAnd) {
						criteriaNew.andBetween(condition.getField(), condition.getValue(), condition.getSecondValue());
					} else {
						criteriaNew.orBetween(condition.getField(), condition.getValue(), condition.getSecondValue());
					}
				}  else{
					if(condition.getValue() != null){
						if(isAnd) {
							criteriaNew.andCondition(condition.getField(), condition.getValue());
						} else {
							criteriaNew.orCondition(condition.getField(), condition.getValue());
						}
					}else{
						if(isAnd) {
							criteriaNew.andCondition(condition.getField());
						} else {
							criteriaNew.orCondition(condition.getField());
						}
					}
				}
			}
		}
		
		//递归嵌套条件处理
		if(condition.getConditions() != null && condition.getConditions().size() > 0) {
			Criteria nextCriteria = criteriaNew;
			for(Condition nextCondition : condition.getConditions()) {
				criteriaOperator(nextCondition, example, nextCriteria);
			}
		}
		return true;
	}
	
	/**
	 * 获取 mybatis mappedStatement Id 
	 * @param sqlSession
	 * @param sql
	 * @param sqlCommandType
	 * @param resultType
	 * @return
	 */
	public static String getMappedStatementId(SqlSession sqlSession, SqlCommandType sqlCommandType, String sql, Class<?> parameterType, Class<?> resultType) {
		StringBuilder msIdBuilder = new StringBuilder(sqlCommandType.toString());
		msIdBuilder.append(".");
		msIdBuilder.append(((resultType != null ? resultType : "") + sql + (parameterType != null ? parameterType : "")).hashCode());
    	String msId = msIdBuilder.toString();
    	Configuration configuration = sqlSession.getConfiguration();
    	if(configuration.hasStatement(msId, false)) {
    		return msId;
    	}
    	SqlSource sqlSource = null;
		if(parameterType == null) {
			sqlSource = new StaticSqlSource(configuration, sql);
		} else {
			sqlSource = configuration.getDefaultScriptingLanuageInstance().createSqlSource(configuration, sql, parameterType);
		}
    	List<ResultMap> resultMaps = new ArrayList<>();
    	resultMaps.add(new ResultMap.Builder(configuration, "defaultResultMap", resultType, new ArrayList<ResultMapping>(0)).build());
    	MappedStatement ms = new MappedStatement.Builder(configuration, msId, sqlSource, sqlCommandType).resultMaps(resultMaps).build();
    	configuration.addMappedStatement(ms);
    	return msId;
	}
	
	/**
	 * 获取pagehelper分页总记录数
	 * @param list
	 * @return
	 */
	public static Long getPageHelperTotal(Object list) {
		if(list == null) {
			return null;
		}
		//pagehelper 分页处理
		if(list.getClass().getTypeName().equals("com.github.pagehelper.Page")) {
			try {
				return (long)ReflectUtils.getFieldValue(list, "total");
			} catch (Exception e) {
				log.debug(e.getMessage(), e);
			}
		}
		return null;
	}
}
