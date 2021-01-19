package com.zhangzlyuyx.easy.mybatis.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.util.StringUtil;

/**
 * 实体工具类
 *
 */
public class EntityUtils {
	
	private static final Logger log = LoggerFactory.getLogger(EntityUtils.class);
	
	/** 表缓存 */
	private static final Map<Class<?>, String> TABLE_MAP = new HashMap<>();
	
	/** 字段缓存 */
	private static final Map<Class<?>, List<String>> COLUMN_MAP = new HashMap<>();

	/**
	 * 获取表名称
	 * @param entityClass
	 * @return
	 */
	public static <T> String getTableName(Class<T> entityClass, MapperHelper mapperHelper) {
		//判断缓存
		if(TABLE_MAP.containsKey(entityClass)) {
			return TABLE_MAP.get(entityClass);
		}
		try {
			EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
			if(entityTable != null) {
				
				String prefix = entityTable.getPrefix();
				if (StringUtil.isEmpty(prefix) && mapperHelper != null) {
		            //使用全局配置
		            prefix = mapperHelper.getConfig().getPrefix();
		        }
				String tableName = StringUtil.isNotEmpty(prefix) ? (prefix + "." + entityTable.getName()) : entityTable.getName();
				TABLE_MAP.put(entityClass, tableName);
				return tableName;
			}
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
		
		//反射获取表名
		javax.persistence.Table table = entityClass.getDeclaredAnnotation(javax.persistence.Table.class);
		if(table != null) {
			TABLE_MAP.put(entityClass, table.name());
			return table.name();
		}
		return null;
	}
	
	/**
	 * 获取列名集合
	 * @param entityClass
	 * @return
	 */
	public static <T> List<String> getColumns(Class<T> entityClass) {
		//判断缓存
		if(COLUMN_MAP.containsKey(entityClass)) {
			return COLUMN_MAP.get(entityClass);
		}
		List<String> columns = new ArrayList<>();
		try {
			Set<EntityColumn> entityColumns = EntityHelper.getColumns(entityClass);
			if(entityColumns != null && entityColumns.size() > 0) {
				for(EntityColumn entityColumn : entityColumns) {
					columns.add(entityColumn.getColumn());
				}
				COLUMN_MAP.put(entityClass, columns);
				return columns;
			}
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
		
		//反射获取字段
		for(Field field : entityClass.getDeclaredFields()) {
			javax.persistence.Column column = field.getDeclaredAnnotation(javax.persistence.Column.class);
			if(column == null) {
				continue;
			}
			columns.add(column.name());
		}
		if(columns.size() == 0) {
			return null;
		}
		COLUMN_MAP.put(entityClass, columns);
		return columns;
	}
}
