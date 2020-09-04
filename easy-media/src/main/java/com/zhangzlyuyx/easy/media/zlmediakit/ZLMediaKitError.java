package com.zhangzlyuyx.easy.media.zlmediakit;

/**
 * ZLMediaKit 错误枚举
 * @author zhangzlyuyx
 *
 */
public enum ZLMediaKitError {

	/**
	 * 代码抛异常
	 */
	Exception(-400, "代码抛异常"),
	/**
	 * 参数不合法
	 */
	InvalidArgs(-300, "参数不合法"),
	/**
	 * sql执行失败
	 */
	SqlFailed(-200, "sql执行失败"),
	/**
	 * 鉴权失败
	 */
	AuthFailed(-100, "鉴权失败"),
	/**
	 * 业务代码执行失败
	 */
	OtherFailed(-1, "业务代码执行失败"),
	/**
	 * 执行成功
	 */
	Success(0, "执行成功")
	;
	
	private Integer code;
	
	public Integer getCode() {
		return code;
	}
	
	private String msg;
	
	public String getMsg() {
		return msg;
	}
	
	private ZLMediaKitError(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public static ZLMediaKitError parse(Integer code) {
		if(code == null) {
			return null;
		}
		for(ZLMediaKitError apiErr : ZLMediaKitError.values()) {
			if(apiErr.getCode().equals(code)) {
				return apiErr;
			}
		}
		return null;
	}
}
