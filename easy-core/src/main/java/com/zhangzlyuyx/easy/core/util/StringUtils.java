package com.zhangzlyuyx.easy.core.util;

import cn.hutool.core.util.StrUtil;

/**
 * String 工具类
 * @author zhangzlyuyx
 *
 */
public class StringUtils {

	public static boolean isEmpty(CharSequence str) {
		return StrUtil.isEmpty(str);
	}
	
	public static boolean isBlank(CharSequence str) {
		return StrUtil.isBlank(str);
	}
}
