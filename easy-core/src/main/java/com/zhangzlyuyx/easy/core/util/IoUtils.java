package com.zhangzlyuyx.easy.core.util;

import java.io.InputStream;
import java.io.OutputStream;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;

/**
 * IO 工具类
 * @author zhangzlyuyx
 *
 */
public class IoUtils {

	/** 默认缓存大小 */
	public static final int DEFAULT_BUFFER_SIZE = 1024;
	
	/** 默认中等缓存大小 */
	public static final int DEFAULT_MIDDLE_BUFFER_SIZE = 4096;
	
	/** 默认大缓存大小 */
	public static final int DEFAULT_LARGE_BUFFER_SIZE = 8192;

	/** 数据流末尾 */
	public static final int EOF = -1;
	
	/**
	 * 将输入流写入到输出流
	 * @param inputStream
	 * @param outputStream
	 * @return
	 * @throws IORuntimeException
	 */
	public static long write(InputStream inputStream, OutputStream outputStream) throws IORuntimeException {
		return IoUtil.copy(inputStream, outputStream);
	}
}
