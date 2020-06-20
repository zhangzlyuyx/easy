package com.zhangzlyuyx.easy.core.util;

import cn.hutool.core.util.StrUtil;

public class StringUtils {

	public static boolean isEmpty(CharSequence str) {
		return StrUtil.isEmpty(str);
	}
	
	public static boolean isBlank(CharSequence str) {
		return StrUtil.isBlank(str);
	}
}
