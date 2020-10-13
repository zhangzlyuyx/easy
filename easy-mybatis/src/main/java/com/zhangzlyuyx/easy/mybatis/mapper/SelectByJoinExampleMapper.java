package com.zhangzlyuyx.easy.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.session.RowBounds;

import com.zhangzlyuyx.easy.mybatis.entity.JoinExample;

/**
 * SelectByJoinExampleMapper
 * @author zhangzlyuyx
 *
 * @param <T>
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface SelectByJoinExampleMapper<T> {

	@SelectProvider(type = JoinExampleProvider.class, method = "dynamicSQL")
	List<T> selectByJoinExample(JoinExample joinExample);
	
	@SelectProvider(type = JoinExampleProvider.class, method = "dynamicSQL")
	List<T> selectByJoinExampleAndRowBounds(JoinExample joinExample, RowBounds rowBounds);
	
	@SelectProvider(type = JoinExampleProvider.class, method = "dynamicSQL")
	@ResultType(Map.class)
	List<Map<String, Object>> selectMapByJoinExample(JoinExample joinExample);
	
	@SelectProvider(type = JoinExampleProvider.class, method = "dynamicSQL")
	@ResultType(Map.class)
	List<Map<String, Object>> selectMapByJoinExampleAndRowBounds(JoinExample joinExample, RowBounds rowBounds);
	
	@SelectProvider(type = JoinExampleProvider.class, method = "dynamicSQL")
	Integer selectCountByJoinExample(JoinExample joinExample);
}
