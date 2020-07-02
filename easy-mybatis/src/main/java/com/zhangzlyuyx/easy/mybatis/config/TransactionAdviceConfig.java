package com.zhangzlyuyx.easy.mybatis.config;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * AOP切面设置全局事务
 * @author zhangzlyuyx
 *
 */
public class TransactionAdviceConfig {

	/**
	 * 切点表达式
	 */
	private String aopPointcutExpression = "execution(* com..service.impl.*.*(..))";
	
	public String getAopPointcutExpression() {
		return this.aopPointcutExpression;
	}
	
	public void setAopPointcutExpression(String aopPointcutExpression) {
		this.aopPointcutExpression = aopPointcutExpression;
	}
	
	private String requiredMethods;
	
	/**
	 * 获取REQUIRED模式函数集合
	 * @return
	 */
	public String getRequiredMethods() {
		return this.requiredMethods;
	}
	
	public void setRequiredMethods(String requiredMethods) {
		this.requiredMethods = requiredMethods;
	}
	
	private String supportMethods;
	
	/**
	 * 获取SUPPORTS模式函数集合
	 * @return
	 */
	public String getSupportMethods() {
		return this.supportMethods;
	}
	
	public void setSupportMethods(String supportMethods) {
		this.supportMethods = supportMethods;
	}
	
	private DefaultTransactionAttribute txAttr_REQUIRED;
	
	public DefaultTransactionAttribute getTxAttr_REQUIRED() {
		if(this.txAttr_REQUIRED == null ) {
			this.txAttr_REQUIRED = new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED);
		}
		return txAttr_REQUIRED;
	}
	
	private DefaultTransactionAttribute txAttr_SUPPORTS;
	
	public DefaultTransactionAttribute getTxAttr_SUPPORTS() {
		if(this.txAttr_SUPPORTS == null) {
			this.txAttr_SUPPORTS = new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_SUPPORTS);
		}
		return txAttr_SUPPORTS;
	}
	
	public TransactionAdviceConfig() {
		
	}
	
	public TransactionAdviceConfig(String aopPointcutExpression) {
		this.aopPointcutExpression = aopPointcutExpression;
	}
	
	@PostConstruct
	public void init() {
		if(this.requiredMethods == null || this.requiredMethods.length() == 0) {
			this.requiredMethods = "add*,insert*,save*,delete*,remove*,update*,modify*,edit*,repair*,exec*,set*";
		}
		if(this.supportMethods == null || this.supportMethods.length() == 0) {
			this.supportMethods = "select*,get*,query*,find*,list*,count*,is*,*";
		}
	}
	
	/**
	 * 事务管理器
	 */
	@Autowired(required = false)
    private PlatformTransactionManager transactionManager;
	
	public PlatformTransactionManager getTransactionManager() {
		return this.transactionManager;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	@Bean
    public TransactionInterceptor txAdvice() {
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        Set<String> methodSet = new HashSet<>();
        if(this.getRequiredMethods() != null) {
        	for(String methodName : this.getRequiredMethods().split(",")) {
        		if(!methodSet.add(methodName)) {
        			continue;
        		}
        		source.addTransactionalMethod(methodName, this.getTxAttr_REQUIRED());
        	}
        }
        if(this.getSupportMethods() != null) {
        	for(String methodName : this.getSupportMethods().split(",")) {
        		if(!methodSet.add(methodName)) {
        			continue;
        		}
        		source.addTransactionalMethod(methodName, this.getTxAttr_SUPPORTS());
        	}
        }
        return new TransactionInterceptor(this.getTransactionManager(), source);
    }
	
	@Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(this.getAopPointcutExpression());
        return new DefaultPointcutAdvisor(pointcut, this.txAdvice());
    }
}
