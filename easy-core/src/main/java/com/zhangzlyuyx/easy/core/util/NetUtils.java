package com.zhangzlyuyx.easy.core.util;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.core.util.NetUtil;

/**
 * 网络 - 工具类
 * @author zhangzlyuyx
 *
 */
public class NetUtils {

	private static final Logger log = LoggerFactory.getLogger(NetUtils.class);
	
	/**
	 * 获取本机网卡IP地址
	 * @return
	 */
	public static InetAddress getLocalhost() {
		return NetUtil.getLocalhost();
	}
	
	/**
	 * 获得指定地址信息中的MAC地址，使用分隔符“-”
	 * @param inetAddress
	 * @return
	 */
	public static String getMacAddress(InetAddress inetAddress) {
		return NetUtil.getMacAddress(inetAddress);
	}
	
	public static void main(String[] args) {
		log.debug("");
	}
}
