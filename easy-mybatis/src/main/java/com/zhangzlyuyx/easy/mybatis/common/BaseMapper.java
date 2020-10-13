package com.zhangzlyuyx.easy.mybatis.common;

import com.zhangzlyuyx.easy.mybatis.mapper.SelectByJoinExampleMapper;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface BaseMapper<T> extends Mapper<T>, IdsMapper<T>, ConditionMapper<T>, MySqlMapper<T>, SelectByJoinExampleMapper<T> {

}