package com.zhangzlyuyx.easy.spring.freemarker;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.zhangzlyuyx.easy.core.util.StringUtils;
import com.zhangzlyuyx.easy.spring.util.SpringUtils;

public class FreeMarkerView extends org.springframework.web.servlet.view.freemarker.FreeMarkerView {

	private static final String CONTEXT_PATH = "path"; 
	
	private static final String STATIC_PATH = "staticPath";
	
	private String contextPathName = CONTEXT_PATH;
	
	public String getContextPathName() {
		return this.contextPathName;
	}
	
	public void setContextPathName(String contextPathName) {
		this.contextPathName = contextPathName;
	}
	
	private String staticPathName = STATIC_PATH;
	
	public String getStaticPathName() {
		return this.staticPathName;
	}
	
	public void setStaticPathName(String staticPathName) {
		this.staticPathName = staticPathName;
	}
	
	@Override
	protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
		String basePath = SpringUtils.getRequestPath(request);
		if(!StringUtils.isEmpty(this.getContextPathName())) {
			model.put(this.getContextPathName(), basePath);
		}
		if(!StringUtils.isEmpty(this.getStaticPathName())) {
			model.put(this.getStaticPathName(), basePath);
		}
		super.exposeHelpers(model, request);
	}
}
