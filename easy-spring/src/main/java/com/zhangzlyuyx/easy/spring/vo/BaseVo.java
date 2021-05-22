package com.zhangzlyuyx.easy.spring.vo;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.zhangzlyuyx.easy.core.Result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("基础Vo")
public class BaseVo implements Serializable {

	private static final long serialVersionUID = 8910855359338155284L;
	
	public static final int STATUS_SUCCESS = 200;
	
	public static final int STATUS_ERROR = 400;
	
	public static final int STATUS_DENIED = 401;
	
	public static final String CODE_SUCCESS = "success";
	
	public static final String CODE_ERROR = "error";
	
	public static final String CODE_DENIED = "denied";
	
	/**
	 * 状态
	 */
	@ApiModelProperty(value = "响应代码(200:成功,400:失败,401:认证/登录失败)", dataType = "int")
	private Integer status;
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@ApiModelProperty(value  = "响应代码(success:成功,error:失败,denied:用户认证失败)")
	private String code;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	@ApiModelProperty(value = "响应消息")
	private String msg;

	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	/**
	 * 初始化
	 */
	public BaseVo() {
		this.success("");
	}
	
	/**
	 * 初始化
	 * @param success
	 */
	public BaseVo(boolean success) {
		if(success) {
			this.success("");
		} else {
			this.error("");
		}
	}
	
	/**
	 * 初始化
	 * @param success
	 * @param msg
	 */
	public BaseVo(boolean success, String msg) {
		if(success) {
			this.success(msg);
		} else {
			this.error(msg);
		}
	}
	
	/**
	 * 是否为成功结果
	 */
	public boolean isSuccess() {
		//code
		if(this.code != null && this.code.equalsIgnoreCase(CODE_SUCCESS)) {
			return true;
		}
		//status
		if(this.status != null && this.status.equals(STATUS_SUCCESS)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 成功结果
	 * @param msg 消息
	 * @return
	 */
	public BaseVo success(String msg) {
		this.setCode(CODE_SUCCESS);
		this.setStatus(STATUS_SUCCESS);
		this.setMsg(msg);
		return this;
	}
	
	/**
	 * 错误结果
	 * @param msg
	 * @return
	 */
	public BaseVo error(String msg) {
		this.setCode(CODE_ERROR);
		this.setStatus(STATUS_ERROR);
		this.setMsg(msg);
		return this;
	}
	
	/**
	 * 认证/登录失败
	 * @param msg
	 * @return
	 */
	public BaseVo denied(String msg) {
		this.setCode(CODE_DENIED);
		this.setStatus(STATUS_DENIED);
		this.setMsg(msg);
		return this;
	}
	
	/**
	 * load
	 * @param result
	 * @return
	 */
	public BaseVo load(Result<?> result) {
		this.setCode(result.getCode());
		this.setMsg(result.getMsg());
		return this;
	}
	
	/**
	 * 判断是否为指定的泛型
	 * @param clazz
	 * @return
	 */
	public boolean isParameterizedType(Class<?> clazz) {
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		if(!(genericSuperclass instanceof ParameterizedType)) {
			return false;
		}
		ParameterizedType paramType = (ParameterizedType)genericSuperclass;
		Type argType = paramType.getActualTypeArguments().length > 0 ? paramType.getActualTypeArguments()[0] : null;
		if(argType == null) {
			return false;
		}
		return argType.equals(clazz);
	}
}
