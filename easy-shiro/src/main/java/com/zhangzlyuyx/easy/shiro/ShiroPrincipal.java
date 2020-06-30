package com.zhangzlyuyx.easy.shiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * shiro 认证主体信息
 * @author zhangzlyuyx
 *
 */
public class ShiroPrincipal implements Serializable {

	private static final long serialVersionUID = -8582297649331713988L;
	
	/**
	 * 用户id
	 */
	private String userId;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 角色列表
	 */
	private List<String> roles;
	
	/**
	 * 权限列表
	 */
	private List<String> permissions;
	
	/**
	 * 附加属性
	 */
	private Map<String, Object> attributes;
	
	/**
	 * token
	 */
	private ShiroToken shiroToken;
	
	public ShiroPrincipal() {
		
	}
	
	/**
	 * 获取 用户id
	 * @return
	 */
	public String getUserId() {
		return this.userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * 获取 用户名
	 * @return
	 */
	public String getUserName() {
		return this.userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * 获取 角色列表
	 * @return
	 */
	public List<String> getRoles() {
		if(this.roles == null) {
			this.roles = new ArrayList<>();
		}
		return roles;
	}
	
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	/**
	 * 获取权限列表
	 * @return
	 */
	public List<String> getPermissions() {
		if(this.permissions == null) {
			this.permissions = new ArrayList<>();
		}
		return permissions;
	}
	
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	
	/**
	 * 获取 附加属性
	 * @return
	 */
	public Map<String, Object> getAttributes() {
		if(this.attributes == null) {
			this.attributes = new HashMap<>();
		}
		return this.attributes;
	}
	
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	/**
	 * 获取 shiro token
	 * @return
	 */
	public ShiroToken getShiroToken() {
		return this.shiroToken;
	}
	
	public void setShiroToken(ShiroToken shiroToken) {
		this.shiroToken = shiroToken;
	}
}
