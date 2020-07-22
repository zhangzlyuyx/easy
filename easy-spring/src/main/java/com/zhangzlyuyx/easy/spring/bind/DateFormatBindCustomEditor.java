package com.zhangzlyuyx.easy.spring.bind;

import java.beans.PropertyEditorSupport;
import java.util.Date;

import com.zhangzlyuyx.easy.core.util.DateUtils;
import com.zhangzlyuyx.easy.core.util.NumberUtils;
import com.zhangzlyuyx.easy.core.util.StringUtils;

public class DateFormatBindCustomEditor extends PropertyEditorSupport {

	private String dateFormat;
	
	public DateFormatBindCustomEditor(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	@Override
	public String getAsText() {
		Object value = this.getValue();
		if(value == null) {
			return null;
		}
		return DateUtils.format((Date)value, this.dateFormat);
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if(StringUtils.isEmpty(text)) {
			this.setValue(null);
		} else {
			if(NumberUtils.isLong(text)) {
				this.setValue(new Date(NumberUtils.parseLong(text)));
			} else {
				this.setValue(DateUtils.parse(text, this.dateFormat));
			}
		}
	}

}
