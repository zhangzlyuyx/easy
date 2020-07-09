package com.zhangzlyuyx.easy.shiro.authc;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.util.StringUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhangzlyuyx.easy.core.util.ConvertUtils;
import com.zhangzlyuyx.easy.shiro.Constant;
import com.zhangzlyuyx.easy.shiro.ShiroRealm;
import com.zhangzlyuyx.easy.shiro.ShiroToken;
import com.zhangzlyuyx.easy.shiro.authz.AuthenticationHandler;

/**
 * cas token
 * @author zhangzlyuyx
 *
 */
public class CasToken extends org.apache.shiro.cas.CasToken implements ShiroToken {

	private static final long serialVersionUID = -2069735830319071117L;
	
	private static Logger log = LoggerFactory.getLogger(CasToken.class);
	
	/**
	 * token 分组
	 */
	private String group = Constant.SHIROTOKEN_DEFAULT_GROUP;
	
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

	public CasToken(String ticket) {
		super(ticket);
	}
	
	/**
	 * 解析 casToken
	 * @param casToken casToken
	 * @return
	 */
	public static CasToken parse(org.apache.shiro.cas.CasToken casToken) {
		CasToken token = new CasToken((String)casToken.getCredentials());
		token.setUserId((String)casToken.getPrincipal());
		token.setRememberMe(casToken.isRememberMe());
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
		TicketValidator ticketValidator = (TicketValidator)params.get(Constant.CAS_TICKETVALIDATOR_PARAM);
		String ticket = (String)this.getCredentials();
        if (!StringUtils.hasText(ticket)) {
            throw new AuthenticationException("无效的 ticket");
        }
        try {
            // contact CAS server to validate service ticket
            Assertion casAssertion = ticketValidator.validate(ticket, realm.getCasService());
            // get principal, user id and attributes
            AttributePrincipal casPrincipal = casAssertion.getPrincipal();
            String userId = casPrincipal.getName();
            log.debug("Validate ticket : {} in CAS server : {} to retrieve user : {}", new Object[]{ ticket, realm.getCasServerUrlPrefix(), userId});
            Map<String, Object> attributes = casPrincipal.getAttributes();
            // refresh authentication token (user id + remember me)
            this.setUserId(userId);
            String rememberMeAttributeName = realm.getRememberMeAttributeName();
            String rememberMeStringValue = (String)attributes.get(rememberMeAttributeName);
            boolean isRemembered = rememberMeStringValue != null && Boolean.parseBoolean(rememberMeStringValue);
            if (isRemembered) {
                this.setRememberMe(true);
            }
            this.getAttributes().putAll(attributes);
        } catch (TicketValidationException e) { 
            throw new AuthenticationException("Unable to validate ticket [" + ticket + "]", e);
        }
	}
}
