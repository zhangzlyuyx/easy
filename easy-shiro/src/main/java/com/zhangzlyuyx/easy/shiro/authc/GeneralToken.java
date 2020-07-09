package com.zhangzlyuyx.easy.shiro.authc;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;

import com.alibaba.fastjson.JSONObject;
import com.zhangzlyuyx.easy.core.util.ConvertUtils;
import com.zhangzlyuyx.easy.shiro.Constant;
import com.zhangzlyuyx.easy.shiro.ShiroRealm;
import com.zhangzlyuyx.easy.shiro.ShiroToken;
import com.zhangzlyuyx.easy.shiro.authz.AuthenticationHandler;

/**
 * 通用 shiro token
 * @author zhangzlyuyx
 *
 */
public class GeneralToken implements AuthenticationToken, ShiroToken {

	private static final long serialVersionUID = 9032276077114710238L;
	

	/**
	 * token 分组
	 */
	private String group = Constant.SHIROTOKEN_DEFAULT_GROUP;
	
	/**
	 * 主体信息
	 */
	private Object principal;
	
	/**
	 * 凭据信息
	 */
	private Object credentials;
	
	/**
	 * 附加属性
	 */
	private Map<String, Object> attributes;
	
	/**
	 * 认证处理器
	 */
	private transient AuthenticationHandler authenticationHandler;
	
	/**
	 * 认证域名称
	 */
	private String realmName;
	
	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public Map<String, Object> getAttributes() {
		if(this.attributes == null) {
			this.attributes = new HashMap<>();
		}
		return this.attributes;
	}

	@Override
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public Object getAttribute(String key) {
		if(!this.getAttributes().containsKey(key)) {
			return null;
		}
		return this.getAttributes().get(key);
	}
	
	@Override
	public <T> T getAttribute(String key, Class<T> clazz) {
		Object value = this.getAttribute(key);
		if(value == null) {
			return null;
		}
		if(value.getClass().equals(clazz) || value.getClass().isAssignableFrom(clazz)) {
			return (T)value;
		}
		return ConvertUtils.convert(clazz, value);
	}
	
	@Override
	public void setAttribute(String key, Object value) {
		this.getAttributes().put(key, value);
	}

	@Override
	public AuthenticationHandler getAuthenticationHandler() {
		return this.authenticationHandler;
	}

	@Override
	public void setAuthenticationHandler(AuthenticationHandler authenticationHandler) {
		this.authenticationHandler = authenticationHandler;
	}
	
	@Override
	public String getRealmName() {
		return this.realmName;
	}
	
	@Override
	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}

	@Override
	public void validation(ShiroRealm realm, Map<String, Object> params) throws AuthenticationException {
		this.setRealmName(realm.getName());
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}
	
	public void setPrincipal(Object principal) {
		this.principal = principal;
	}

	@Override
	public Object getCredentials() {
		return this.credentials;
	}

	public void setCredentials(Object credentials) {
		this.credentials = credentials;
	}
}
