package com.zhangzlyuyx.easy.shiro.authc;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;

import com.zhangzlyuyx.easy.shiro.Constant;
import com.zhangzlyuyx.easy.shiro.ShiroRealm;
import com.zhangzlyuyx.easy.shiro.ShiroToken;
import com.zhangzlyuyx.easy.shiro.authz.AuthenticationHandler;

/**
 * userpassword token
 * @author zhangzlyuyx
 *
 */
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken implements ShiroToken {

	private static final long serialVersionUID = 6289263822802522812L;
	
	/**
	 * 分组
	 */
	private String group = Constant.SHIROTOKEN_DEFAULT_GROUP;
	
	/**
	 * 附加属性
	 */
	private Map<String, Object> attributes;
	
	/**
	 * 认证处理器
	 */
	private AuthenticationHandler authenticationHandler;
	
	public UsernamePasswordToken() {
		
	}

	/**
	 * 解析 UsernamePasswordToken
	 * @param usernamePasswordToken usernamePasswordToken
	 * @return
	 */
	public static UsernamePasswordToken parse(org.apache.shiro.authc.UsernamePasswordToken usernamePasswordToken) {
		UsernamePasswordToken token = new UsernamePasswordToken();
		token.setHost(usernamePasswordToken.getHost());
		token.setUsername(usernamePasswordToken.getUsername());
		token.setPassword(usernamePasswordToken.getPassword());
		token.setRememberMe(usernamePasswordToken.isRememberMe());
		return token;
	}
	
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
	public AuthenticationHandler getAuthenticationHandler() {
		return this.authenticationHandler;
	}
	
	@Override
	public void setAuthenticationHandler(AuthenticationHandler authenticationHandler) {
		this.authenticationHandler = authenticationHandler;
	}

	@Override
	public void validation(ShiroRealm realm, Map<String, Object> params) throws AuthenticationException {
		
	}
}
