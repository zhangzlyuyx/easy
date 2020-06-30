package com.zhangzlyuyx.easy.shiro.util;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zhangzlyuyx.easy.shiro.authz.AuthenticationHandler;
import com.zhangzlyuyx.easy.shiro.authz.SimpleAuthenticationHandler;

/**
 * shiro工具类
 * @author zhangzlyuyx
 *
 */
public class ShiroUtils {

	private static Logger log = LoggerFactory.getLogger(ShiroUtils.class);
	
	/**
	 * get subject
	 * @return
	 */
	public static Subject getSubject() {
		Subject subject = SecurityUtils.getSubject();
		return subject;
	}
	
	/**
	 * 获取 shiro 会话
	 * @return
	 */
	public static Session getSession() {
		Subject subject = getSubject();
		return subject.getSession();
	}
	
	/**
	 * 获取 realm
	 * @param realmName
	 * @return
	 */
	public static Realm getRealm(String realmName) {
		SecurityManager securityManager = SecurityUtils.getSecurityManager();
		if(securityManager instanceof RealmSecurityManager) {
			RealmSecurityManager realmSecurityManager = (RealmSecurityManager)securityManager;
			for(Realm realm: realmSecurityManager.getRealms()) {
				if(realmName == null || realmName.length() == 0) {
					return realm;
				} else if(realmName.equals(realm.getName())) {
					return realm;
				}
			}
		}
		return null;
	}
	
	/**
	 * 执行登录
	 * @param token
	 * @throws AuthenticationException
	 */
	public static void login(AuthenticationToken token) throws AuthenticationException {
		Subject subject = getSubject();
		subject.login(token);
	}
	
	/**
	 * 执行注销
	 */
	public static void logout() {
		Subject subject = getSubject();
		subject.logout();
	}
	
	/**
	 * get principal
	 * @return
	 */
	public static Object getPrincipal() {
		Subject subject = getSubject();
		return subject.getPrincipal();
	}
	
	/**
	 * set principal
	 * @param principal
	 * @param realmName
	 */
	public static void setPrincipal(Object principal, String realmName) {
		Subject subject = getSubject();
		if(realmName == null || realmName.length() == 0) {
			Realm realm = getRealm(realmName);
			if(realm != null) {
				realmName = realm.getName();
			}
		}
		subject.runAs(new SimplePrincipalCollection(principal, realmName));
	}
	
	/**
	 * 获取认证处理器
	 * @param request
	 * @return
	 */
	public static AuthenticationHandler getAuthenticationHandler(ServletRequest request) {
		AuthenticationHandler authenticationHandler = null;
		if(request != null) {
			try {
				ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
				authenticationHandler = applicationContext.getBean(AuthenticationHandler.class);
				if(authenticationHandler != null) {
					return authenticationHandler;
				}
			} catch (Exception e) {
				log.warn("", e);
			}
		}
		if(authenticationHandler == null) {
			authenticationHandler = SimpleAuthenticationHandler.getInstance();
		}
		return authenticationHandler;
	}
}
