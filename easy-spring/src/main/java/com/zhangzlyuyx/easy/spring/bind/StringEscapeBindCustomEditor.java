package com.zhangzlyuyx.easy.spring.bind;

import java.beans.PropertyEditorSupport;

import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;

import com.zhangzlyuyx.easy.core.util.ConvertUtils;

public class StringEscapeBindCustomEditor extends PropertyEditorSupport {

	private boolean escapeHtml;
	
    private boolean escapeJs;
    
    public StringEscapeBindCustomEditor(boolean escapeHtml, boolean escapeJs) {
    	
    }
	
	@Override
	public String getAsText() {
		Object value = this.getValue();
		return ConvertUtils.toString(value, null);
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if(text == null) {
			this.setValue(null);
		} else {
			if(this.escapeHtml) {
				text = HtmlUtils.htmlEscape(text);
			}
			if(this.escapeJs) {
				text = JavaScriptUtils.javaScriptEscape(text);
			}
			this.setValue(text);
		}
	}
}
