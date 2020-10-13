package com.zhangzlyuyx.easy.mybatis.mapper;

import java.util.Set;

import org.apache.ibatis.mapping.MappedStatement;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * JoinExampleProvider
 * @author zhangzlyuyx
 *
 */
public class JoinExampleProvider extends MapperTemplate {
	
	public static final String method_selectByJoinExample = "selectByJoinExample";
	
	public static final String method_selectByJoinExampleAndRowBounds = "selectByJoinExampleAndRowBounds";
	
	public static final String method_selectMapByJoinExample = "selectMapByJoinExample";
	
	public static final String method_selectMapByJoinExampleAndRowBounds = "selectMapByJoinExampleAndRowBounds";
	
	public static final String method_selectCountByJoinExample = "selectCountByJoinExample";

	public JoinExampleProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}
    
    /**
     * 根据JoinExample查询
     *
     * @param ms
     * @return
     */
    public String selectByJoinExample(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        //将返回值修改为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder("SELECT ");
        if (isCheckExampleEntityClass()) {
            sql.append(SqlHelper.exampleCheck(entityClass));
        }
        sql.append("<if test=\"distinct\">distinct</if>");
        //支持查询指定列
        sql.append(exampleSelectColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(exampeJoinOn());
        sql.append(exampleWhereClause(entityClass));
        sql.append(exampleOrderBy(entityClass));
        sql.append(SqlHelper.exampleForUpdate());
        return sql.toString();
    }
    
    /**
     * 根据JoinExample查询
     * @param ms
     * @return
     */
    public String selectByJoinExampleAndRowBounds(MappedStatement ms) {
    	return this.selectByJoinExample(ms);
    }
    
    /**
     * 根据JoinExample查询
     *
     * @param ms
     * @return
     */
    public String selectMapByJoinExample(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder("SELECT ");
        if (isCheckExampleEntityClass()) {
            sql.append(SqlHelper.exampleCheck(entityClass));
        }
        sql.append("<if test=\"distinct\">distinct</if>");
        //支持查询指定列
        sql.append(exampleSelectColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(exampeJoinOn());
        sql.append(exampleWhereClause(entityClass));
        sql.append(exampleOrderBy(entityClass));
        sql.append(SqlHelper.exampleForUpdate());
        return sql.toString();
    }
    
    /**
     * 根据JoinExample查询
     * @param ms
     * @return
     */
    public String selectMapByJoinExampleAndRowBounds(MappedStatement ms) {
    	return this.selectMapByJoinExample(ms);
    }
    
    /**
     * 根据Example查询总数
     *
     * @param ms
     * @return
     */
    public String selectCountByJoinExample(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder("SELECT ");
        if (isCheckExampleEntityClass()) {
            sql.append(SqlHelper.exampleCheck(entityClass));
        }
        sql.append(SqlHelper.exampleCountColumn(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(exampeJoinOn());
        sql.append(exampleWhereClause(entityClass));
        sql.append(SqlHelper.exampleForUpdate());
        return sql.toString();
    }
    
    /**
     * example支持查询指定列时
     *
     * @return
     */
    private String exampleSelectColumns(Class<?> entityClass) {
    	String tableName = SqlHelper.getDynamicTableName(entityClass, tableName(entityClass));
        StringBuilder sql = new StringBuilder();
        sql.append("<choose>");
        sql.append("  <when test=\"@tk.mybatis.mapper.util.OGNL@hasSelectColumns(_parameter)\">");
        sql.append("    <foreach collection=\"_parameter.selectColumns\" item=\"selectColumn\" separator=\",\">");
        sql.append("      <if test=\"_parameter.selectColumnsIncludeTable and selectColumn.indexOf('.') &lt; 0 \">" + tableName + ".</if>" + "${selectColumn}");
        sql.append("    </foreach>");
        sql.append("  </when>");
        sql.append("  <otherwise>");
        sql.append("    " + getAllColumns(entityClass));
        sql.append("  </otherwise>");
        sql.append("</choose>");
        return sql.toString();
    }
    
    /**
     * 获取所有查询列，如id,name,code...
     *
     * @param entityClass
     * @return
     */
    private String getAllColumns(Class<?> entityClass) {
    	String tableName = SqlHelper.getDynamicTableName(entityClass, tableName(entityClass));
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        StringBuilder sql = new StringBuilder();
        for (EntityColumn entityColumn : columnSet) {
            sql.append("<if test=\"_parameter != null and _parameter.selectColumnsIncludeTable\">" + tableName + ".</if>" + entityColumn.getColumn()).append(",");
        }
        return sql.substring(0, sql.length() - 1);
    }
    
    /**
     * 获取 where 语句 xml
     *
     * @return
     */
    private String exampleWhereClause(Class<?> entityClass) {
    	String tableName = SqlHelper.getDynamicTableName(entityClass, tableName(entityClass));
        return "<if test=\"_parameter != null\">" +
                "<where>\n" +
                " ${@tk.mybatis.mapper.util.OGNL@andNotLogicDelete(_parameter)}" +
                " <trim prefix=\"(\" prefixOverrides=\"and |or \" suffix=\")\">\n" +
                "  <foreach collection=\"_parameter.oredCriteria\" item=\"criteria\">\n" +
                "    <if test=\"criteria.valid\">\n" +
                "      ${@tk.mybatis.mapper.util.OGNL@andOr(criteria)}" +
                "      <trim prefix=\"(\" prefixOverrides=\"and |or \" suffix=\")\">\n" +
                "        <foreach collection=\"criteria.criteria\" item=\"criterion\">\n" +
                "          <choose>\n" +
                "            <when test=\"criterion.noValue\">\n" +
                "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} ${criterion.condition}\n" +
                "            </when>\n" +
                "            <when test=\"criterion.singleValue\">\n" +
                "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} " + "<if test=\"_parameter.whereColumnsIncludeTable and criterion.condition.indexOf('.') &lt; 0 \">" + tableName + ".</if>" + "${criterion.condition} #{criterion.value}\n" +
                "            </when>\n" +
                "            <when test=\"criterion.betweenValue\">\n" +
                "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} " + "<if test=\"_parameter.whereColumnsIncludeTable and criterion.condition.indexOf('.') &lt; 0 \">" + tableName + ".</if>" + "${criterion.condition} #{criterion.value} and #{criterion.secondValue}\n" +
                "            </when>\n" +
                "            <when test=\"criterion.listValue\">\n" +
                "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} " + "<if test=\"_parameter.whereColumnsIncludeTable and criterion.condition.indexOf('.') &lt; 0 \">" + tableName + ".</if>" + "${criterion.condition}\n" +
                "              <foreach close=\")\" collection=\"criterion.value\" item=\"listItem\" open=\"(\" separator=\",\">\n" +
                "                #{listItem}\n" +
                "              </foreach>\n" +
                "            </when>\n" +
                "          </choose>\n" +
                "        </foreach>\n" +
                "      </trim>\n" +
                "    </if>\n" +
                "  </foreach>\n" +
                " </trim>\n" +
                "</where>" +
                "</if>";
    }
    
    /**
     * 获取 order by xml
     * @param entityClass
     * @return
     */
    private String exampleOrderBy(Class<?> entityClass) {
    	String tableName = SqlHelper.getDynamicTableName(entityClass, tableName(entityClass));
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"_parameter != null and _parameter.orderByClause != null\">");
        sql.append("  ORDER BY " + "<if test=\"_parameter.orderByColumnsIncludeTable and _parameter.orderByClause.indexOf('.') &lt; 0 \">" + tableName + ".</if>" + "${_parameter.orderByClause}");
        sql.append("</if>");
        String orderByClause = EntityHelper.getOrderByClause(entityClass);
        if (orderByClause != null && orderByClause.length() > 0) {
            sql.append("<if test=\"_parameter != null and _parameter.orderByClause == null\">");
            sql.append("  ORDER BY " + "<if test=\"_parameter.orderByColumnsIncludeTable and _parameter.orderByClause.indexOf('.') &lt; 0 \">" + tableName + ".</if>" + orderByClause);
            sql.append("</if>");
        }
        return sql.toString();
    }
    
    /**
     * 获取 join on xml
     * @return
     */
    private String exampeJoinOn() {
    	StringBuilder sql = new StringBuilder();
    	//join
        sql.append("<if test=\"_parameter != null and _parameter.joinParts != null\">");
        sql.append("  <foreach collection=\"_parameter.joinParts\" item=\"joinPart\" index=\"index\" separator=\" \">");
        sql.append("    ${joinPart.joinConnect} ${joinPart.joinTable} ON ${joinPart.joinOnClause} ");
        sql.append("  </foreach>");
        sql.append("</if>");
        return sql.toString();
    }
}
