package com.zhangzlyuyx.easy.shiro.util;

import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhangzlyuyx.easy.shiro.ShiroPrincipal;
import com.zhangzlyuyx.easy.shiro.ShiroToken;
import com.zhangzlyuyx.easy.shiro.authc.AccessToken;
import com.zhangzlyuyx.easy.shiro.authc.GeneralToken;
import com.zhangzlyuyx.easy.shiro.authc.UsernamePasswordToken;
import com.zhangzlyuyx.easy.shiro.authz.AuthenticationHandler;
import com.zhangzlyuyx.easy.shiro.authz.SimpleAuthenticationHandler;
import com.zhangzlyuyx.easy.shiro.filter.AuthenticationFilter;
import com.zhangzlyuyx.easy.spring.util.SpringUtils;

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
		try {
			Subject subject = SecurityUtils.getSubject();
			return subject;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * 执行登录
	 * @param token
	 * @throws AuthenticationException
	 */
	public static void login(AuthenticationToken token) throws AuthenticationException {
		Subject subject = getSubject();
		if(subject == null) {
			throw new AuthenticationException("shiro subject is null!");
		}
		subject.login(token);
	}
	
	/**
	 * 执行注销
	 */
	public static boolean logout() {
		Subject subject = getSubject();
		return logout(subject);
	}
	
	/**
	 * 执行注销
	 * @param subject
	 * @return
	 */
	public static boolean logout(Subject subject) {
		if(subject == null) {
			return false;
		}
		subject.logout();
		return true;
	}
	
	/**
	 * 是否已登录
	 * @return
	 */
	public static boolean isLogined() {
		return isLogined(getSubject());
	}
	
	/**
	 * 是否已登录
	 * @param subject
	 * @return
	 */
	public static boolean isLogined(Subject subject) {
		return subject != null && (subject.isAuthenticated() || subject.isRemembered());
	}
	
	/**
	 * 是否拥有指定角色
	 * @param subject
	 * @param roleIdentifier 角色标识
	 * @return
	 */
	public static boolean hasRole(Subject subject, String roleIdentifier) {
		return subject != null && subject.hasRole(roleIdentifier);
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
	 * 获取shiro认证主体信息
	 * @return
	 */
	public static Object getPrincipal() {
		Subject subject = getSubject();
		return getPrincipal(subject);
	}
	
	/**
	 * 获取shiro认证主体信息
	 * @param subject
	 * @return
	 */
	public static Object getPrincipal(Subject subject) {
		//判断是否已登录
		if(!isLogined(subject)) {
			return null;
		}
		try {
			return subject.getPrincipal();
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * 设置 shiro 认证主体信息
	 * @param principal
	 * @param realmName
	 * @return
	 */
	public static boolean setPrincipal(Object principal, String realmName) {
		Subject subject = getSubject();
		return setPrincipal(subject, principal, realmName);
	}

	/**
	 * 设置 shiro 认证主体信息
	 * @param subject
	 * @param principal
	 * @param realmName
	 * @return
	 */
	public static boolean setPrincipal(Subject subject, Object principal, String realmName) {
		if(subject == null) {
			return false;
		}
		if(realmName == null || realmName.length() == 0) {
			//根据原有的 Principals 获取 realmName
			Set<String> realmNames = subject.getPrincipals().getRealmNames();
			if(realmNames.size() > 0) {
				realmName = subject.getPrincipals().getRealmNames().iterator().next();
			}
		}
		if(realmName == null || realmName.length() == 0) {
			Realm realm = getRealm(realmName);
			if(realm != null) {
				realmName = realm.getName();
			}
		}
		subject.runAs(new SimplePrincipalCollection(principal, realmName));
		return true;
	}
	
	/**
	 * 获取 shiro 主体信息
	 * @return
	 */
	public static ShiroPrincipal getShiroPrincipal() {
		Object principal = getPrincipal();
		return (principal != null && principal instanceof ShiroPrincipal) ? (ShiroPrincipal)principal : null;
	}
	
	/**
	 * 获取 shiro token
	 * @return
	 */
	public static ShiroToken getShiroToken() {
		ShiroPrincipal shiroPrincipal = getShiroPrincipal();
		return shiroPrincipal != null ? shiroPrincipal.getShiroToken() : null;
	}
	
	/**
	 * 获取 AccessToken
	 * @return
	 */
	public static AccessToken getAccessToken() {
		ShiroToken shiroToken = getShiroToken();
		return (shiroToken != null && shiroToken instanceof AccessToken) ? (AccessToken)shiroToken : null;
	}
	
	/**
	 * 获取 UsernamePasswordToken
	 * @return
	 */
	public static UsernamePasswordToken getUsernamePasswordToken() {
		ShiroToken shiroToken = getShiroToken();
		return (shiroToken != null && shiroToken instanceof UsernamePasswordToken) ? (UsernamePasswordToken)shiroToken : null;
	}
	
	/**
	 * 获取 GeneralToken
	 * @return
	 */
	public static GeneralToken getGeneralToken() {
		ShiroToken shiroToken = getShiroToken();
		return (shiroToken != null && shiroToken instanceof GeneralToken) ? (GeneralToken)shiroToken : null;
	}
	
	/**
	 * 获取认证处理器
	 * @param request
	 * @return
	 */
	public static AuthenticationHandler getAuthenticationHandler(ServletRequest request) {
		AuthenticationHandler authenticationHandler = null;
		
		if(authenticationHandler == null) {
			try {
				authenticationHandler = SpringUtils.getWebApplicationContext(request != null ? request.getServletContext() : null).getBean(AuthenticationHandler.class);
				if(authenticationHandler != null) {
					return authenticationHandler;
				}
			} catch (Exception e) {
				log.warn("", e);
			}
		}
		if(authenticationHandler == null) {
			authenticationHandler = SimpleAuthenticationHandler.getInstance();
			if(authenticationHandler != null) {
				return authenticationHandler;
			}
		}
		return authenticationHandler;
	}
	
	/**
	 * 获取认证处理器
	 * @param token
	 * @return
	 */
	public static AuthenticationHandler getAuthenticationHandler(AuthenticationToken token) {
		AuthenticationHandler authenticationHandler = null;
		if(token != null && token instanceof ShiroToken) {
			authenticationHandler = ((ShiroToken)token).getAuthenticationHandler();
		}
		if(authenticationHandler == null) {
			authenticationHandler = SimpleAuthenticationHandler.getInstance();
		}
		return authenticationHandler;
	}
	
	/**
	 * 获取认证处理器
	 * @param token
	 * @return
	 */
	public static AuthenticationHandler getAuthenticationHandler(Object principal) {
		AuthenticationHandler authenticationHandler = null;
		if(principal != null && principal instanceof ShiroPrincipal) {
			ShiroPrincipal shiroPrincipal = (ShiroPrincipal)principal;
			if(shiroPrincipal.getShiroToken() != null && shiroPrincipal.getShiroToken().getAuthenticationHandler() != null) {
				authenticationHandler = shiroPrincipal.getShiroToken().getAuthenticationHandler();
			}
		}
		if(authenticationHandler == null) {
			authenticationHandler = SimpleAuthenticationHandler.getInstance();
		}
		return authenticationHandler;
	}
	
	/**
	 * 是否允许操作
	 * @param authenticationFilter
	 * @param request
	 * @param response
	 * @param mappedValue
	 * @return
	 */
	public static boolean isAccessAllowed(AuthenticationFilter authenticationFilter, ServletRequest request, ServletResponse response, Object mappedValue) {
		Subject subject = getSubject();
		//验证认证状态
		if(!isLogined(subject)) {
			return false;
		}
		//认证主体信息
		ShiroPrincipal shiroPrincipal = getShiroPrincipal();
		if(shiroPrincipal == null) {
			subject.logout();
			return false;
		}
		//认证 token
		ShiroToken shiroToken = shiroPrincipal.getShiroToken();
		if(shiroToken == null) {
			subject.logout();
			return false;
		}
		//验证token分组一致性 
		if(!authenticationFilter.getGroup().equalsIgnoreCase(shiroToken.getGroup())) {
			subject.logout();
			return false;
		}
		return true;
	}
	
	/**
	 * 获取 shiro 会话
	 * @return
	 */
	public static Session getShiroSession() {
		Subject subject = getSubject();
		return subject != null ? subject.getSession() : null;
	}
	
	/**
	 * 获取 shiro 会话属性值 
	 * @param key
	 * @return
	 */
	public static Object getShiroSessionAttribute(Object key) {
		Session session = getShiroSession();
		if(session == null) {
			return null;
		}
		return session.getAttribute(key);
	}

	/**
	 * 设置 shiro 会话属性值
	 * @param key
	 * @param value
	 * @return
	 */
	public static Boolean setShiroSessionAttribute(Object key, Object value) {
		Session session = getShiroSession();
		if(session == null) {
			return null;
		}
		session.setAttribute(key, value);
		return true;
	}
	
	/**
	 * 保存当前请求
	 * @param request
	 */
	public static void saveRequest(ServletRequest request) {
		WebUtils.saveRequest(request);
	}
	
	/**
	 * 获取保存的请求
	 * @param request
	 * @return
	 */
	public static SavedRequest getSavedRequest(ServletRequest request) {
		return WebUtils.getSavedRequest(request);
	}
	
	/**
	 * 获取保存的请求并清除保存
	 * @param request
	 * @return
	 */
	public static SavedRequest getAndClearSavedRequest(ServletRequest request) {
		return WebUtils.getAndClearSavedRequest(request);
	}
}
