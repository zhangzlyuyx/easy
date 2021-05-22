package com.zhangzlyuyx.easy.shiro;

import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * 默认 shiro 配置
 *
 */
public abstract class ShiroConfig {

	public static final String BEAN_SHIROREALM = "shiroRealm";
	
	public static final String BEAN_SESSIONMANAGER = "sessionManager";
	
	public static final String BEAN_SECURITYMANAGER = "securityManager";
	
	public static final String BEAN_LIFECYCLEBEANPOSTPROCESSOR = "lifecycleBeanPostProcessor";
	
	public static final String BEAN_SHIROFILTERFACTORY = "shiroFilterFactory";
	
	public static final String BEAN_ADVISORAUTOPROXYCREATOR = "advisorAutoProxyCreator";
	
	public static final String BEAN_AUTHORIZATIONATTRIBUTESOURCEADVISOR = "authorizationAttributeSourceAdvisor";
	
	public static final String CACHE_AUTHENTICATIONCACHE = "authenticationCache";
	
	public static final String CACHE_AUTHORIZATIONCACHE = "authorizationCache";
	
	private final static String DEFAULT_SESSION_IDCOOKIE = "SESSIONID";
	
	private final static int DEFAULT_SESSION_TIMEOUT = 30 * 60;
	
	private String sessionIdCookieName = DEFAULT_SESSION_IDCOOKIE;
	
	public String getSessionIdCookieName() {
		return sessionIdCookieName;
	}
	
	public void setSessionIdCookieName(String sessionIdCookieName) {
		this.sessionIdCookieName = sessionIdCookieName;
	}
	
	/**
	 * 会话超时时间(秒) = DEFAULT_SESSION_TIMEOUT 30 * 60
	 */
	private int sessionTimeout = DEFAULT_SESSION_TIMEOUT;
	
	public int getSessionTimeout() {
		return this.sessionTimeout;
	}
	
	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}
	
	/**
	 * 会话验证定时周期(秒) = DEFAULT_SESSION_TIMEOUT / 2;
	 */
	private int sessionValidationInterval = DEFAULT_SESSION_TIMEOUT / 2;
	
	public int getSessionValidationInterval() {
		return this.sessionValidationInterval;
	}
	
	public void setSessionValidationInterval(int sessionValidationInterval) {
		this.sessionValidationInterval = sessionValidationInterval;
	}
	
	/**
	 * 全局会话超时时间(秒) = DEFAULT_SESSION_TIMEOUT 30 * 60
	 */
	private int globalSessionTimeout = DEFAULT_SESSION_TIMEOUT;
	
	public int getGlobalSessionTimeout() {
		return this.globalSessionTimeout;
	}
	
	public void setGlobalSessionTimeout(int globalSessionTimeout) {
		this.globalSessionTimeout = globalSessionTimeout;
	}
	
	private String authenticationCacheName = CACHE_AUTHENTICATIONCACHE;
	
	public String getAuthenticationCacheName() {
		return authenticationCacheName;
	}
	
	public void setAuthenticationCacheName(String authenticationCacheName) {
		this.authenticationCacheName = authenticationCacheName;
	}
	
	private String authorizationCacheName = CACHE_AUTHORIZATIONCACHE;
	
	public String getAuthorizationCacheName() {
		return authorizationCacheName;
	}
	
	public void setAuthorizationCacheName(String authorizationCacheName) {
		this.authorizationCacheName = authorizationCacheName;
	}
	
	@Bean(BEAN_LIFECYCLEBEANPOSTPROCESSOR)
	public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean(BEAN_SHIROREALM)
	public ShiroRealm shiroRealm() {
		ShiroRealm shiroRealm = new ShiroRealm();
		shiroRealm.setAuthenticationCachingEnabled(true);
		shiroRealm.setAuthenticationCacheName(this.getAuthenticationCacheName());
		shiroRealm.setAuthorizationCachingEnabled(true);
		shiroRealm.setAuthorizationCacheName(this.getAuthorizationCacheName());
		return shiroRealm;
	}
	
	@Bean(BEAN_SESSIONMANAGER)
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new com.zhangzlyuyx.easy.shiro.DefaultWebSessionManager();
		
		//会话访问
		sessionManager.setSessionDAO(this.getSessionDAO());
		
		//缓存管理
		sessionManager.setCacheManager(this.getCacheManager());
		
		//sessionIdCookie
		sessionManager.setSessionIdCookieEnabled(true);
		sessionManager.setSessionIdCookie(this.createSessionIdCookie());
		sessionManager.setSessionIdUrlRewritingEnabled(false);//去掉URL中的JSESSIONID
		
		//全局的session会话超时时间，单位为毫秒
		sessionManager.setGlobalSessionTimeout(this.getGlobalSessionTimeout() * 1000L);
		
		//session的失效扫描间隔，单位为毫秒
		sessionManager.setSessionValidationInterval(this.getSessionValidationInterval() * 1000L);
		
		//无效的Session定时调度器
		ExecutorServiceSessionValidationScheduler validationScheduler = new ExecutorServiceSessionValidationScheduler(sessionManager);
        validationScheduler.setInterval(this.getSessionValidationInterval() * 1000L);
        sessionManager.setSessionValidationScheduler(validationScheduler);
        sessionManager.setSessionValidationSchedulerEnabled(true);
		
		//删除所有无效的Session对象
		sessionManager.setDeleteInvalidSessions(true);
		
		this.onCreateSessionManager(sessionManager);
		return sessionManager;
	}
	
	@Bean(BEAN_SECURITYMANAGER)
	public org.apache.shiro.mgt.SecurityManager securityManager(@Qualifier(BEAN_SHIROREALM) ShiroRealm shiroRealm, @Qualifier(BEAN_SESSIONMANAGER) DefaultWebSessionManager sessionManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(shiroRealm);
		securityManager.setCacheManager(this.getCacheManager());
		securityManager.setSessionManager(sessionManager);
		this.onCreateSecurityManager(securityManager);
		return securityManager;
	}
	
	@Bean(BEAN_SHIROFILTERFACTORY)
	public ShiroFilterFactoryBean shiroFilterFactory(@Qualifier(BEAN_SECURITYMANAGER) org.apache.shiro.mgt.SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactory = new ShiroFilterFactoryBean();
		shiroFilterFactory.setSecurityManager(securityManager);
		shiroFilterFactory.setFilters(this.getFilters());
		shiroFilterFactory.setFilterChainDefinitionMap(this.getFilterChainDefinitionMap());
		this.onCreateShiroFilterFactory(shiroFilterFactory);
		return shiroFilterFactory;
	}
	
	@Bean(BEAN_ADVISORAUTOPROXYCREATOR)
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}
	
	@Bean(BEAN_AUTHORIZATIONATTRIBUTESOURCEADVISOR)
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier(BEAN_SECURITYMANAGER) org.apache.shiro.mgt.SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}
	
	/**
	 * 创建过滤器代理
	 * @return
	 */
	public DelegatingFilterProxy createDelegatingFilterProxy() {
		DelegatingFilterProxy proxy = new DelegatingFilterProxy();
		proxy.setTargetFilterLifecycle(true);
	    proxy.setTargetBeanName(BEAN_SHIROFILTERFACTORY);
		return proxy;
	}
	
	/**
	 * 创建会话id cookie
	 * @return
	 */
	public Cookie createSessionIdCookie() {
		SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName(this.getSessionIdCookieName());
        simpleCookie.setPath("/");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(this.getSessionTimeout());
        return simpleCookie;
	}
	
	/**
	 * 获取过滤器
	 * @return
	 */
	public abstract Map<String, Filter> getFilters();
	
	/**
	 * 获取过滤器规则
	 * @return
	 */
	public abstract Map<String, String> getFilterChainDefinitionMap();
	
	/**
	 * 获取缓存管理器
	 * @return
	 */
	public abstract CacheManager getCacheManager();
	
	/**
	 * 获取session数据访问
	 * @return
	 */
	public abstract SessionDAO getSessionDAO();
	
	/**
	 * 创建 shiroFilterFactory
	 * @param shiroFilterFactory
	 */
	public void onCreateShiroFilterFactory(ShiroFilterFactoryBean shiroFilterFactory) {
		
	}
	
	/**
	 * 创建会话管理器
	 * @param sessionManager
	 */
	public void onCreateSessionManager(SessionManager sessionManager) {
		
	}
	
	/**
	 * 创建安全管理器
	 * @param securityManager
	 */
	public void onCreateSecurityManager(SecurityManager securityManager) {
		
	}
}
